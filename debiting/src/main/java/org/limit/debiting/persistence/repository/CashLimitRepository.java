package org.limit.debiting.persistence.repository;

import org.limit.debiting.persistence.entity.CashLimit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;

public interface CashLimitRepository extends JpaRepository<CashLimit, Long> {

    @Modifying
    @Query("UPDATE CashLimit c SET c.limitValue = :limitValue")
    void updateLimits(BigDecimal limitValue);
}
