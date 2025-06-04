package com.team.teamreadioserver.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JoinRequestDTO {

  @NotBlank
  private String userId;

  @NotBlank
  private String userName;

  @NotBlank
  private String userPwd;

  @NotBlank
  @Email
  private String userEmail;

  @NotBlank
  private String userPhone;

  @NotBlank
  private String userBirthday;

  // 새로 추가된 필드
  private boolean isSocial = false;          // 기본값 false (일반회원)

  private String socialProvider;             // ex) "KAKAO", "GOOGLE", null 가능

}
