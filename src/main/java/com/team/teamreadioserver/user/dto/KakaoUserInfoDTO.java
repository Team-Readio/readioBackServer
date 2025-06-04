package com.team.teamreadioserver.user.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

public class KakaoUserInfoDTO {

  private String id;

  @JsonProperty("kakao_account")
  private KakaoAccount kakaoAccount;

  public String getId() {
    return id;
  }
  public void setId(String id) {
    this.id = id;
  }

  public KakaoAccount getKakaoAccount() {
    return kakaoAccount;
  }
  public void setKakaoAccount(KakaoAccount kakaoAccount) {
    this.kakaoAccount = kakaoAccount;
  }

  public static class KakaoAccount {
    private String email;

    private Profile profile;

    public String getEmail() {
      return email;
    }
    public void setEmail(String email) {
      this.email = email;
    }

    public Profile getProfile() {
      return profile;
    }
    public void setProfile(Profile profile) {
      this.profile = profile;
    }
  }

  public static class Profile {
    private String nickname;

    public String getNickname() {
      return nickname;
    }
    public void setNickname(String nickname) {
      this.nickname = nickname;
    }
  }

}
