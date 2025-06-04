package com.team.teamreadioserver.user.dto;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JwtResponseDTO {

  private String accessToken;
  private String tokenType = "Bearer";
  private String userId;
  private String userName;
  private String userRole;  // 추가



  public JwtResponseDTO(String token) {
    this.accessToken = token;
    this.tokenType = "Bearer"; // 생략 시 null일 수 있음
  }

  public JwtResponseDTO(String accessToken, String userId, String userName, String userRole){
    this.accessToken = accessToken;
    this.userId = userId;
    this.userName = userName;
    this.userRole = userRole;
    this.tokenType = "Bearer"; // 기본값 명시적으로 설정
  }

}

