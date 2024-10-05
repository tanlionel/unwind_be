package com.capstone.unwind.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "cancelation_policy")
public class CancelationPolicy {
    @Id
    @Column(name = "cancelation_policy_id", nullable = false)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "refund_rate")
    private Float refundRate;

    @Column(name = "duration_before", length = 45)
    private String durationBefore;

    @Column(name = "description", length = 45)
    private String description;

    @Column(name = "is_active")
    private Boolean isActive;

}