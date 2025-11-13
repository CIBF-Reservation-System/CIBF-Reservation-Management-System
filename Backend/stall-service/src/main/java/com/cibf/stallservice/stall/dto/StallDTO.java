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
    private UUID stallId; 
    private String label; 
    @Enumerated(EnumType.STRING)
    private StallSize stallSize;
    private double price; 
    private int availability;
    private String area;
}



