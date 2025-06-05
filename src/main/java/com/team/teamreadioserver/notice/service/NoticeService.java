package com.team.teamreadioserver.notice.service;

import com.team.teamreadioserver.notice.dto.NoticeRequestDTO;
import com.team.teamreadioserver.notice.dto.NoticeResponseDTO;
import com.team.teamreadioserver.notice.dto.NoticeUpdateDTO;
import com.team.teamreadioserver.notice.entity.Notice;
import com.team.teamreadioserver.notice.entity.NoticeImg;
import com.team.teamreadioserver.notice.repository.NoticeRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class NoticeService {
    @Autowired
    private NoticeRepository noticeRepository;

    public List<NoticeResponseDTO> getNoticeList() {
        List<Notice> notices = noticeRepository.findAllByOrderByNoticeCreateAtDesc(); // 최신순 정렬

        return notices.stream()
                .map(notice -> new NoticeResponseDTO(
                        notice.getNoticeId(),
                        notice.getNoticeTitle(),
                        notice.getNoticeContent(),
                        notice.getNoticeCreateAt(),
                        notice.getNoticeView(),
                        notice.getNoticeState(),
                        notice.getUserId()
                ))
                .collect(Collectors.toList());
    }

    public void writeNotice(NoticeRequestDTO requestDTO) {
        Notice notice = Notice.builder()
                .noticeTitle(requestDTO.getNoticeTitle())
                .noticeContent(requestDTO.getNoticeContent())
                .noticeState(requestDTO.getNoticeState())
                .build();

        if(requestDTO.getNoticeImg() != null) {
            NoticeImg img = new NoticeImg();
            img.setOriginalName(requestDTO.getNoticeImg().getOriginalName());
            img.setSavedName(requestDTO.getNoticeImg().getSavedName());

            notice.setNoticeImg(img);
        }

        noticeRepository.save(notice);
    }

    @Transactional
    public void updateNotice(NoticeUpdateDTO updateDTO) {
        Notice notice = noticeRepository.findById(updateDTO.getNoticeId())
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다."));

        NoticeImg img = null;
        if (updateDTO.getNoticeImg() != null) {
            img = new NoticeImg();
            img.setOriginalName(updateDTO.getNoticeImg().getOriginalName());
            img.setSavedName(updateDTO.getNoticeImg().getSavedName());
        }

        notice.update(
                updateDTO.getNoticeTitle(),
                updateDTO.getNoticeContent(),
                updateDTO.getNoticeState(),
                img
        );
    }

    @Transactional
    public void deleteNotice(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("해당 공지사항이 없습니다."));

        noticeRepository.delete(notice);
    }

    @Transactional // 트랜잭션 추가
    public NoticeResponseDTO detail(Integer noticeId) {
        Notice notice = noticeRepository.findById(noticeId)
                .orElseThrow(() -> new IllegalArgumentException("공지사항이 존재하지 않습니다."));

        // 조회수 1 증가
        notice.setNoticeView(notice.getNoticeView() + 1);
        // save를 명시적으로 호출할 필요 없음: @Transactional 어노테이션이 붙은 메서드 내에서 엔티티의 상태가 변경되면
        // 트랜잭션이 커밋될 때 자동으로 변경사항이 데이터베이스에 반영됩니다 (Dirty Checking).

        return NoticeResponseDTO.fromEntity(notice);
    }

    public List<NoticeResponseDTO> searchNoticesByTitle(String keyword) {
        List<Notice> notices = noticeRepository.findByNoticeTitleContainingIgnoreCase(keyword);

        return notices.stream()
                .map(NoticeResponseDTO::fromEntity)
                .collect(Collectors.toList());
    }
}