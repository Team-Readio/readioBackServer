package com.team.teamreadioserver.notice.entity;

import com.team.teamreadioserver.notice.enumPackage.NoticeState;

import com.team.teamreadioserver.user.entity.User;
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
@Table(name = ("notice"))
@Getter
public class Notice {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = ("notice_id"))
    private Integer noticeId;

    @Column(name = ("notice_title"))
    private String noticeTitle;

    @Column(name = ("notice_create_at"))
    private LocalDateTime noticeCreateAt;

    @Column(name = ("notice_view"))
    private int noticeView;

    @Column(name = ("notice_content"))
    private String noticeContent;

    @Column(name = ("notice_state"))
    @Enumerated(EnumType.STRING)
    private NoticeState noticeState;

    @OneToOne(mappedBy = "notice", fetch = FetchType.LAZY, cascade = CascadeType.ALL) //fetch 노란줄 무시해도 괜찮음
    private NoticeImg noticeImg;

    @ManyToOne(fetch=FetchType.LAZY)
    @JoinColumn(name="user_id", referencedColumnName = "user_id", nullable = false)
    private User userId;

    @PrePersist
    public void prePersist() {
        this.noticeCreateAt = LocalDateTime.now();
        this.noticeView = 0;
    }

    public void update(String title, String content, NoticeState state, NoticeImg img) {
        this.noticeTitle = title;
        this.noticeContent = content;
        this.noticeState = state;
        this.setNoticeImg(img); // ✅ 이렇게 써야 연결됨
    }

    public void setNoticeImg(NoticeImg img) {
        this.noticeImg = img;
        if (img != null) {
            img.setNotice(this);
        }
    }

}