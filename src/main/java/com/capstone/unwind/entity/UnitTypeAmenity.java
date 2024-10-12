package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "unit_type_amenities")
public class UnitTypeAmenity {
    @Id
    @Column(name = "unit_type_amenities_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "type")
    private String type;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "unit_type_id")
    private UnitType unitType;

}