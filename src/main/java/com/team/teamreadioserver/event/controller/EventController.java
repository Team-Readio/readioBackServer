package com.team.teamreadioserver.event.controller;

import com.team.teamreadioserver.event.dto.EventRequestDTO;
import com.team.teamreadioserver.event.dto.EventResponseDTO;
import com.team.teamreadioserver.event.dto.EventUpdateDTO;
import com.team.teamreadioserver.event.entity.Event;
import com.team.teamreadioserver.event.repository.EventRepository;
import com.team.teamreadioserver.event.service.EventService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/serviceCenter")
public class EventController {

    @Autowired
    private EventService eventService;
    @Autowired
    private EventRepository eventRepository;

    @Operation(summary = "공지 등록", description = "새로운 공지사항을 등록합니다.")
    @PostMapping("/event/write")
    public String createEvent(@RequestBody @Valid EventRequestDTO eventRequestDTO) {
        eventService.writeEvent(eventRequestDTO);
        return "공지사항이 성공적으로 등록되었습니다.";
    }

    @Operation(summary = "공지사항 수정", description ="공지사항을 수정합니다.")
    @PutMapping("/event/update")
    public String updateEvent(@RequestBody @Valid EventUpdateDTO eventUpdateDTO) {
        eventService.updateEvent(eventUpdateDTO);
        return "공지사항이 성공적으로 수정되었습니다.";
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.")
    @DeleteMapping("/event/delete/{eventId}")
    public String deleteEvent(@PathVariable Integer eventId) {
        eventService.deleteEvent(eventId);
        return "공지사항이 삭제되었습니다.";
    }

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 리스트를 조회합니다.")
    @GetMapping("/event/list")
    public List<EventResponseDTO> EventList() {
        return eventService.getEventList();
    }

    @Operation(summary = "공지사항 상세", description = "공지사항 게시글을 상세 조회합니다.")
    @GetMapping("/event/detail/{eventId}")
    public ResponseEntity<EventResponseDTO> getEventDetail(@PathVariable Integer eventId) {
        EventResponseDTO event = eventService.detail(eventId);
        return ResponseEntity.ok(event);
    }
    @Operation(summary = "페이징 처리", description = "게시글 수에 맞춰 페이징 처리합니다.")
    @GetMapping("/event/list/paging")
    public Page<Event> list(@PageableDefault(page=0, size = 7, sort = "eventId", direction = Sort.Direction.DESC) Pageable pageable)  {
        return eventRepository.findAll(pageable);
    }

    @Operation(summary = "제목으로 공지사항 검색", description = "제목에 포함된 키워드로 공지사항을 검색합니다.")
    @GetMapping("/event/search")
    public ResponseEntity<List<EventResponseDTO>> searchEvents(@RequestParam String keyword) {
        List<EventResponseDTO> results = eventService.searchEventsByTitle(keyword);
        return ResponseEntity.ok(results);
    }
}
