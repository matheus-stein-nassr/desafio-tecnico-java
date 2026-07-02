package com.challenge.event_management.service;

import com.challenge.event_management.dto.EventRequestDTO;
import com.challenge.event_management.dto.EventResponseDTO;
import com.challenge.event_management.entity.Event;
import com.challenge.event_management.exception.ResourceNotFoundException;
import com.challenge.event_management.mapper.EventMapper;
import com.challenge.event_management.repository.EventRepository;
import com.challenge.event_management.service.impl.EventServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.InOrder;

@ExtendWith(MockitoExtension.class)
class EventServiceImplTest {

    @Mock
    private EventRepository eventRepository;

    @Mock
    private EventMapper eventMapper;

    @InjectMocks
    private EventServiceImpl eventService;

    private Event event;
    private EventRequestDTO requestDTO;
    private EventResponseDTO responseDTO;

    @BeforeEach
    void setUp() {
        event = new Event();
        event.setId(1L);
        event.setTitle("Workshop de Java");
        event.setEventDateTime(LocalDateTime.now().plusDays(5));
        event.setLocation("Auditório Central");

        requestDTO = new EventRequestDTO();
        requestDTO.setTitle("Workshop de Java");
        requestDTO.setEventDateTime(event.getEventDateTime());
        requestDTO.setLocation("Auditório Central");

        responseDTO = new EventResponseDTO();
        responseDTO.setId(1L);
        responseDTO.setTitle("Workshop de Java");
    }

    @Test
    void deveCriarEventoComSucesso() {
        when(eventMapper.toEntity(requestDTO)).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toResponseDTO(event)).thenReturn(responseDTO);

        EventResponseDTO result = eventService.create(requestDTO);

        assertThat(result.getTitle()).isEqualTo("Workshop de Java");
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void deveLancarExcecaoQuandoEventoNaoEncontrado() {
        when(eventRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.findById(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveMarcarEventoComoDeletadoAoInvesDeRemoverFisicamente() {
        when(eventRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.softDelete(1L);

        assertThat(event.isDeleted()).isTrue();
        verify(eventRepository, never()).deleteById(any());
    }

    @Test
    void deveLancarExcecaoQuandoSoftDeleteComIdInexistente() {
        when(eventRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.softDelete(99L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveFindByIdComSucesso() {
        when(eventRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(event));
        when(eventMapper.toResponseDTO(event)).thenReturn(responseDTO);

        EventResponseDTO result = eventService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getTitle()).isEqualTo("Workshop de Java");
        verify(eventRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void deveSoftDeleteIdempotente() {
        when(eventRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(event);

        eventService.softDelete(1L);
        assertThat(event.isDeleted()).isTrue();

        // Segunda chamada no mesmo evento já deletado
        when(eventRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.softDelete(1L))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deveAtualizarEventoComSucesso() {
        EventRequestDTO updateDTO = new EventRequestDTO();
        updateDTO.setTitle("Workshop de Spring Boot");
        updateDTO.setEventDateTime(LocalDateTime.now().plusDays(10));
        updateDTO.setLocation("Auditório B");

        Event updatedEvent = new Event();
        updatedEvent.setId(1L);
        updatedEvent.setTitle("Workshop de Spring Boot");

        EventResponseDTO updatedResponseDTO = new EventResponseDTO();
        updatedResponseDTO.setId(1L);
        updatedResponseDTO.setTitle("Workshop de Spring Boot");

        when(eventRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.of(event));
        when(eventRepository.save(any(Event.class))).thenReturn(updatedEvent);
        when(eventMapper.toResponseDTO(updatedEvent)).thenReturn(updatedResponseDTO);

        EventResponseDTO result = eventService.update(1L, updateDTO);

        assertThat(result.getTitle()).isEqualTo("Workshop de Spring Boot");
        verify(eventRepository, times(1)).save(any(Event.class));
    }

    @Test
    void deveVerificarSequenciaDeMapperAndSaveAoCriar() {
        InOrder inOrder = inOrder(eventMapper, eventRepository);

        when(eventMapper.toEntity(requestDTO)).thenReturn(event);
        when(eventRepository.save(any(Event.class))).thenReturn(event);
        when(eventMapper.toResponseDTO(event)).thenReturn(responseDTO);

        eventService.create(requestDTO);

        // Verifica ordem: mapper.toEntity -> repository.save -> mapper.toResponseDTO
        inOrder.verify(eventMapper).toEntity(requestDTO);
        inOrder.verify(eventRepository).save(any(Event.class));
        inOrder.verify(eventMapper).toResponseDTO(event);
    }

    @Test
    void deveLancarExcecaoQuandoRepositorioFalhaAoSalvar() {
        when(eventMapper.toEntity(requestDTO)).thenReturn(event);
        when(eventRepository.save(any(Event.class)))
                .thenThrow(new RuntimeException("Falha de conexão com banco"));

        assertThatThrownBy(() -> eventService.create(requestDTO))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Falha de conexão com banco");

        verify(eventMapper, times(1)).toEntity(requestDTO);
        verify(eventRepository, times(1)).save(any(Event.class));
        // Mapper.toResponseDTO não deve ser chamado se save falhar
        verify(eventMapper, never()).toResponseDTO(any());
    }

    @Test
    void deveFindByIdComEventoDeletado() {
        Event deletedEvent = new Event();
        deletedEvent.setId(1L);
        deletedEvent.setDeleted(true);

        when(eventRepository.findByIdAndDeletedFalse(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> eventService.findById(1L))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(eventRepository, times(1)).findByIdAndDeletedFalse(1L);
    }

    @Test
    void deveAtualizarEventoComIdInexistente() {
        when(eventRepository.findByIdAndDeletedFalse(99L)).thenReturn(Optional.empty());

        EventRequestDTO updateDTO = new EventRequestDTO();
        updateDTO.setTitle("Novo Título");
        updateDTO.setEventDateTime(LocalDateTime.now().plusDays(5));
        updateDTO.setLocation("Local");

        assertThatThrownBy(() -> eventService.update(99L, updateDTO))
                .isInstanceOf(ResourceNotFoundException.class);

        verify(eventRepository, never()).save(any());
    }
}