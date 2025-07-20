package org.limit.debiting.persistence.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.time.OffsetDateTime;

@Entity
@Table(name = "cash_reserve")
@EqualsAndHashCode
@RequiredArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class CashReserve {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private Long userId;

    @Column(name = "draft")
    private Boolean cancel;

    @Column(name = "amount")
    private BigDecimal amount;

    @Column(name = "date")
    private OffsetDateTime date;
}
