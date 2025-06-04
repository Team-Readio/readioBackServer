package com.team.teamreadioserver.user.controller;

import com.team.teamreadioserver.user.dto.JwtResponseDTO;
import com.team.teamreadioserver.user.dto.KakaoLoginRequestDTO;
import com.team.teamreadioserver.user.dto.KakaoUserInfoDTO;
import com.team.teamreadioserver.user.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
public class KaKaoAuthController {

  private final KakaoAuthService kakaoAuthService;

  public KaKaoAuthController(KakaoAuthService kakaoAuthService) {
    this.kakaoAuthService = kakaoAuthService;
  }

  @PostMapping("/kakao")
  public ResponseEntity<JwtResponseDTO> kakaoLogin(@RequestBody KakaoLoginRequestDTO request) {
    String accessToken = request.getAccessToken();

    KakaoUserInfoDTO kakaoUserInfo = kakaoAuthService.getKakaoUserInfo(accessToken);

    JwtResponseDTO jwtResponse = kakaoAuthService.registerOrLogin(kakaoUserInfo);

    return ResponseEntity.ok(jwtResponse);
  }
}
