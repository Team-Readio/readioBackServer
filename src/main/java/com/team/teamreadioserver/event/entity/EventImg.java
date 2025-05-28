package com.team.teamreadioserver.event.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Entity
@Table(name = ("event_img"))
@Getter
@Setter
public class EventImg {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("img_id"))
    private Long imgId;

    @Column(name = ("original_name"))
    private String originalName;

    @Column(name = ("saved_name"))
    private String savedName;

    @JsonIgnore
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "event_id")
    private Event event;
}
