package com.cibf.stallservice.stall.service;

import com.cibf.stallservice.stall.dto.StallDTO;
import com.cibf.stallservice.stall.model.Stall;
import com.cibf.stallservice.stall.repository.StallRepo;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
public class StallService {
    @Autowired
    private StallRepo stallRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<StallDTO> getAllStalls() {
        List<Stall>stallList = stallRepo.findAll();
        return modelMapper.map(stallList, new TypeToken<List<StallDTO>>(){}.getType());
    }

    public StallDTO saveStall(StallDTO stallDTO) {
        Stall saveStall= stallRepo.save(modelMapper.map(stallDTO, Stall.class));
        return stallDTO;
    }

    public String deleteStalls(UUID stallId) {
        Stall existingStall = stallRepo.getStallById(stallId);
        if (existingStall == null) {
            return "No stall id " + stallId + "  to delete";
        }

        stallRepo.deleteById(stallId);
        return "Stall deleted successfully";
    }

    public StallDTO getStallsById(UUID stallId) {
        Stall stall = stallRepo.getStallById(stallId);

        if (stall == null) {
            return null;
        }
        return modelMapper.map(stall, StallDTO.class);
    }
    public List<StallDTO> getAvailableStalls() {
        List<Stall> availableStalls = stallRepo.getAvailableStalls();
        return modelMapper.map(availableStalls, new TypeToken<List<StallDTO>>(){}.getType());
    }

    public List<StallDTO> getReservedStalls() {
        List<Stall> reservedStalls = stallRepo.getReservedStalls();
        return modelMapper.map(reservedStalls, new TypeToken<List<StallDTO>>(){}.getType());
    }

    public StallDTO updateStall(UUID stallId, StallDTO stallDTO) {
        Stall existingStall = stallRepo.getStallById(stallId);

        if (existingStall == null) {
            throw new RuntimeException("Stall with stallId " + stallId + " not found");
        }


        existingStall.setLabel(stallDTO.getLabel());
        existingStall.setPrice(stallDTO.getPrice());
        existingStall.setArea(stallDTO.getArea());
        existingStall.setStallSize(stallDTO.getStallSize());

        stallRepo.save(existingStall);
        return modelMapper.map(existingStall, StallDTO.class);
    }

    public String updateAvailability(UUID stallId) {
        Stall existingStall = stallRepo.getStallById(stallId);

        if (existingStall == null) {
            return "No stall found with ID ";
        }

        if (existingStall.getAvailability() == 0) {
            return "Stall is not available";
        } else if (existingStall.getAvailability() == 1) {
            existingStall.setAvailability(0); // set to unavailable
            stallRepo.save(existingStall);
            return "Stall availability updated to 0 (unavailable)";
        }

        return "Invalid availability status for stall ID ";
    }

    public String cancelBooking(UUID stallId) {
        Stall existingStall = stallRepo.getStallById(stallId);

        if (existingStall == null) {
            return "No stall found with ID ";
        }

        existingStall.setAvailability(1);
        stallRepo.save(existingStall);

        return "Stall is available";

    }


}
