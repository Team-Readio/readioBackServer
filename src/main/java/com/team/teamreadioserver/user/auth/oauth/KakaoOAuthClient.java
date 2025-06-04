package com.team.teamreadioserver.user.auth.oauth;

import com.team.teamreadioserver.user.dto.KakaoUserInfoDTO;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class KakaoOAuthClient {

  private final RestTemplate restTemplate = new RestTemplate();

  public KakaoUserInfoDTO getUserInfo(String accessToken) {
    HttpHeaders headers = new HttpHeaders();
    headers.setBearerAuth(accessToken);
    HttpEntity<Void> entity = new HttpEntity<>(headers);

    ResponseEntity<KakaoUserInfoDTO> response = restTemplate.exchange(
        "https://kapi.kakao.com/v2/user/me",
        HttpMethod.GET,
        entity,
        KakaoUserInfoDTO.class
    );

    return response.getBody();
  }
}
