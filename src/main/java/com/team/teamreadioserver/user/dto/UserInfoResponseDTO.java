package com.team.teamreadioserver.user.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

// 회원정보 조회용 DTO
@Getter
@Setter
public class UserInfoResponseDTO {

  private String userId;
  private String userName;
  private String userPwd;
  private String userEmail;
  private String userPhone;
  private LocalDate userBirthday;
  private LocalDateTime userEnrollDate; // 가입일자(등록일) 추가
  private String userRole;
  private Long profileId;

  private boolean social; // isSocial() 대응 필드명은 보통 boolean 타입으로

  private String socialProvider; // 소셜 로그인 공급자명

  @Override
  public String toString() {
    return "UserInfoResponseDTO{" +
        "userId='" + userId + '\'' +
        ", userName='" + userName + '\'' +
        ", userEmail='" + userEmail + '\'' +
        ", userPhone='" + userPhone + '\'' +
        ", userBirthday='" + userBirthday + '\'' +
        ", userEnrollDate='" + userEnrollDate + '\'' +
        ", userRole='" + userRole + '\'' +
        ", social=" + social +
        ", socialProvider='" + socialProvider + '\'' +
        '}';
  }
}
