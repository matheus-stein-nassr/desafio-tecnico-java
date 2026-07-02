package com.challenge.event_management.service.impl;


import com.challenge.event_management.dto.EventRequestDTO;
import com.challenge.event_management.dto.EventResponseDTO;
import com.challenge.event_management.entity.Event;
import com.challenge.event_management.exception.ResourceNotFoundException;
import com.challenge.event_management.mapper.EventMapper;
import com.challenge.event_management.repository.EventRepository;
import com.challenge.event_management.service.EventService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class EventServiceImpl implements EventService {

    private final EventRepository eventRepository;
    private final EventMapper eventMapper;

    @Override
    @Transactional(readOnly = true)
    public Page<EventResponseDTO> findAll(Pageable pageable) {
        return eventRepository.findAllByDeletedFalse(pageable)
                .map(eventMapper::toResponseDTO);
    }

    @Override
    @Transactional(readOnly = true)
    public EventResponseDTO findById(Long id) {
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com id: " + id));
        return eventMapper.toResponseDTO(event);
    }

    @Override
    public EventResponseDTO create(EventRequestDTO dto) {
        Event event = eventMapper.toEntity(dto);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    @Override
    public EventResponseDTO update(Long id, EventRequestDTO dto) {
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com id: " + id));
        eventMapper.updateEntityFromDto(dto, event);
        return eventMapper.toResponseDTO(eventRepository.save(event));
    }

    @Override
    public void softDelete(Long id) {
        Event event = eventRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new ResourceNotFoundException("Evento não encontrado com id: " + id));
        event.setDeleted(true);
        eventRepository.save(event);
    }
}