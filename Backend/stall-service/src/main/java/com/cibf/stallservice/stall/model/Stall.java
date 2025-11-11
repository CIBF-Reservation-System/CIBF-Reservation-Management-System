package com.cibf.stallservice.stall.model;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
@Table(name = "stalls")
public class Stall {
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID stallId;

    @Column(nullable = false, unique = true)
    private String label;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StallSize stallSize;

    @Column(nullable = false)
    private double price;

    @Column(nullable = false)
    private int availability;

    @Column(nullable = false)
    private String area;
}
