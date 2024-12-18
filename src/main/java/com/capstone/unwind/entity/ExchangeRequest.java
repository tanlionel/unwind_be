package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.*;

import java.sql.Timestamp;
import java.time.Instant;
import java.time.LocalDate;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "exchange_request")
public class ExchangeRequest {
    @Id
    @Column(name = "exchange_request_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "timeshare_id")
    private Timeshare timeshare;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_info_id")
    private RoomInfo roomInfo;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Customer owner;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @Column(name = "status", length = 45)
    private String status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "exchange_posting_id")
    private ExchangePosting exchangePosting;

    @Column(name = "note", length = 100)
    private String note;

    @Column(name = "created_date")
    private Timestamp createdDate;

    @Column(name = "updated_date")
    private Timestamp updatedDate;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "price_valuation")
    private Float priceValuation;

    @Column(name = "exchange_package_id")
    private Integer exchangePackageId;

    @PrePersist
    protected void onCreate() {
        this.createdDate = Timestamp.from(Instant.now());
        this.updatedDate = Timestamp.from(Instant.now());
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedDate = Timestamp.from(Instant.now());
    }
}