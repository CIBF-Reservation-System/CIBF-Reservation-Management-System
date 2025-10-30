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
        stallRepo.save(modelMapper.map(stallDTO, Stall.class));
        return stallDTO;
    }

    public StallDTO updateStall(StallDTO stallDTO) {
        stallRepo.save(modelMapper.map(stallDTO, Stall.class));
        return stallDTO;
    }

    public String deleteStalls(Integer stallId) {
        stallRepo.deleteById(stallId);
        return "Stall deleted";
    }

    public StallDTO getStallsById(Integer stallId) {
        Stall stall = stallRepo.getStallById(stallId);
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

}
