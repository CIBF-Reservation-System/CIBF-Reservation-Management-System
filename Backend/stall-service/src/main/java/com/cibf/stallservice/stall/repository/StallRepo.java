package com.cibf.stallservice.stall.repository;


import com.cibf.stallservice.stall.model.Stall;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StallRepo extends JpaRepository<Stall,Integer> {
    @Query(value = "SELECT * FROM stall WHERE stall_id = ?1", nativeQuery = true)
    Stall getStallById(Integer stallId);

    @Query(value = "SELECT * FROM stall WHERE availability = 1", nativeQuery = true)
    List<Stall> getAvailableStalls();

    @Query(value = "SELECT * FROM stall WHERE availability = 0", nativeQuery = true)
    List<Stall> getReservedStalls();

}
