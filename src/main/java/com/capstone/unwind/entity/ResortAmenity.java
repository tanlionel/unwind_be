package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "resort_amenities")
public class ResortAmenity {
    @Id
    @Column(name = "resort_amenities_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", length = 45)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "resort_id")
    private Resort resort;

    @Column(name = "is_active")
    private Boolean isActive;

    @Column(name = "type", length = 45)
    private String type;

}