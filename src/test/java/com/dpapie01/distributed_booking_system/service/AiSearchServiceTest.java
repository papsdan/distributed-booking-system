package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.ai.OllamaClient;
import com.dpapie01.distributed_booking_system.dto.GameFilterDTO;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AiSearchServiceTest {

    @Mock
    private OllamaClient ollamaClient;
    @Mock
    private LocationRepository locationRepository;

    private AiSearchService aiSearchService;

    private static final LocalDate TODAY = LocalDate.of(2026, 10, 19); // a Monday

    @BeforeEach
    void setUp() {
        aiSearchService = new AiSearchService(ollamaClient, locationRepository, new SimpleMeterRegistry());
    }

    private void givenKnownLocation() {
        when(locationRepository.findAll()).thenReturn(List.of(new Location(1L, "Leeds", "City Centre")));
    }

    @Test
    void testSearch_BlankQuery_ReturnsEmptyWithoutCallingOllama() {
        Optional<GameFilterDTO> result = aiSearchService.search("   ", TODAY);

        assertTrue(result.isEmpty());
        verify(ollamaClient, never()).chat(anyString(), anyString(), any());
    }

    @Test
    void testSearch_OllamaFails_ReturnsEmpty() {
        givenKnownLocation();
        when(ollamaClient.chat(anyString(), anyString(), any())).thenThrow(new RuntimeException("connection refused"));

        Optional<GameFilterDTO> result = aiSearchService.search("5-a-side in Leeds", TODAY);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_OllamaReturnsInvalidJson_ReturnsEmpty() {
        givenKnownLocation();
        when(ollamaClient.chat(anyString(), anyString(), any())).thenReturn("Sure! Here are some games...");

        Optional<GameFilterDTO> result = aiSearchService.search("5-a-side in Leeds", TODAY);

        assertTrue(result.isEmpty());
    }

    @Test
    void testSearch_HappyPath_MapsAllFields() {
        givenKnownLocation();
        when(ollamaClient.chat(anyString(), anyString(), any())).thenReturn("""
                {"city":"leeds","area":null,"gameType":"FIVE_A_SIDE","genderOption":"WOMEN",
                 "gameDate":"2026-10-24","maxPrice":5,"openSlotsOnly":true}
                """);

        Optional<GameFilterDTO> result = aiSearchService.search("women's 5-a-side in Leeds under £5 with open slots", TODAY);

        assertTrue(result.isPresent());
        GameFilterDTO filter = result.get();
        assertEquals("Leeds", filter.getCity()); // matched back to the DB's own spelling
        assertNull(filter.getArea());
        assertEquals(GameType.FIVE_A_SIDE, filter.getGameType());
        assertEquals(GameGenderOption.WOMEN, filter.getGenderOption());
        assertEquals(LocalDate.of(2026, 10, 24), filter.getGameDate());
        assertEquals(0, BigDecimal.valueOf(5).compareTo(filter.getMaxPrice()));
        assertTrue(filter.isOpenSlotsOnly());
    }

    @Test
    void testToFilter_UnknownCity_IsDropped() {
        GameFilterDTO filter = aiSearchService.toFilter(
                """
                {"city":"Atlantis","area":null,"gameType":null,"genderOption":null,
                 "gameDate":null,"maxPrice":null,"openSlotsOnly":false}
                """, TODAY, List.of(new Location(1L, "Leeds", "City Centre")));

        assertNull(filter.getCity());
    }

    @Test
    void testToFilter_AreaFromWrongCity_IsDropped() {
        // "Clapham" is a real area, but it belongs to London, not Bristol - the model
        // pairing them should not produce a city/area combination that can't exist.
        GameFilterDTO filter = aiSearchService.toFilter(
                """
                {"city":"Bristol","area":"Clapham","gameType":null,"genderOption":null,
                 "gameDate":null,"maxPrice":null,"openSlotsOnly":false}
                """, TODAY, List.of(
                        new Location(1L, "London", "Clapham"),
                        new Location(2L, "Bristol", "Bedminster")));

        assertEquals("Bristol", filter.getCity());
        assertNull(filter.getArea());
    }

    @Test
    void testToFilter_PastDate_IsDropped() {
        GameFilterDTO filter = aiSearchService.toFilter(
                """
                {"city":null,"area":null,"gameType":null,"genderOption":null,
                 "gameDate":"2020-01-01","maxPrice":null,"openSlotsOnly":false}
                """, TODAY, List.of());

        assertNull(filter.getGameDate());
    }

    @Test
    void testToFilter_NegativePrice_IsDropped() {
        GameFilterDTO filter = aiSearchService.toFilter(
                """
                {"city":null,"area":null,"gameType":null,"genderOption":null,
                 "gameDate":null,"maxPrice":-10,"openSlotsOnly":false}
                """, TODAY, List.of());

        assertNull(filter.getMaxPrice());
    }

    @Test
    void testToFilter_UnknownGameType_IsDropped() {
        GameFilterDTO filter = aiSearchService.toFilter(
                """
                {"city":null,"area":null,"gameType":"FUTSAL","genderOption":null,
                 "gameDate":null,"maxPrice":null,"openSlotsOnly":false}
                """, TODAY, List.of());

        assertNull(filter.getGameType());
    }

    @Test
    void testBuildSystemPrompt_IncludesTodayAndWeekday() {
        String prompt = aiSearchService.buildSystemPrompt(TODAY, List.of(new Location(1L, "Leeds", "City Centre")));

        assertTrue(prompt.contains("2026-10-19"));
        assertTrue(prompt.contains("MONDAY"));
        assertTrue(prompt.contains("Leeds"));
    }

    @Test
    void testSearch_QueryOverMaxLength_IsTruncatedNotRejected() {
        givenKnownLocation();
        String longQuery = "a".repeat(AiSearchService.MAX_QUERY_LENGTH + 50);
        when(ollamaClient.chat(anyString(), anyString(), any())).thenReturn("""
                {"city":null,"area":null,"gameType":null,"genderOption":null,
                 "gameDate":null,"maxPrice":null,"openSlotsOnly":false}
                """);

        Optional<GameFilterDTO> result = aiSearchService.search(longQuery, TODAY);

        assertTrue(result.isPresent());
        assertFalse(result.get().isOpenSlotsOnly());
        verify(ollamaClient).chat(anyString(),
                argThat(q -> q.length() == AiSearchService.MAX_QUERY_LENGTH), any());
    }

    @Test
    void testToFilter_GameTypeLabelAndDayWord_AreResolvedInJava() {
        // The model is told to copy day words (not do date maths) and may use either the
        // enum name or the display label for gameType - both should resolve correctly.
        GameFilterDTO filter = aiSearchService.toFilter(
                """
                {"city":null,"area":null,"gameType":"5-a-side","genderOption":null,
                 "gameDate":"SATURDAY","maxPrice":null,"openSlotsOnly":false}
                """, TODAY, List.of());

        assertEquals(GameType.FIVE_A_SIDE, filter.getGameType());
        assertEquals(LocalDate.of(2026, 10, 24), filter.getGameDate()); // Monday 19th -> Saturday 24th
    }
}
