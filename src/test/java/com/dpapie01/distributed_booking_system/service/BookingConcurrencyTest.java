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
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
class BookingConcurrencyTest {

    private static final int PLAYER_COUNT = 20;

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
        location.setCity("City_ConcurrencyTest");
        location.setArea("Area_ConcurrencyTest");
        location = locationRepository.save(location);

        pitch = new Pitch();
        pitch.setName("Pitch_Concurrency_Test_ " + System.nanoTime());
        pitch.setLocation(location);
        pitch.setCapacity(10);
        pitch.setActive(true);
        pitch = pitchRepository.save(pitch);

        organiser = createUser("organiser");

        game = new Game();
        game.setPitch(pitch);
        game.setOrganiser(organiser);
        game.setTitle("Game_Concurrency_Test");
        game.setGameDate(LocalDate.now().plusDays(7));
        game.setGameTime(LocalTime.of(18, 0));
        game.setDurationMinutes(60);
        game.setGameType(GameType.FIVE_A_SIDE);
        game.setGenderOption(GameGenderOption.MIXED);
        game.setMaxPlayers(1);
        game.setPrice(BigDecimal.ZERO);
        game.setPaymentType(PaymentType.FREE);
        game.setRefundPolicy(RefundPolicy.NO_REFUND);
        game = gameRepository.save(game);

        GameSlot slot = new GameSlot();
        slot.setGame(game);
        slot.setStatus(GameSlotStatus.AVAILABLE);
        gameSlotRepository.save(slot);

        for (int i = 0; i < PLAYER_COUNT; i++) {
            players.add(createUser("User_Concurrency_Test_Player" + i));
        }
    }

    private User createUser(String label) {
        String unique = label + "-" + System.nanoTime();

        User user = new User();
        user.setFirstName("Concurrency_Test");
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

    @Test
    void testHoldSlot_OnlyOneWinsWhenManyPlayersRaceForLastSlot() throws InterruptedException {
        CountDownLatch startLatch = new CountDownLatch(1);
        CountDownLatch endLatch = new CountDownLatch(PLAYER_COUNT);
        AtomicInteger successCount = new AtomicInteger(0);
        AtomicInteger failureCount = new AtomicInteger(0);

        try (ExecutorService executor = Executors.newFixedThreadPool(PLAYER_COUNT)) {
            for (User player : players) {
                executor.submit(() -> {
                    try {
                        startLatch.await();
                        bookingService.holdSlot(game.getId(), player.getEmail());
                        successCount.incrementAndGet();
                    } catch (ResponseStatusException e) {
                        failureCount.incrementAndGet();
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    } finally {
                        endLatch.countDown();
                    }
                });
            }

            startLatch.countDown();
            endLatch.await();
        }

        assertEquals(1, successCount.get());
        assertEquals(PLAYER_COUNT - 1, failureCount.get());
    }

    @AfterEach
    void tearDown() {
        bookingRepository.deleteAll(bookingRepository.findBySlot_GameAndStatus(game, BookingStatus.HELD));
        gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.HELD).ifPresent(gameSlotRepository::delete);
        gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE).ifPresent(gameSlotRepository::delete);
        gameRepository.delete(game);
        profileRepository.deleteAll(profiles);
        userRepository.delete(organiser);
        userRepository.deleteAll(players);
        pitchRepository.delete(pitch);
        locationRepository.delete(location);
    }
}
