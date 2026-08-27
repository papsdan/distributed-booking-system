package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Game;
import com.dpapie01.distributed_booking_system.enums.GameGenderOption;
import com.dpapie01.distributed_booking_system.enums.GameType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * This interface is the repository for Game entities.
 */
@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    /**
     * Searches for upcoming and non-cancelled games matching the optional filters.
     * A null filter value is treated as "match any".
     * @param city city to filter by
     * @param area area to filter by
     * @param gameType game type to filter by
     * @param genderOption gender option to filter by
     * @param gameDate date to filter by
     * @param maxPrice maximum price to filter by
     * @param openSlotsOnly if true, only include games with at least one available slot
     * @return matching games ordered by date and time
     */
    @Query("SELECT g FROM Game g WHERE " +
            "g.pitch.location.city = COALESCE(:city, g.pitch.location.city) AND " +
            "g.pitch.location.area = COALESCE(:area, g.pitch.location.area) AND " +
            "g.gameType = COALESCE(:gameType, g.gameType) AND " +
            "g.genderOption = COALESCE(:genderOption, g.genderOption) AND " +
            "g.gameDate = COALESCE(:gameDate, g.gameDate) AND " +
            "g.price <= COALESCE(:maxPrice, g.price) AND " +
            "g.status <> com.dpapie01.distributed_booking_system.enums.GameStatus.CANCELLED AND " +
            "(g.gameDate > CURRENT_DATE OR (g.gameDate = CURRENT_DATE AND g.gameTime >= CURRENT_TIME)) AND " +
            "(:openSlotsOnly = false OR (SELECT COUNT(gs) FROM GameSlot gs " +
            "WHERE gs.game = g AND gs.status = com.dpapie01.distributed_booking_system.enums.GameSlotStatus.AVAILABLE) > 0) " +
            "ORDER BY g.gameDate, g.gameTime")
    List<Game> filterGames(@Param("city") String city,
                            @Param("area") String area,
                            @Param("gameType") GameType gameType,
                            @Param("genderOption") GameGenderOption genderOption,
                            @Param("gameDate") LocalDate gameDate,
                            @Param("maxPrice") BigDecimal maxPrice,
                            @Param("openSlotsOnly") boolean openSlotsOnly);
}
