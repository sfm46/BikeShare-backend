package com.bikeshare.services;

import com.bikeshare.dto.PostRideRequest;
import com.bikeshare.dto.RideDto;
import com.bikeshare.models.Ride;
import com.bikeshare.models.User;
import com.bikeshare.repositories.RideRepository;
import com.bikeshare.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideService {

    private final RideRepository rideRepository;
    private final UserRepository userRepository;

    public RideDto postRide(Long userId, PostRideRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!user.getIsVerified()) {
            throw new RuntimeException("User must be verified to post a ride.");
        }

        Ride ride = Ride.builder()
                .poster(user)
                .startCity(request.getStartCity())
                .endCity(request.getEndCity())
                .date(request.getDate())
                .time(request.getTime())
                .price(request.getPrice())
                .seatsAvailable(request.getSeatsAvailable() != null ? request.getSeatsAvailable() : 1)
                .build();

        Ride savedRide = rideRepository.save(ride);
        return RideDto.fromEntity(savedRide);
    }

    public List<RideDto> searchRides(String startCity, String endCity, LocalDate date) {
        List<Ride> rides = rideRepository.findByStartCityAndEndCityAndDate(startCity, endCity, date);
        return rides.stream()
                .filter(ride -> ride.getSeatsAvailable() > 0)
                .map(RideDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RideDto> getMyRides(Long userId) {
        List<Ride> rides = rideRepository.findByPosterId(userId);
        return rides.stream()
                .map(RideDto::fromEntity)
                .collect(Collectors.toList());
    }
}
