package com.bikeshare.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalTime;

@Data
public class PostRideRequest {
    @NotBlank
    private String startCity;

    @NotBlank
    private String endCity;

    @NotNull
    private LocalDate date;

    @NotNull
    private LocalTime time;

    @NotNull
    private Double price;

    private Integer seatsAvailable = 1;
}
