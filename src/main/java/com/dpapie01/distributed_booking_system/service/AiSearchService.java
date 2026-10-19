package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.ai.OllamaClient;
import com.dpapie01.distributed_booking_system.dto.GameFilterDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Timer;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.json.JsonMapper;

import java.math.BigDecimal;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.time.temporal.TemporalAdjusters;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * Turns a plain-English search ("women's 5-a-side in Leeds this Saturday under £5")
 * into a GameFilterDTO, using a local LLM running in Ollama.
 *
 * The key design idea: the AI only TRANSLATES the sentence into filter fields.
 * It never touches bookings or the database. The existing, tested GameService.filterGames
 * does the actual search, so the booking logic stays deterministic.
 *
 * Flow:
 *   1. Build a prompt that tells the model today's date and the allowed values.
 *   2. Ask Ollama for JSON in a fixed shape.
 *   3. Validate every field. Anything the model got wrong is dropped, never trusted.
 *   4. If anything fails (Ollama down, timeout, bad JSON), return Optional.empty()
 *      so the controller can fall back to the normal filter form.
 */
@Slf4j
@Service
public class AiSearchService {

    /** Ignore very long input: keeps prompts small and stops people pasting essays. */
    static final int MAX_QUERY_LENGTH = 200;

    private final OllamaClient ollamaClient;
    private final LocationRepository locationRepository;
    private final MeterRegistry meterRegistry;
    private final JsonMapper jsonMapper = JsonMapper.builder().build();

    public AiSearchService(OllamaClient ollamaClient,
                            LocationRepository locationRepository,
                            MeterRegistry meterRegistry) {
        this.ollamaClient = ollamaClient;
        this.locationRepository = locationRepository;
        this.meterRegistry = meterRegistry;
    }

    /**
     * Converts a natural-language query into game filters.
     *
     * @param query what the player typed
     * @param today today's date, passed in (rather than calling LocalDate.now() here)
     *              so tests can fix the date and check that "Saturday" resolves correctly
     * @return the filters, or empty if the AI search failed and we should fall back
     */
    public Optional<GameFilterDTO> search(String query, LocalDate today) {
        if (query == null || query.isBlank()) {
            return Optional.empty();
        }
        String trimmed = query.strip();
        if (trimmed.length() > MAX_QUERY_LENGTH) {
            trimmed = trimmed.substring(0, MAX_QUERY_LENGTH);
        }

        List<Location> locations = locationRepository.findAll();

        // Timer.Sample measures how long the AI call takes. The result shows up in
        // /actuator/prometheus as kickabout_ai_search_seconds, tagged success or fallback.
        Timer.Sample sample = Timer.start(meterRegistry);

        try {
            String reply = ollamaClient.chat(buildSystemPrompt(today, locations), trimmed, responseSchema());
            GameFilterDTO filter = toFilter(reply, today, locations);
            sample.stop(meterRegistry.timer("kickabout.ai.search", "outcome", "success"));
            return Optional.of(filter);
        } catch (RuntimeException e) {
            // Catching broadly is deliberate here: whatever went wrong, the player should
            // still be able to search. Log it, count it, and fall back.
            log.warn("AI search failed, falling back to manual filters: {}", e.getMessage());
            sample.stop(meterRegistry.timer("kickabout.ai.search", "outcome", "fallback"));
            return Optional.empty();
        }
    }

    /**
     * The model can't know today's date or which cities exist in our database,
     * so we tell it. Listing the allowed values makes it far more accurate.
     */
    String buildSystemPrompt(LocalDate today, List<Location> locations) {
        String cities = locations.stream().map(Location::getCity).distinct().sorted().toList().toString();
        String areas = locations.stream().map(Location::getArea).distinct().sorted().toList().toString();
        String gameTypes = Arrays.stream(GameType.values())
                .map(t -> t.name() + " (" + t.getLabel() + ")")
                .toList().toString();

        return """
                You convert a football player's search into filters for a game listing.
                Reply with JSON only.

                IMPORTANT: only set a field if the player's text actually mentions it. If a field
                is not mentioned, its value MUST be null (or false for openSlotsOnly). Never invent
                or guess a value - for example, do not pick a gameType (e.g. 5-a-side, 10-a-side)
                unless the player's text states or clearly implies a specific format.

                Today is %s (%s). This is ONLY so you can recognise words like "today" or
                "tomorrow" if the player uses them - it is NOT a value to put in gameDate.
                - gameDate: do NOT work out dates yourself, and do NOT default to today's date.
                  If the player did not mention a day at all, gameDate MUST be null - this is the
                  most common case. If they mentioned one specific day, copy it as one word:
                  TODAY, TOMORROW, or a weekday such as SATURDAY. Only if they gave an exact date,
                  write it as yyyy-MM-dd. If they mentioned a range or period instead (e.g. "next
                  week", "this month", "this weekend" meaning both Sat and Sun), leave this null -
                  a single date cannot represent a range.
                - city: one of %s. Otherwise null.
                - area: one of %s. Otherwise null.
                - gameType: one of %s. Only set this if a specific format (e.g. "5-a-side", "5s",
                  "11s") is explicitly in the player's text. Otherwise null.
                - genderOption: one of %s. "women's", "ladies" or "female" means WOMEN;
                  "men's" means MEN; "mixed" means MIXED. Otherwise null.
                - maxPrice: a number in pounds, only if a price limit or "free" was mentioned.
                  Otherwise null.
                - openSlotsOnly: true only if the player explicitly asked for games with spaces
                  left (e.g. "open slots", "spaces available"). Otherwise false.

                Examples:
                Player: "5-a-side in Leeds"
                {"city":"Leeds","area":null,"gameType":"FIVE_A_SIDE","genderOption":null,"gameDate":null,"maxPrice":null,"openSlotsOnly":false}

                Player: "5-a-side in Tottenham Hale on Saturday"
                {"city":"London","area":"Tottenham Hale","gameType":"FIVE_A_SIDE","genderOption":null,"gameDate":"SATURDAY","maxPrice":null,"openSlotsOnly":false}

                Player: "women's games under £5 with open slots"
                {"city":null,"area":null,"gameType":null,"genderOption":"WOMEN","gameDate":null,"maxPrice":5,"openSlotsOnly":true}

                Player: "games in Tottenham Hale next week"
                {"city":"London","area":"Tottenham Hale","gameType":null,"genderOption":null,"gameDate":null,"maxPrice":null,"openSlotsOnly":false}
                """.formatted(today, today.getDayOfWeek(), cities, areas, gameTypes,
                Arrays.toString(GameGenderOption.values()));
    }

