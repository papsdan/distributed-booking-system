package com.dpapie01.distributed_booking_system.repository;

import com.dpapie01.distributed_booking_system.entity.Credit;
import com.dpapie01.distributed_booking_system.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

/**
 * This interface is the repository for Credit entities.
 */
@Repository
public interface CreditRepository extends JpaRepository<Credit, Long> {

    /**
     * Sums all credit ledger entries for a user to get their current balance.
     * @param user the user
     * @return the sum of the user's credit amounts, or zero if they have none
     */
    @Query("SELECT COALESCE(SUM(c.amount), 0) FROM Credit c WHERE c.user = :user")
    BigDecimal sumAmountByUser(@Param("user") User user);
}
