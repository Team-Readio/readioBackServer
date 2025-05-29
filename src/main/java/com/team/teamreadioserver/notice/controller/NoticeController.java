package com.team.teamreadioserver.notice.controller;

import com.team.teamreadioserver.notice.dto.NoticeRequestDTO;
import com.team.teamreadioserver.notice.dto.NoticeResponseDTO;
import com.team.teamreadioserver.notice.dto.NoticeUpdateDTO;
import com.team.teamreadioserver.notice.entity.Notice;
import com.team.teamreadioserver.notice.repository.NoticeRepository;
import com.team.teamreadioserver.notice.service.NoticeService;
import com.team.teamreadioserver.user.auth.model.DetailsUser;
import com.team.teamreadioserver.user.entity.User;
import jakarta.validation.Valid;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import io.swagger.v3.oas.annotations.Operation;

import java.util.List;

@RestController
@RequestMapping("/serviceCenter")
public class NoticeController {

    @Autowired
    private NoticeService noticeService;
    @Autowired
    private NoticeRepository noticeRepository;

    @Operation(summary = "공지 등록", description = "새로운 공지사항을 등록합니다.")
    @PostMapping("/notice/write")
    public ResponseEntity<String> createNotice(
            @RequestBody @Valid NoticeRequestDTO noticeRequestDTO,
            @AuthenticationPrincipal DetailsUser detailsUser) {
        if(detailsUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        User user = detailsUser.getUser();

        noticeService.writeNotice(noticeRequestDTO, user);
        return ResponseEntity.status(HttpStatus.CREATED).body("공지사항이 성공적으로 등록되었습니다.");
    }

    @Operation(summary = "공지사항 수정", description ="공지사항을 수정합니다.")
    @PutMapping("/notice/update")
    public ResponseEntity<String> updateNotice(
            @RequestBody @Valid NoticeUpdateDTO noticeUpdateDTO,
            @AuthenticationPrincipal DetailsUser detailsUser) {
        if(detailsUser == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        User user = detailsUser.getUser();
        try{
            noticeService.updateNotice(noticeUpdateDTO, user);
            return ResponseEntity.ok("공지사항이 성공적으로 수정되었습니다.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "공지사항 삭제", description = "공지사항을 삭제합니다.")
    @DeleteMapping("/notice/delete/{noticeId}")
    public ResponseEntity<String> deleteNotice( // ✅ 반환 타입을 ResponseEntity<String>으로 변경
                                                @PathVariable Integer noticeId,
                                                @AuthenticationPrincipal DetailsUser detailsUser) { // ✅ DetailsUser로 타입 변경
        if (detailsUser == null){
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("로그인이 필요합니다.");
        }
        User user = detailsUser.getUser(); // ✅ DetailsUser에서 User 엔티티 꺼내기
        try{
            noticeService.deleteNotice(noticeId, user); // ✅ 서비스로 User 엔티티 전달
            return ResponseEntity.ok("공지사항이 삭제되었습니다.");
        }catch (IllegalArgumentException e){
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        }
    }

    @Operation(summary = "공지사항 목록 조회", description = "공지사항 리스트를 조회합니다.")
    @GetMapping("/notice/list")
    public List<NoticeResponseDTO> noticeList() {
        return noticeService.getNoticeList();
    }

    @Operation(summary = "공지사항 상세", description = "공지사항 게시글을 상세 조회합니다.")
    @GetMapping("/notice/detail/{noticeId}")
    public ResponseEntity<NoticeResponseDTO> getNoticeDetail(@PathVariable Integer noticeId) {
        NoticeResponseDTO notice = noticeService.detail(noticeId);
        return ResponseEntity.ok(notice);
    }
    @Operation(summary = "페이징 처리", description = "게시글 수에 맞춰 페이징 처리합니다.")
    @GetMapping("/notice/list/paging")
    public Page<NoticeResponseDTO> list(@PageableDefault(page=0, size = 7, sort = "noticeId", direction = Sort.Direction.DESC) Pageable pageable)  {
        // ✅ 이 메서드는 NoticeService에 추가되어 있어야 합니다.
        // 현재 이 메서드가 NoticeService에 없거나 시그니처가 다르면 빨간 줄이 뜹니다.
        return noticeService.getPagedNoticeList(pageable);
    }

    @Operation(summary = "제목으로 공지사항 검색", description = "제목에 포함된 키워드로 공지사항을 검색합니다.")
    @GetMapping("/notice/search")
    public ResponseEntity<List<NoticeResponseDTO>> searchNotices(@RequestParam String keyword) {
        List<NoticeResponseDTO> results = noticeService.searchNoticesByTitle(keyword);
        return ResponseEntity.ok(results);
    }
}
