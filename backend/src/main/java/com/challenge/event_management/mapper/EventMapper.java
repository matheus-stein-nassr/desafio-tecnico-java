package com.challenge.event_management.mapper;

import com.challenge.event_management.dto.EventRequestDTO;
import com.challenge.event_management.dto.EventResponseDTO;
import com.challenge.event_management.entity.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    public Event toEntity(EventRequestDTO dto) {
        Event event = new Event();
        event.setTitle(dto.getTitle());
        event.setDescription(dto.getDescription());
        event.setEventDateTime(dto.getEventDateTime());
        event.setLocation(dto.getLocation());
        return event;
    }

    public void updateEntityFromDto(EventRequestDTO dto, Event entity) {
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setEventDateTime(dto.getEventDateTime());
        entity.setLocation(dto.getLocation());
    }

    public EventResponseDTO toResponseDTO(Event entity) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.setId(entity.getId());
        dto.setTitle(entity.getTitle());
        dto.setDescription(entity.getDescription());
        dto.setEventDateTime(entity.getEventDateTime());
        dto.setLocation(entity.getLocation());
        dto.setCreatedAt(entity.getCreatedAt());
        dto.setUpdatedAt(entity.getUpdatedAt());
        return dto;
    }
}