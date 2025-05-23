package com.team.teamreadioserver.video.controller;

import com.team.teamreadioserver.common.common.ResponseDTO;
import com.team.teamreadioserver.filtering.dto.FilteringDTO;
import com.team.teamreadioserver.filtering.dto.FilteringGroupDTO;
import com.team.teamreadioserver.filtering.dto.FilteringGroupDetailDTO;
import com.team.teamreadioserver.video.dto.CurationKeywordsDTO;
import com.team.teamreadioserver.video.service.CurationKeywordsService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/curation")
@CrossOrigin(origins = "http://localhost:5173")
@RequiredArgsConstructor
public class CurationKeywordsController {

    private static final Logger log = LoggerFactory.getLogger(CurationKeywordsController.class);
    private final CurationKeywordsService curationKeywordsService;

    @Operation(summary = "큐레이션 키워드 조회", description = "큐레이션 키워드가 조회됩니다.", tags = { "CurationKeywordsController" })
    @GetMapping("/{type}")
    public ResponseEntity<ResponseDTO> selectCurationKeywordsByType(@PathVariable String type)
    {
        log.info("[CurationKeywordsController] selectCurationKeywordsByType");

        List<CurationKeywordsDTO> result = curationKeywordsService.selectCurationKeywordsByType(type);

        return ResponseEntity.ok().body(
                new ResponseDTO(HttpStatus.OK, "큐레이션 키워드 조회 성공", result));

    }
}
