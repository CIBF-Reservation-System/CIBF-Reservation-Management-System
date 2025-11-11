package com.cibf.stallservice.stall.dto;


import com.cibf.stallservice.stall.model.StallSize;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StallDTO {

    private UUID stallId;  // Unique identifier (UUID)
    private String label; // e.g., "A1", "B2"
    @Enumerated(EnumType.STRING)
    private StallSize stallSize; // SMALL, MEDIUM, LARGE
    private double price; // e.g., 15000, 25000, etc.
    private int availability; // true or false
    private String area; // e.g., "Hall A", "Outdoor"
    // "Hall A", "Outdoor"
}


//@Data
//@NoArgsConstructor
//@AllArgsConstructor
//public class StallDTO {
//    private int stallId;
//    private String stallName;
//    private String stallDescription;
//    private String location;
//    private int availability;
//
//    @Enumerated(EnumType.STRING)
//    private StallSize stallSize;
//}
