package com.team.teamreadioserver.user.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "user")
public class User {

  @Id
  @Column(name = "user_id", nullable = false, length = 30)
  private String userId;

  @Enumerated(EnumType.STRING)
  @Column(name = "user_role", nullable = false)
  private UserRole userRole;

  @Column(name = "user_name", nullable = false, length = 30)
  private String userName;

  @Column(name = "user_pwd")  // NOTNULL 풀어놨음
  private String userPwd;

  @Column(name = "user_email", nullable = false)
  private String userEmail;

  @Column(name = "user_phone", nullable = false, length = 15)
  private String userPhone;

  @Column(name = "user_birthday", nullable = false)
  private LocalDate userBirthday;

  @Column(name = "user_enrolldate", nullable = false)
  private LocalDateTime userEnrollDate;

  // ✅ 추가된 소셜 로그인 관련 필드
  @Column(name = "is_social", nullable = false)
  private boolean isSocial;

  @Column(name = "social_provider", length = 30)
  private String socialProvider;

}
