package com.cibf.stallservice.stall.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import java.util.UUID;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "stalls")
public class Stall {

    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    @Column(name = "id", updatable = false, nullable = false)
    private UUID stallId;  // Unique identifier (UUID)

    @Column(nullable = false, unique = true)
    private String label; // e.g., "A1", "B2"

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StallSize stallSize; // SMALL, MEDIUM, LARGE

    @Column(nullable = false)
    private double price; // e.g., 15000, 25000, etc.

    @Column(nullable = false)
    private int availability; // true or false

    @Column(nullable = false)
    private String area; // e.g., "Hall A", "Outdoor"

}


//import jakarta.persistence.Entity;
//import jakarta.persistence.EnumType;
//import jakarta.persistence.Enumerated;
//import jakarta.persistence.Id;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//@Entity
//@NoArgsConstructor
//@AllArgsConstructor
//@Data
//public class Stall {
//    @Id
//    private int stallId;
//    private String stallName;
//    private String stallDescription;
//    private String location;
//    private int availability;
//
//    @Enumerated(EnumType.STRING)
//    private StallSize stallSize;
//}
