package com.bikeshare.repositories;

import com.bikeshare.models.Ride;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface RideRepository extends JpaRepository<Ride, Long> {
    List<Ride> findByStartCityAndEndCityAndDate(String startCity, String endCity, LocalDate date);
    List<Ride> findByPosterId(Long posterId);
}
