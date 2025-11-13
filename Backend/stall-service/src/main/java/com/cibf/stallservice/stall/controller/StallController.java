package com.cibf.stallservice.stall.controller;


import com.cibf.stallservice.stall.dto.StallDTO;
import com.cibf.stallservice.stall.service.StallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

@RestController
//@CrossOrigin
@RequestMapping(value = "/api/v1/stall")
public class StallController {

    @Autowired
    private StallService stallService;

    @GetMapping("/")
    public String home() {
        return "Stall Service Microservice is running!";
    }

    @GetMapping("/getstalls")
    public List<StallDTO> getStalls() {
        return stallService.getAllStalls();
    }

    @GetMapping("/stall/{stallId}")
//    public StallDTO getStallsById(@PathVariable Integer stallId) {
//
//        return stallService.getStallsById(stallId);
//
//    }
    public ResponseEntity<?> getStallById(@PathVariable UUID stallId) {
        StallDTO stallDTO = stallService.getStallsById(stallId);

        if (stallDTO == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(Map.of("error", "Stall with stallId " + stallId + " not found"));
        }

        return ResponseEntity.ok(Map.of("message", "Stall found", "stall", stallDTO));
    }

    @PostMapping("/addstall")
    public StallDTO saveStall(@RequestBody StallDTO stallId) {
        return stallService.saveStall(stallId);
    }

    @PutMapping("/updatestall/{stallId}")
    public ResponseEntity<?> updateStall(@PathVariable UUID stallId, @RequestBody StallDTO stallDTO) {
         try {
             StallDTO updatedStall = stallService.updateStall(stallId, stallDTO);
             Map<String, Object> response = new HashMap<>();
             response.put("message", "Stall updated successfully");
             response.put("stall", updatedStall);
             return ResponseEntity.ok(response);
         } catch (RuntimeException e) {
             Map<String, Object> error = new HashMap<>();
             error.put("error", e.getMessage());
             return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
         }
    }

    @DeleteMapping("/deletestall/{stallId}")
    public String deleteStalls(@PathVariable UUID stallId) {
        return stallService.deleteStalls(stallId);
    }

    @GetMapping("/available")
    public List<StallDTO> getAvailableStalls() {
        return stallService.getAvailableStalls();
    }

    @GetMapping("/reserved")
    public List<StallDTO> getReservedStalls() {
        return stallService.getReservedStalls();
    }

    @PutMapping("/updateavailability/{stallId}")
    public String updateAvailability(@PathVariable UUID stallId) {
        return stallService.updateAvailability(stallId);
    }

}
