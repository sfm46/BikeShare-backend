package com.bikeshare.repositories;

import com.bikeshare.models.RideRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RideRequestRepository extends JpaRepository<RideRequest, Long> {
    List<RideRequest> findByRideId(Long rideId);
    List<RideRequest> findByRequesterId(Long requesterId);
    Optional<RideRequest> findByRideIdAndRequesterId(Long rideId, Long requesterId);
}
