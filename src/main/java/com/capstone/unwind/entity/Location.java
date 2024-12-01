package com.capstone.unwind.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "location")
public class Location {
    @Id
    @Column(name = "location_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer locationId;

    @Column(name = "name", length = 100)
    private String name;

    @Column(name = "display_name", length = 200)
    private String displayName;

    @Column(name = "latitude", length = 50)
    private String latitude;

    @Column(name = "longitude", length = 50)
    private String longitude;

    @Column(name = "country", length = 100)
    private String country;

    @Column(name = "place_id", length = 100)
    private String placeId;

    @OneToOne(mappedBy = "location", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Resort resort;

    @OneToOne(mappedBy = "location", fetch = FetchType.LAZY)
    private TimeshareCompany timeshareCompany;
}
