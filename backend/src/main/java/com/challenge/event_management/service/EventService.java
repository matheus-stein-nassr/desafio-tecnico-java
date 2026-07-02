package com.challenge.event_management.service;

import com.challenge.event_management.dto.EventRequestDTO;
import com.challenge.event_management.dto.EventResponseDTO;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface EventService {
    Page<EventResponseDTO> findAll(Pageable pageable);
    EventResponseDTO findById(Long id);
    EventResponseDTO create(EventRequestDTO dto);
    EventResponseDTO update(Long id, EventRequestDTO dto);
    void softDelete(Long id);
}
