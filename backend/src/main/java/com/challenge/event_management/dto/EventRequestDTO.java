package com.challenge.event_management.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventRequestDTO {
    @NotBlank
    @Size(max = 100)
    private String title;

    @Size(max = 1000)
    private String description;

    @NotNull
    @FutureOrPresent
    private LocalDateTime eventDateTime;

    @NotBlank @Size(max = 200)
    private String location;
}
