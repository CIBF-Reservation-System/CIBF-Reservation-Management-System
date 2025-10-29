package com.cibf.stallservice.stall.controller;


import com.cibf.stallservice.stall.dto.StallDTO;
import com.cibf.stallservice.stall.service.StallService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@CrossOrigin
@RequestMapping(value = "/api/v1/")
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
    public StallDTO getStallsById(@PathVariable Integer stallId) {
        return stallService.getStallsById(stallId);
    }

    @PostMapping("/addstall")
    public StallDTO saveStall(@RequestBody StallDTO stallId) {
        return stallService.saveStall(stallId);
    }

    @PutMapping("/updatestall/{stallId}")
    public StallDTO updateStall(@RequestBody StallDTO stallId) {
        return stallService.updateStall(stallId);
    }

    @DeleteMapping("/deletestall/{stallId}")
    public String deleteStalls(@PathVariable Integer stallId) {
        return stallService.deleteStalls(stallId);
    }
}
