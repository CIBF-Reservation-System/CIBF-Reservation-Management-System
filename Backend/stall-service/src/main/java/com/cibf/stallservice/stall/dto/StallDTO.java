package com.cibf.stallservice.stall.dto;


import com.cibf.stallservice.stall.model.StallSize;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String area;
}
