package com.cibf.stallservice.stall.dto;


import com.cibf.stallservice.stall.model.StallSize;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StallDTO {
    private int id;
    private int stallId;
    private String stallName;
    private String stallDescription;
    private String location;
    private int availability;

    @Enumerated(EnumType.STRING)
    private StallSize stallSize;
}
