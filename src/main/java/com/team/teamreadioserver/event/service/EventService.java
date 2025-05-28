package com.team.teamreadioserver.event.service;

import com.team.teamreadioserver.event.dto.EventRequestDTO;
import com.team.teamreadioserver.event.dto.EventResponseDTO;
import com.team.teamreadioserver.event.dto.EventUpdateDTO;
import com.team.teamreadioserver.event.entity.Event;
import com.team.teamreadioserver.event.entity.EventImg;
import com.team.teamreadioserver.event.repository.EventRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EventService {
    @Autowired
    private EventRepository eventRepository;

    public List<EventResponseDTO> getEventList() {
        List<Event> events = eventRepository.findAllByOrderByEventCreateAtDesc(); // 최신순 정렬

        return events.stream()
                .map(event -> new EventResponseDTO(
                        event.getEventId(),
                        event.getEventTitle(),
                        event.getEventContent(),
                        event.getEventCreateAt(),
                        event.getEventView(),
                        event.getEventState()
                ))
                .collect(Collectors.toList());
    }

    public void writeEvent(EventRequestDTO requestDTO) {
        Event event = Event.builder()
                .eventTitle(requestDTO.getEventTitle())
                .eventContent(requestDTO.getEventContent())
                .eventState(requestDTO.getEventState())
                .build();

        if(requestDTO.getEventImg() != null) {
            EventImg img = new EventImg();
            img.setOriginalName(requestDTO.getEventImg().getOriginalName());
            img.setSavedName(requestDTO.getEventImg().getSavedName());

            event.setEventImg(img);
        }



        eventRepository.save(event);
    }
    @Transactional
    public void updateEvent(EventUpdateDTO updateDTO) {
        Event event = eventRepository.findById(updateDTO.getEventId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다."));

        EventImg img = null;
        if (updateDTO.getEventImg() != null) {
            img = new EventImg();
            img.setOriginalName(updateDTO.getEventImg().getOriginalName());
            img.setSavedName(updateDTO.getEventImg().getSavedName());
        }

        event.update(
                updateDTO.getEventTitle(),
                updateDTO.getEventContent(),
                updateDTO.getEventState(),
                img
        );
    }

    @Transactional
    public void deleteEvent(Integer eventId) {
        Event event = eventRepository.findById(eventId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다."));

        eventRepository.delete(event);
    }

    public EventResponseDTO detail(Integer eventId) {
        return eventRepository.findById(eventId)
                .map(EventResponseDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));
    }

    public List<EventResponseDTO> searchEventsByTitle(String keyword) {
        List<Event> events = eventRepository.findByEventTitleContainingIgnoreCase(keyword);

        return events.stream()
                .map(EventResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }


}
