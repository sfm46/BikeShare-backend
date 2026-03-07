package com.bikeshare.dto;

import com.bikeshare.models.Ride;
import lombok.Data;
import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class RideDto {
    private Long id;
    private Long posterId;
    private String posterName;
    private String startCity;
    private String endCity;
    private LocalDate date;
    private LocalTime time;
    private Integer seatsAvailable;
    private Double price;

    public static RideDto fromEntity(Ride ride) {
        RideDto dto = new RideDto();
        dto.setId(ride.getId());
        dto.setPosterId(ride.getPoster().getId());
        dto.setPosterName(ride.getPoster().getName());
        dto.setStartCity(ride.getStartCity());
        dto.setEndCity(ride.getEndCity());
        dto.setDate(ride.getDate());
        dto.setTime(ride.getTime());
        dto.setSeatsAvailable(ride.getSeatsAvailable());
        dto.setPrice(ride.getPrice());
        return dto;
    }
}
