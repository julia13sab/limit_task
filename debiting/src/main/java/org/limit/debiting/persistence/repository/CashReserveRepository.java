package org.limit.debiting.persistence.repository;

import org.limit.debiting.persistence.entity.CashReserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.OffsetDateTime;
import java.util.List;

public interface CashReserveRepository extends JpaRepository<CashReserve, Long> {
    List<CashReserve> findAllByDateGreaterThanAndDateLessThanAndCancelIsFalse(OffsetDateTime startDate, OffsetDateTime endDate);
}
