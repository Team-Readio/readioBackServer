package com.team.teamreadioserver.event.dto;

import com.team.teamreadioserver.event.entity.Event;
import com.team.teamreadioserver.event.enumPackage.EventState;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class EventResponseDTO {
    private Integer eventId;
    private String eventTitle;
    private String eventContent;
    private LocalDateTime eventCreateAt;
    private int eventView;
    private EventState eventState;

    public static EventResponseDTO fromEntity(Event event) {
        EventResponseDTO dto = new EventResponseDTO();
        dto.eventId = event.getEventId();
        dto.eventTitle = event.getEventTitle();
        dto.eventContent = event.getEventContent();
        dto.eventState = event.getEventState();
        dto.eventCreateAt = event.getEventCreateAt(); // 날짜 형식에 따라 변경 가능
        dto.eventView = event.getEventView();
        return dto;
    }
}
