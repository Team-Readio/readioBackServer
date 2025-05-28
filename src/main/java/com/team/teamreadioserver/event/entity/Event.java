package com.team.teamreadioserver.event.entity;

import com.team.teamreadioserver.event.enumPackage.EventState;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = ("event"))
@Getter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("event_id"))
    private Integer eventId;

    @Column(name = ("event_title"))
    private String eventTitle;

    @Column(name = ("event_create_at"))
    private LocalDateTime eventCreateAt;

    @Column(name = ("event_view"))
    private int eventView;

    @Column(name = ("event_content"))
    private String eventContent;

    @Column(name = ("event_state"))
    @Enumerated(EnumType.STRING)
    private EventState eventState;

    @OneToOne(mappedBy = "event", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //fetch 노란줄 무시해도 괜찮음
    private EventImg eventImg;

    @Column(name = ("user_id"))
    private String userId;

    @PrePersist
    public void prePersist() {
        this.userId = "test2";
        this.eventCreateAt = LocalDateTime.now();
        this.eventView = 0;
    }

    public void update(String title, String content, EventState state, EventImg img) {
        this.eventTitle = title;
        this.eventContent = content;
        this.eventState = state;
        this.setEventImg(img); // ✅ 이렇게 써야 연결됨
    }

    public void setEventImg(EventImg img) {
        this.eventImg = img;
        if (img != null) {
            img.setEvent(this);
        }
    }

}