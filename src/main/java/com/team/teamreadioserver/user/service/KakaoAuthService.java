package com.team.teamreadioserver.user.service;



import com.team.teamreadioserver.user.auth.jwt.JwtTokenProvider;
import com.team.teamreadioserver.user.auth.oauth.KakaoOAuthClient;
import com.team.teamreadioserver.user.dto.JwtResponseDTO;
import com.team.teamreadioserver.user.dto.KakaoUserInfoDTO;
import com.team.teamreadioserver.user.dto.UserInfoResponseDTO;
import com.team.teamreadioserver.user.entity.User;
import com.team.teamreadioserver.user.entity.UserRole;
import com.team.teamreadioserver.user.mapper.UserMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class KakaoAuthService {

  private final UserMapper userMapper;
  private final KakaoOAuthClient kakaoOAuthClient;
  private final JwtTokenProvider jwtTokenProvider;

  public KakaoAuthService(UserMapper userMapper,
                          KakaoOAuthClient kakaoOAuthClient,
                          JwtTokenProvider jwtTokenProvider) {
    this.userMapper = userMapper;
    this.kakaoOAuthClient = kakaoOAuthClient;
    this.jwtTokenProvider = jwtTokenProvider;
  }

  public KakaoUserInfoDTO getKakaoUserInfo(String accessToken) {
    return kakaoOAuthClient.getUserInfo(accessToken);
  }

  public JwtResponseDTO registerOrLogin(KakaoUserInfoDTO kakaoUserInfo) {
    String kakaoId = kakaoUserInfo.getId();
    String userId = "KAKAO_" + kakaoId;

    UserInfoResponseDTO userDto = userMapper.selectUserById(userId);

    User user = null;
    // 회원이 없으면 등록
    if (userDto == null) {
      user = User.builder()
          .userId(userId)
          .userName(kakaoUserInfo.getKakaoAccount().getProfile().getNickname())
          .userPwd("") // 소셜 로그인은 비워둠
          .userEmail("")
          .userPhone("") // 기본값
          .userBirthday(LocalDate.of(2000, 1, 1)) // 기본값
          .userEnrollDate(LocalDateTime.now())
          .userRole(UserRole.USER)
          .isSocial(true)
          .socialProvider("KAKAO")
          .build();

      userMapper.insertUserEntity(user);
    } else {
      // 회원 있으면 DTO → Entity 변환
      user = User.builder()
          .userId(userDto.getUserId())
          .userName(userDto.getUserName())
          .userPwd(userDto.getUserPwd())
          .userEmail(userDto.getUserEmail())
          .userPhone(userDto.getUserPhone())
          .userBirthday(userDto.getUserBirthday())
          .userEnrollDate(userDto.getUserEnrollDate())
          .userRole(UserRole.valueOf(userDto.getUserRole()))
          .isSocial(userDto.isSocial())
          .socialProvider(userDto.getSocialProvider())
          .build();
    }

    String jwt = jwtTokenProvider.generateToken(user.getUserId());
    return new JwtResponseDTO(jwt, user.getUserId(), user.getUserName(), user.getUserRole().name());
  }

}