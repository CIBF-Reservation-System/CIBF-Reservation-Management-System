package com.cibf.reservationservice.reservation.Service;

import jakarta.transaction.Transactional;
import com.cibf.reservationservice.reservation.Entity.Reservation;
import com.cibf.reservationservice.reservation.Repository.ReservationRepo;
import com.cibf.reservationservice.reservation.DTO.ReservationDTO;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Transactional
public class ReservationService {
    @Autowired
    private ReservationRepo reservationRepo;

    @Autowired
    private ModelMapper modelMapper;

    public List<ReservationDTO> getAllReservations() {
        List<Reservation>reservationList = reservationRepo.findAll();
        return modelMapper.map(reservationList, new TypeToken<List<ReservationDTO>>(){}.getType());
    }

    public ReservationDTO saveReservation(ReservationDTO reservationDTO) {
        reservationRepo.save(modelMapper.map(reservationDTO, Reservation.class));
        return reservationDTO;
    }

    public String deleteReservation(Integer reserveId) {
        reservationRepo.deleteById(reserveId);
        return "Reservation deleted";
    }

    public ReservationDTO getReservationById(Integer reserveId) {
        Reservation reservation = reservationRepo.getReservationById(reserveId);
        return modelMapper.map(reservation, ReservationDTO.class);
    }


}
