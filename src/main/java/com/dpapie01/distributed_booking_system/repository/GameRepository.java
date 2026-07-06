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

@Repository
public interface GameRepository extends JpaRepository<Game, Long> {

    @Query("SELECT g FROM Game g WHERE " +
            "g.pitch.location.city = COALESCE(:city, g.pitch.location.city) AND " +
            "g.pitch.location.area = COALESCE(:area, g.pitch.location.area) AND " +
            "g.gameType = COALESCE(:gameType, g.gameType) AND " +
            "g.genderOption = COALESCE(:genderOption, g.genderOption) AND " +
            "g.gameDate = COALESCE(:gameDate, g.gameDate) AND " +
            "g.price <= COALESCE(:maxPrice, g.price) " +
            "ORDER BY g.gameDate, g.gameTime")
    List<Game> filterGames(@Param("city") String city,
                            @Param("area") String area,
                            @Param("gameType") GameType gameType,
                            @Param("genderOption") GameGenderOption genderOption,
                            @Param("gameDate") LocalDate gameDate,
                            @Param("maxPrice") BigDecimal maxPrice);
}
