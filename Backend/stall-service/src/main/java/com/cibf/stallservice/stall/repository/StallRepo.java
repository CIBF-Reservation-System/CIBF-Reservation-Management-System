package com.cibf.stallservice.stall.repository;


import com.cibf.stallservice.stall.model.Stall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface StallRepo extends JpaRepository<Stall,UUID> {
    @Query(value = "SELECT * FROM stalls WHERE id = ?1", nativeQuery = true)
  // @Query(value = "SELECT * FROM stalls WHERE id = ?1", nativeQuery = true)
    Stall getStallById(UUID stallId);

    @Query(value = "SELECT * FROM stalls WHERE availability = 1", nativeQuery = true)
    List<Stall> getAvailableStalls();

    @Query(value = "SELECT * FROM stalls WHERE availability = 0", nativeQuery = true)
    List<Stall> getReservedStalls();

}
