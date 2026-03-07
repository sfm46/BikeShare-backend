package com.bikeshare.controllers;

import com.bikeshare.dto.PostRideRequest;
import com.bikeshare.dto.RideDto;
import com.bikeshare.models.User;
import com.bikeshare.services.RideService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/rides")
@RequiredArgsConstructor
public class RideController {

    private final RideService rideService;

    @PostMapping
    public ResponseEntity<RideDto> postRide(
            @AuthenticationPrincipal User user,
            @Valid @RequestBody PostRideRequest request
    ) {
        return ResponseEntity.ok(rideService.postRide(user.getId(), request));
    }

    @GetMapping("/search")
    public ResponseEntity<List<RideDto>> searchRides(
            @RequestParam String startCity,
            @RequestParam String endCity,
            @RequestParam String date
    ) {
        return ResponseEntity.ok(rideService.searchRides(startCity, endCity, LocalDate.parse(date)));
    }

    @GetMapping("/my")
    public ResponseEntity<List<RideDto>> getMyRides(
            @AuthenticationPrincipal User user
    ) {
        return ResponseEntity.ok(rideService.getMyRides(user.getId()));
    }
}
