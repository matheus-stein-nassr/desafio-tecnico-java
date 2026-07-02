package com.challenge.event_management.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class EventResponseDTO {
    private Long id;
    private String title;
    private String description;
    private LocalDateTime eventDateTime;
    private String location;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