    /**
     * A JSON Schema describing the reply we want. Ollama uses this to constrain the
     * model's output, so it can't reply with chatty text like "Sure! Here are...".
     */
    Map<String, Object> responseSchema() {
        Map<String, Object> nullableString = Map.of("type", List.of("string", "null"));
        return Map.of(
                "type", "object",
                "properties", Map.of(
                        "city", nullableString,
                        "area", nullableString,
                        "gameType", nullableString,
                        "genderOption", nullableString,
                        "gameDate", nullableString,
                        "maxPrice", Map.of("type", List.of("number", "null")),
                        "openSlotsOnly", Map.of("type", "boolean")),
                "required", List.of("city", "area", "gameType", "genderOption",
                        "gameDate", "maxPrice", "openSlotsOnly"));
    }

    /**
     * Never trust LLM output directly. Each field is checked against real values;
     * anything invalid is simply left out of the filter (treated as "any").
     */
    GameFilterDTO toFilter(String reply, LocalDate today, List<Location> locations) {
        JsonNode json = jsonMapper.readTree(reply); // throws if the reply isn't valid JSON

        GameFilterDTO filter = new GameFilterDTO();
        String city = matchKnown(text(json, "city"), locations.stream().map(Location::getCity).toList());
        filter.setCity(city);
        // Scope area matching to the resolved city's own areas, otherwise a model like
        // "Bristol, Clapham" (Clapham is actually a London area) slips through as valid.
        List<String> areasInCity = locations.stream()
                .filter(loc -> city == null || loc.getCity().equals(city))
                .map(Location::getArea).toList();
        filter.setArea(matchKnown(text(json, "area"), areasInCity));
        filter.setGameType(parseGameType(text(json, "gameType")));
        filter.setGenderOption(parseEnum(GameGenderOption.class, text(json, "genderOption")));
        filter.setGameDate(resolveDate(text(json, "gameDate"), today));
        filter.setMaxPrice(parsePrice(json.get("maxPrice")));
        filter.setOpenSlotsOnly(json.path("openSlotsOnly").asBoolean(false));
        return filter;
    }

    /** Reads a text field, treating missing, null and blank all as "not mentioned". */
    private String text(JsonNode json, String field) {
        JsonNode node = json.get(field);
        if (node == null || node.isNull()) {
            return null;
        }
        String value = node.asString().strip();
        return value.isEmpty() ? null : value;
    }

    /**
     * Returns the database's own spelling of a city or area (e.g. "leeds" becomes "Leeds")
     * because the repository query matches exactly. Unknown places are dropped.
     */
    private String matchKnown(String value, List<String> known) {
        if (value == null) {
            return null;
        }
        return known.stream().filter(k -> k.equalsIgnoreCase(value)).findFirst().orElse(null);
    }

    /** "FIVE_A_SIDE" becomes GameType.FIVE_A_SIDE. Made-up values like "FUTSAL" become null. */
    private <E extends Enum<E>> E parseEnum(Class<E> type, String value) {
        if (value == null) {
            return null;
        }
        try {
            return Enum.valueOf(type, value.toUpperCase());
        } catch (IllegalArgumentException e) {
            return null;
        }
    }

    /** Accepts either the enum name ("FIVE_A_SIDE") or the display label ("5-a-side"). */
    private GameType parseGameType(String value) {
        if (value == null) {
            return null;
        }
        return Arrays.stream(GameType.values())
                .filter(t -> t.name().equalsIgnoreCase(value) || t.getLabel().equalsIgnoreCase(value))
                .findFirst().orElse(null);
    }

    /** The model copies the day word; Java does the date maths (llama3.2 got "Saturday" wrong in testing). */
    private LocalDate resolveDate(String value, LocalDate today) {
        if (value == null) {
            return null;
        }
        String word = value.strip().toUpperCase();
        if (word.equals("TODAY")) {
            return today;
        }
        if (word.equals("TOMORROW")) {
            return today.plusDays(1);
        }
        try {
            return today.with(TemporalAdjusters.nextOrSame(DayOfWeek.valueOf(word)));
        } catch (IllegalArgumentException notAWeekday) {
            // not a weekday, so try an exact date below
        }
        try {
            LocalDate date = LocalDate.parse(value.strip());
            return date.isBefore(today) ? null : date;
        } catch (DateTimeParseException e) {
            return null;
        }
    }

    /** Accepts zero or positive prices only. */
    private BigDecimal parsePrice(JsonNode node) {
        if (node == null || !node.isNumber()) {
            return null;
        }
        BigDecimal price = node.decimalValue();
        return price.signum() < 0 ? null : price;
    }
}
