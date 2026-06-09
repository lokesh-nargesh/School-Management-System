package com.school.management.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "vehicles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "vehicle_id")
    private Long vehicleId;

    @Column(name = "register_no", unique = true, nullable = false)
    private String registerNo;

    @Column(name = "driver_name", nullable = false)
    private String driverName;

    @Column(name = "driver_phone", nullable = false)
    private String driverPhone;

    @ManyToOne
    @JoinColumn(name = "route_id")
    private Route route;
}
