package com.dpapie01.distributed_booking_system.service;

import com.dpapie01.distributed_booking_system.dto.BookingResponeDTO;
import com.dpapie01.distributed_booking_system.entity.Booking;
import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.entity.GameSlot;
import com.dpapie01.distributed_booking_system.entity.Profile;
import com.dpapie01.distributed_booking_system.entity.User;
import com.dpapie01.distributed_booking_system.enums.BookingStatus;
import com.dpapie01.distributed_booking_system.enums.Gender;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameSlotStatus;
import com.dpapie01.distributed_booking_system.enums.GameStatus;
import com.dpapie01.distributed_booking_system.enums.GameType;
import com.dpapie01.distributed_booking_system.enums.PaymentType;
import com.dpapie01.distributed_booking_system.enums.RefundPolicy;
import com.dpapie01.distributed_booking_system.mapper.BookingMapper;
import com.dpapie01.distributed_booking_system.repository.BookingRepository;
import com.dpapie01.distributed_booking_system.repository.CreditRepository;
import com.dpapie01.distributed_booking_system.repository.GameRepository;
import com.dpapie01.distributed_booking_system.repository.GameSlotRepository;
import com.dpapie01.distributed_booking_system.repository.ProfileRepository;
import com.dpapie01.distributed_booking_system.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class BookingServiceTest {

    @Mock
    private GameRepository gameRepository;
    @Mock
    private GameSlotRepository gameSlotRepository;
    @Mock
    private BookingRepository bookingRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ProfileRepository profileRepository;
    @Mock
    private CreditRepository creditRepository;
    @Mock
    private BookingMapper bookingMapper;

    @InjectMocks
    private BookingService bookingService;

    private Game game;
    private User user;
    private GameSlot slot;
    private Profile profile;

    @BeforeEach
    void setUp() {
        game = new Game();
        game.setId(1L);
        game.setGameDate(LocalDate.of(2026, 7, 20));
        game.setGameTime(LocalTime.of(18, 0));
        game.setDurationMinutes(60);
        game.setGameType(GameType.TEN_A_SIDE);
        game.setGenderOption(GameGenderOption.MIXED);
        game.setMaxPlayers(10);
        game.setPrice(BigDecimal.ZERO);
        game.setPaymentType(PaymentType.FREE);
        game.setStatus(GameStatus.OPEN);

        user = new User();
        user.setId(2L);
        user.setEmail("jane@example.com");

        slot = new GameSlot();
        slot.setId(5L);
        slot.setGame(game);
        slot.setStatus(GameSlotStatus.AVAILABLE);

        profile = new Profile();
        profile.setUser(user);
        profile.setGender(Gender.WOMAN);


    }

    @Test
    void testBookSlot_GameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testBookSlot_UserNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testBookSlot_GameCancelled() {
        game.setStatus(GameStatus.CANCELLED);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game has been cancelled", ex.getReason());
    }

    @Test
    void testBookSlot_GameAlreadyPlayed() {
        game.setGameDate(LocalDate.of(2020, 1, 1));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game has already taken place", ex.getReason());
    }

    @Test
    void testBookSlot_GenderIneligible_WomanBlockedFromMensGame() {
        game.setGenderOption(GameGenderOption.MENS);
        profile.setGender(Gender.WOMAN);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game is open to Men only", ex.getReason());
    }

    @Test
    void testBookSlot_GenderIneligible_ManBlockedFromWomensGame() {
        game.setGenderOption(GameGenderOption.WOMENS);
        profile.setGender(Gender.MAN);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game is open to Women only", ex.getReason());
    }

    @Test
    void testBookSlot_GenderIneligible_PreferNotToSayBlockedFromMensGame() {
        game.setGenderOption(GameGenderOption.MENS);
        profile.setGender(Gender.PREFER_NOT_TO_SAY);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game is open to Men only", ex.getReason());
    }

    @Test
    void testBookSlot_GenderIneligible_PreferNotToSayBlockedFromWomensGame() {
        game.setGenderOption(GameGenderOption.WOMENS);
        profile.setGender(Gender.PREFER_NOT_TO_SAY);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game is open to Women only", ex.getReason());
    }

    @Test
    void testBookSlot_PreferNotToSayEligibleForMixedGame() {
        game.setGenderOption(GameGenderOption.MIXED);
        profile.setGender(Gender.PREFER_NOT_TO_SAY);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(10L);
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        assertEquals(GameSlotStatus.BOOKED, slot.getStatus());
    }

    @Test
    void testBookSlot_NonBinaryEligibleForMensGame() {
        game.setGenderOption(GameGenderOption.MENS);
        profile.setGender(Gender.NON_BINARY);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(10L);
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        assertEquals(GameSlotStatus.BOOKED, slot.getStatus());
    }

    @Test
    void testBookSlot_NonBinaryEligibleForWomensGame() {
        game.setGenderOption(GameGenderOption.WOMENS);
        profile.setGender(Gender.NON_BINARY);
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(10L);
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        assertEquals(GameSlotStatus.BOOKED, slot.getStatus());
    }

    @Test
    void testBookSlot_GameFull() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(0L);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("This game is full", ex.getReason());
    }

    @Test
    void testBookSlot_AlreadyBooked() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(bookingRepository.existsBySlot_GameAndUserAndStatus(game,user,BookingStatus.CONFIRMED)).thenReturn(true);
        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You have already booked into this game", ex.getReason());
    }

    @Test
    void testBookSlot_Valid() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(10L);
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        assertEquals(GameSlotStatus.BOOKED, slot.getStatus());
        verify(gameSlotRepository).save(slot);
        verify(bookingRepository).save(argThat(booking ->
                booking.getSlot() == slot
                        && booking.getUser() == user
                        && booking.getStatus() == BookingStatus.CONFIRMED
                        && booking.getConfirmedAt() != null));
    }

    @Test
    void testBookSlot_OverlappingBookingBlocksJoin() {
        Game otherGame = new Game();
        otherGame.setId(2L);
        otherGame.setGameDate(LocalDate.of(2026, 7, 20));
        otherGame.setGameTime(LocalTime.of(18, 30));
        otherGame.setDurationMinutes(60);
        otherGame.setStatus(GameStatus.OPEN);

        GameSlot otherSlot = new GameSlot();
        otherSlot.setGame(otherGame);

        Booking otherBooking = new Booking();
        otherBooking.setSlot(otherSlot);
        otherBooking.setUser(user);
        otherBooking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(bookingRepository.findByUserAndStatus(user, BookingStatus.CONFIRMED)).thenReturn(List.of(otherBooking));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You already have a booking that overlaps with this game's time", ex.getReason());
    }

    @Test
    void testBookSlot_NonOverlappingBookingAllowsJoin() {
        Game otherGame = new Game();
        otherGame.setId(2L);
        otherGame.setGameDate(LocalDate.of(2026, 7, 20));
        otherGame.setGameTime(LocalTime.of(20, 0));
        otherGame.setDurationMinutes(60);
        otherGame.setStatus(GameStatus.OPEN);

        GameSlot otherSlot = new GameSlot();
        otherSlot.setGame(otherGame);

        Booking otherBooking = new Booking();
        otherBooking.setSlot(otherSlot);
        otherBooking.setUser(user);
        otherBooking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(bookingRepository.findByUserAndStatus(user, BookingStatus.CONFIRMED)).thenReturn(List.of(otherBooking));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(10L);
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        assertEquals(GameSlotStatus.BOOKED, slot.getStatus());
    }

    @Test
    void testGetMyBookings_UserNotFound() {
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.getMyBookings("jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testGetMyBookings_NoBookings() {
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findByUser(user)).thenReturn(List.of());

        List<BookingResponeDTO> result = bookingService.getMyBookings("jane@example.com");

        assertEquals(0, result.size());
    }

    @Test
    void testGetMyBookings_ReturnsBookings() {
        Booking booking1 = new Booking();
        booking1.setId(10L);
        booking1.setSlot(slot);
        booking1.setUser(user);
        booking1.setStatus(BookingStatus.CONFIRMED);
        booking1.setCreatedAt(LocalDateTime.of(2026, 7, 1, 10, 0));

        Game otherGame = new Game();
        otherGame.setId(2L);
        GameSlot otherSlot = new GameSlot();
        otherSlot.setGame(otherGame);

        Booking booking2 = new Booking();
        booking2.setId(11L);
        booking2.setSlot(otherSlot);
        booking2.setUser(user);
        booking2.setStatus(BookingStatus.WITHDRAWN);
        booking2.setCreatedAt(LocalDateTime.of(2026, 7, 1, 10, 0));

        BookingResponeDTO dto1 = new BookingResponeDTO();
        dto1.setId(10L);
        BookingResponeDTO dto2 = new BookingResponeDTO();
        dto2.setId(11L);

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findByUser(user)).thenReturn(List.of(booking1, booking2));
        when(bookingMapper.toResponseDTO(booking1)).thenReturn(dto1);
        when(bookingMapper.toResponseDTO(booking2)).thenReturn(dto2);

        List<BookingResponeDTO> result = bookingService.getMyBookings("jane@example.com");

        assertEquals(List.of(dto1, dto2), result);
    }

    @Test
    void testWithdrawSlot_GameNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.withdrawSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("Game not found", ex.getReason());
    }

    @Test
    void testWithdrawSlot_UserNotFound() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.withdrawSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
        assertEquals("User not found", ex.getReason());
    }

    @Test
    void testWithdrawSlot_NoConfirmedBooking() {
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.withdrawSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You don't have a booking for this game", ex.getReason());
    }

    @Test
    void testWithdrawSlot_Valid() {
        slot.setStatus(GameSlotStatus.BOOKED);
        Booking booking = new Booking();
        booking.setId(10L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.of(booking));

        bookingService.withdrawSlot(1L, "jane@example.com");

        assertEquals(BookingStatus.WITHDRAWN, booking.getStatus());
        assertEquals(GameSlotStatus.AVAILABLE, slot.getStatus());
        verify(bookingRepository).save(argThat(b ->
                b.getStatus() == BookingStatus.WITHDRAWN && b.getWithdrawnAt() != null));
        verify(gameSlotRepository).save(slot);
    }

    @Test
    void testGetMyBookings_SupersededWithdrawalHidden() {
        Booking oldWithdrawn = new Booking();
        oldWithdrawn.setId(20L);
        oldWithdrawn.setSlot(slot);
        oldWithdrawn.setUser(user);
        oldWithdrawn.setStatus(BookingStatus.WITHDRAWN);
        oldWithdrawn.setCreatedAt(LocalDateTime.of(2026, 7, 1, 10, 0));

        Booking newConfirmed = new Booking();
        newConfirmed.setId(21L);
        newConfirmed.setSlot(slot);
        newConfirmed.setUser(user);
        newConfirmed.setStatus(BookingStatus.CONFIRMED);
        newConfirmed.setCreatedAt(LocalDateTime.of(2026, 7, 2, 10, 0));

        BookingResponeDTO confirmedDto = new BookingResponeDTO();
        confirmedDto.setId(21L);

        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findByUser(user)).thenReturn(List.of(oldWithdrawn, newConfirmed));
        when(bookingMapper.toResponseDTO(newConfirmed)).thenReturn(confirmedDto);

        List<BookingResponeDTO> result = bookingService.getMyBookings("jane@example.com");

        assertEquals(List.of(confirmedDto), result);
    }

    @Test
    void testBookSlot_InsufficientCredits() {
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(creditRepository.sumAmountByUser(user)).thenReturn(BigDecimal.valueOf(10));

        ResponseStatusException ex = assertThrows(ResponseStatusException.class,
                () -> bookingService.bookSlot(1L, "jane@example.com"));

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
        assertEquals("You don't have enough credits for this game", ex.getReason());
    }

    @Test
    void testBookSlot_PaidOnlineDeductsCredits() {
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(profileRepository.findByUser(user)).thenReturn(Optional.of(profile));
        when(creditRepository.sumAmountByUser(user)).thenReturn(BigDecimal.valueOf(100));
        when(gameSlotRepository.countByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(10L);
        when(gameSlotRepository.findFirstByGameAndStatus(game, GameSlotStatus.AVAILABLE)).thenReturn(Optional.of(slot));

        bookingService.bookSlot(1L, "jane@example.com");

        verify(creditRepository).save(argThat(credit ->
                credit.getUser() == user && credit.getAmount().compareTo(BigDecimal.valueOf(-20)) == 0));
    }

    @Test
    void testWithdrawSlot_RefundsWithinWindow_Hours24() {
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        game.setRefundPolicy(RefundPolicy.HOURS_24);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.of(booking));

        bookingService.withdrawSlot(1L, "jane@example.com");

        verify(creditRepository).save(argThat(credit ->
                credit.getUser() == user && credit.getAmount().compareTo(BigDecimal.valueOf(20)) == 0));
    }

    @Test
    void testWithdrawSlot_RefundsWithinWindow_Hours48() {
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        game.setRefundPolicy(RefundPolicy.HOURS_48);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.of(booking));

        bookingService.withdrawSlot(1L, "jane@example.com");

        verify(creditRepository).save(argThat(credit ->
                credit.getUser() == user && credit.getAmount().compareTo(BigDecimal.valueOf(20)) == 0));
    }

    @Test
    void testWithdrawSlot_NoRefundOutsideWindow_Hours24() {
        LocalDateTime nowPlus2Hours = LocalDateTime.now().plusHours(2);
        game.setGameDate(nowPlus2Hours.toLocalDate());
        game.setGameTime(nowPlus2Hours.toLocalTime());
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        game.setRefundPolicy(RefundPolicy.HOURS_24);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.of(booking));

        bookingService.withdrawSlot(1L, "jane@example.com");

        verify(creditRepository, never()).save(any());
    }

    @Test
    void testWithdrawSlot_NoRefundOutsideWindow_Hours48() {
        LocalDateTime nowPlus2Hours = LocalDateTime.now().plusHours(2);
        game.setGameDate(nowPlus2Hours.toLocalDate());
        game.setGameTime(nowPlus2Hours.toLocalTime());
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        game.setRefundPolicy(RefundPolicy.HOURS_48);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.of(booking));

        bookingService.withdrawSlot(1L, "jane@example.com");

        verify(creditRepository, never()).save(any());
    }

    @Test
    void testWithdrawSlot_NoRefundWhenPolicyIsNoRefund() {
        game.setPaymentType(PaymentType.PAID_ONLINE);
        game.setPrice(BigDecimal.valueOf(20));
        game.setRefundPolicy(RefundPolicy.NO_REFUND);

        Booking booking = new Booking();
        booking.setId(10L);
        booking.setSlot(slot);
        booking.setUser(user);
        booking.setStatus(BookingStatus.CONFIRMED);

        when(gameRepository.findById(1L)).thenReturn(Optional.of(game));
        when(userRepository.findByEmail("jane@example.com")).thenReturn(Optional.of(user));
        when(bookingRepository.findBySlot_GameAndUserAndStatus(game, user, BookingStatus.CONFIRMED))
                .thenReturn(Optional.of(booking));

        bookingService.withdrawSlot(1L, "jane@example.com");

        verify(creditRepository, never()).save(any());
    }
}
