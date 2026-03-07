package com.bikeshare.dto;

import com.bikeshare.models.RideRequest;
import lombok.Data;

@Data
public class RideRequestDto {
    private Long id;
    private Long rideId;
    private String rideStartCity;
    private String rideEndCity;
    private Long requesterId;
    private String requesterName;
    private String requesterPhone;
    private String status;

    public static RideRequestDto fromEntity(RideRequest request) {
        RideRequestDto dto = new RideRequestDto();
        dto.setId(request.getId());
        dto.setRideId(request.getRide().getId());
        dto.setRideStartCity(request.getRide().getStartCity());
        dto.setRideEndCity(request.getRide().getEndCity());
        dto.setRequesterId(request.getRequester().getId());
        dto.setRequesterName(request.getRequester().getName());
        dto.setRequesterPhone(request.getRequester().getPhone());
        dto.setStatus(request.getStatus().name());
        return dto;
    }
}
