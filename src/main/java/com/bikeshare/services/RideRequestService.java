package com.bikeshare.services;

import com.bikeshare.dto.RideRequestDto;
import com.bikeshare.models.Ride;
import com.bikeshare.models.RideRequest;
import com.bikeshare.models.User;
import com.bikeshare.repositories.RideRepository;
import com.bikeshare.repositories.RideRequestRepository;
import com.bikeshare.repositories.UserRepository;
import com.bikeshare.services.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class RideRequestService {

    private final RideRequestRepository rideRequestRepository;
    private final RideRepository rideRepository;
    private final UserRepository userRepository;
    private final NotificationService notificationService;

    @Transactional
    public RideRequestDto requestRide(Long rideId, Long requesterId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
                
        User requester = userRepository.findById(requesterId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!requester.getIsVerified()) {
            throw new RuntimeException("User must be verified to request a ride.");
        }

        if (ride.getSeatsAvailable() <= 0) {
            throw new RuntimeException("No seats available on this ride.");
        }

        if (ride.getPoster().getId().equals(requesterId)) {
            throw new RuntimeException("You cannot request your own ride.");
        }
        
        if (rideRequestRepository.findByRideIdAndRequesterId(rideId, requesterId).isPresent()) {
             throw new RuntimeException("You have already requested this ride.");
        }

        RideRequest request = RideRequest.builder()
                .ride(ride)
                .requester(requester)
                .status(RideRequest.RequestStatus.PENDING)
                .build();

        RideRequest savedRequest = rideRequestRepository.save(request);
        
        // Notify the ride poster
        if (ride.getPoster().getFcmToken() != null) {
            notificationService.sendPushNotification(
                ride.getPoster().getFcmToken(),
                "New Ride Request",
                requester.getName() + " has requested to join your ride to " + ride.getEndCity()
            );
        }
        
        return RideRequestDto.fromEntity(savedRequest);
    }

    @Transactional
    public RideRequestDto acceptRequest(Long requestId, Long posterId) {
        RideRequest request = rideRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
                
        Ride ride = request.getRide();

        if (!ride.getPoster().getId().equals(posterId)) {
            throw new RuntimeException("Unauthorized to accept this request");
        }

        if (request.getStatus() != RideRequest.RequestStatus.PENDING) {
            throw new RuntimeException("Request is already " + request.getStatus());
        }

        if (ride.getSeatsAvailable() <= 0) {
            throw new RuntimeException("No more seats available");
        }

        request.setStatus(RideRequest.RequestStatus.ACCEPTED);
        ride.setSeatsAvailable(ride.getSeatsAvailable() - 1);
        
        rideRepository.save(ride);
        RideRequest savedRequest = rideRequestRepository.save(request);
        
        // Notify the requester
        if (savedRequest.getRequester().getFcmToken() != null) {
            notificationService.sendPushNotification(
                savedRequest.getRequester().getFcmToken(),
                "Ride Request Accepted",
                "Your request for the ride to " + ride.getEndCity() + " has been accepted!"
            );
        }
        
        return RideRequestDto.fromEntity(savedRequest);
    }

    public RideRequestDto rejectRequest(Long requestId, Long posterId) {
        RideRequest request = rideRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
                
        if (!request.getRide().getPoster().getId().equals(posterId)) {
            throw new RuntimeException("Unauthorized to reject this request");
        }

        request.setStatus(RideRequest.RequestStatus.REJECTED);
        RideRequest savedRequest = rideRequestRepository.save(request);
        
        // Notify the requester
        if (savedRequest.getRequester().getFcmToken() != null) {
            notificationService.sendPushNotification(
                savedRequest.getRequester().getFcmToken(),
                "Ride Request Update",
                "Your request for the ride to " + request.getRide().getEndCity() + " was not accepted."
            );
        }
        
        return RideRequestDto.fromEntity(savedRequest);
    }

    public List<RideRequestDto> getRequestsForRide(Long rideId, Long posterId) {
        Ride ride = rideRepository.findById(rideId)
                .orElseThrow(() -> new RuntimeException("Ride not found"));
                
        if (!ride.getPoster().getId().equals(posterId)) {
             throw new RuntimeException("Unauthorized to view requests for this ride");
        }

        return rideRequestRepository.findByRideId(rideId).stream()
                .map(RideRequestDto::fromEntity)
                .collect(Collectors.toList());
    }

    public List<RideRequestDto> getMyRequests(Long requesterId) {
        return rideRequestRepository.findByRequesterId(requesterId).stream()
                .map(RideRequestDto::fromEntity)
                .collect(Collectors.toList());
    }
}
