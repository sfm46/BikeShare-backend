package com.bikeshare.controllers;

import com.bikeshare.dto.RideRequestDto;
import com.bikeshare.models.User;
import com.bikeshare.services.RideRequestService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/requests")
@RequiredArgsConstructor
public class RideRequestController {

    private final RideRequestService rideRequestService;

    @PostMapping("/{rideId}")
    public ResponseEntity<RideRequestDto> requestRide(
            @AuthenticationPrincipal User user,
            @PathVariable Long rideId
    ) {
        return ResponseEntity.ok(rideRequestService.requestRide(rideId, user.getId()));
    }

    @PostMapping("/{requestId}/accept")
    public ResponseEntity<RideRequestDto> acceptRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(rideRequestService.acceptRequest(requestId, user.getId()));
    }

    @PostMapping("/{requestId}/reject")
    public ResponseEntity<RideRequestDto> rejectRequest(
            @AuthenticationPrincipal User user,
            @PathVariable Long requestId
    ) {
        return ResponseEntity.ok(rideRequestService.rejectRequest(requestId, user.getId()));
    }

    @GetMapping("/ride/{rideId}")
    public ResponseEntity<List<RideRequestDto>> getRequestsForRide(
            @AuthenticationPrincipal User user,
            @PathVariable Long rideId
    ) {
        return ResponseEntity.ok(rideRequestService.getRequestsForRide(rideId, user.getId()));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RideRequestDto>> getMyRequests(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(rideRequestService.getMyRequests(user.getId()));
    }
}
