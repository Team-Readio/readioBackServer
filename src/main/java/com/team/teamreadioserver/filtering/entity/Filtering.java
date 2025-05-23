package com.team.teamreadioserver.filtering.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "filtering")
@Getter
@ToString
@RequiredArgsConstructor
@AllArgsConstructor
public class Filtering {

    @Id
    @Column(name = "filtering_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int filteringId;

    @Column(name = "group_id")
    private int groupId;

    @Column(name = "video_id")
    private String videoId;

    @Column(name = "keyword")
    private String keyword;

}
