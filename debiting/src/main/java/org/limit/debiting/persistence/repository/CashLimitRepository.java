package org.limit.debiting.persistence.repository;

import org.limit.debiting.persistence.entity.CashLimit;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CashLimitRepository extends JpaRepository<CashLimit, Long> {
}
