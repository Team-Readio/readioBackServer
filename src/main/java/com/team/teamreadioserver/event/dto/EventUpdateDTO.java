package com.team.teamreadioserver.event.dto;

import com.team.teamreadioserver.event.enumPackage.EventState;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class EventUpdateDTO {
    private Integer eventId;
    @NotBlank(message = "제목은 공백이 아니어야 합니다.")
    private String eventTitle;
    @NotBlank(message = "내용은 공백이 아니어야 합니다.")
    private String eventContent;
    @NotNull(message = "말머리는 무조건 선택되어야 합니다.")
    private EventState eventState;

    private EventImgDTO eventImg;
}
