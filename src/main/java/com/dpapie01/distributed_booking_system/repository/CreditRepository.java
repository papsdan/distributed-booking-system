package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Credit;
import com.dpapie01.distributed_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Credit c WHERE c.user = :user")
    BigDecimal sumAmountByUser(@Param("user") User user);
}
