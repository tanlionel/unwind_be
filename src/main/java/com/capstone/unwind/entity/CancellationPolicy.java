package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cancellation_policy")
public class CancellationPolicy {
    @Id
    @Column(name = "cancellation_policy_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "refund_rate")
    private Integer refundRate;

    @Column(name = "duration_before")
    private Integer durationBefore;

    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

}