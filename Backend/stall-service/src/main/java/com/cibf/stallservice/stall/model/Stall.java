package com.cibf.stallservice.stall.model;


import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Data
public class Stall {
    @Id
    private int stallId;
    private String stallName;
    private String stallDescription;
    private String location;
    private int availability;

    @Enumerated(EnumType.STRING)
    private StallSize stallSize;
}
