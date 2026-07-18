package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Location;
import com.dpapie01.distributed_booking_system.entity.Pitch;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import com.dpapie01.distributed_booking_system.enums.Role;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.LocationRepository;
import com.dpapie01.distributed_booking_system.repository.PitchRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class BookingLoadTest {

    private static final int PLAYER_COUNT = 40;
    private static final int SLOT_COUNT = 22;
    private static final long RESPONSIVE_THRESHOLD_MS = 1000;

    @Autowired
    private BookingService bookingService;
    @Autowired
    private LocationRepository locationRepository;
    @Autowired
    private PitchRepository pitchRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProfileRepository profileRepository;
    @Autowired
    private GameRepository gameRepository;
    @Autowired
    private GameSlotRepository gameSlotRepository;
    @Autowired
    private BookingRepository bookingRepository;

    private Location location;
    private Pitch pitch;
    private Game game;
    private User organiser;
    private final List<User> players = new ArrayList<>();
    private final List<Profile> profiles = new ArrayList<>();

    @BeforeEach
    void setUp() {
        location = new Location();
        location.setCity("City_LoadTest");
        location.setArea("Area_LoadTest");
        location = locationRepository.save(location);

        pitch = new Pitch();
        pitch.setName("Pitch_LoadTest_" + System.nanoTime());
        pitch.setLocation(location);
        pitch.setCapacity(SLOT_COUNT);
        pitch.setActive(true);
        pitch = pitchRepository.save(pitch);

        organiser = createUser("organiser");

        game = new Game();
        game.setPitch(pitch);
        game.setOrganiser(organiser);
        game.setTitle("Game_Load_Test");
        game.setGameDate(LocalDate.now().plusDays(7));
        game.setGameTime(LocalTime.of(18, 0));
        game.setDurationMinutes(60);
        game.setGameType(GameType.ELEVEN_A_SIDE);
        game.setGenderOption(GameGenderOption.MIXED);
        game.setMaxPlayers(SLOT_COUNT);
        game.setPrice(BigDecimal.ZERO);
        game.setPaymentType(PaymentType.FREE);
        game.setRefundPolicy(RefundPolicy.NO_REFUND);
        game = gameRepository.save(game);

        for (int i = 0; i < PLAYER_COUNT; i++) {
            players.add(createUser("User_Concurrency_Test_Player" + i));
        }
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll(bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.HELD));
        gameSlotRepository.deleteAll(gameSlotRepository.findByGame(game));
        gameRepository.delete(game);
        profileRepository.deleteAll(profiles);
        userRepository.delete(organiser);
        userRepository.deleteAll(players);
        pitchRepository.delete(pitch);
        locationRepository.delete(location);
    }


    @Test
    void testHoldSlot_SingleSlotRaceStaysResponsive() throws InterruptedException {
        GameSlot slot = new GameSlot();
        slot.setGame(game);
        slot.setStatus(GameSlotStatus.AVAILABLE);
        gameSlotRepository.save(slot);

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(PLAYER_COUNT);
        long start;

        try (ExecutorService executor = Executors.newFixedThreadPool(PLAYER_COUNT)) {
            for (User player : players) {
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        bookingService.holdSlot(game.getId(), player.getEmail());
                    } catch (ResponseStatusException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            start = System.nanoTime();
            startLatch.countDown();
            endLatch.await();
        }

        long elapsedMs = (System.nanoTime() - start) / 1000000;
        System.out.println("Single slot race: " + PLAYER_COUNT + " players racing for 1 slot took " + elapsedMs + "ms");
        assertTrue(elapsedMs < RESPONSIVE_THRESHOLD_MS,"Single slot race took " + elapsedMs + "ms. Expected to be under " + RESPONSIVE_THRESHOLD_MS + "ms");
    }

    @Test
    void testHoldSlot_ManySlotsLoadStaysResponsive() throws InterruptedException {

        for (int i = 0; i < SLOT_COUNT; i++) {
            GameSlot slot = new GameSlot();
            slot.setGame(game);
            slot.setStatus(GameSlotStatus.AVAILABLE);
            gameSlotRepository.save(slot);
        }

        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(PLAYER_COUNT);
        long start;

        try (ExecutorService executor = Executors.newFixedThreadPool(PLAYER_COUNT)) {
            for (User player : players) {
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        bookingService.holdSlot(game.getId(), player.getEmail());
                    } catch (ResponseStatusException e) {
                        System.out.println(e.getMessage());
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            start = System.nanoTime();
            startLatch.countDown();
            endLatch.await();
        }

        long elapsedMs = (System.nanoTime() - start) / 1000000;
        System.out.println("Many slots load: " + PLAYER_COUNT + " players racing for " + SLOT_COUNT + " slots took " + elapsedMs + "ms");
        assertTrue(elapsedMs < RESPONSIVE_THRESHOLD_MS,"Many slots load took " + elapsedMs + "ms. Expected to be under " + RESPONSIVE_THRESHOLD_MS + "ms");
    }

    private User createUser(String label) {
        String unique = label + "-" + System.nanoTime();

        User user = new User();
        user.setFirstName("Load_Test");
        user.setLastName(label);
        user.setUsername(unique);
        user.setEmail(unique + "@test.com");
        user.setPassword("password123");
        user.setRole(Role.PLAYER);
        user.setActive(true);
        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setUser(user);
        profile.setPreferredLocation(location);
        profile.setGender(Gender.MAN);
        profiles.add(profileRepository.save(profile));

        return user;
    }
}
