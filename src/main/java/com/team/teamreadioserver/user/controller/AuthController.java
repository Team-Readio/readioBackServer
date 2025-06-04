package com.team.teamreadioserver.user.controller;//package com.team.teamreadioserver.user.controller;

import com.team.teamreadioserver.user.auth.jwt.JwtTokenProvider;
import com.team.teamreadioserver.user.dto.JwtResponseDTO;
import com.team.teamreadioserver.user.dto.LoginRequestDTO;
import com.team.teamreadioserver.user.dto.UserInfoResponseDTO;
import com.team.teamreadioserver.user.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.*;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

//로그인 컨트롤러 (로그인 엔드포인트)
@RestController
@RequestMapping("/users")
@Tag(name = "회원 API", description = "회원 로그인 API")
public class AuthController {

  private static final Logger logger = LoggerFactory.getLogger(AuthController.class); // Logger 인스턴스 생성

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private JwtTokenProvider tokenProvider;
  @Autowired
  private UserService userService;

  @Operation(summary = "내 정보 조회", description = "현재 로그인한 사용자의 정보를 조회합니다.")
  @GetMapping("/me")
  public ResponseEntity<UserInfoResponseDTO> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
    if (userDetails == null) {
      logger.warn("/users/me 요청에 인증된 사용자 정보(userDetails)가 없습니다. UNAUTHORIZED.");
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
    }

    // userDetails.getUsername() = 로그인한 사용자 ID
    String userId = userDetails.getUsername();
    logger.info("/users/me 요청: 인증된 사용자 ID = {}", userId); // 추가된 디버깅 로그

    // 예시: 서비스에서 사용자 정보 조회
    UserInfoResponseDTO userInfo = userService.getUserInfo(userId);
    if (userInfo == null) {
      logger.info("/users/me 요청 성공: 사용자 ID = {}, 사용자 이름 = {}", userInfo.getUserId(), userInfo.getUserName()); // 추가된 디버깅 로그
      return ResponseEntity.notFound().build();
    }

    logger.info("/users/me 요청 성공: 사용자 ID = {}, 사용자 이름 = {}", userInfo.getUserId(), userInfo.getUserName()); // 추가된 디버깅 로그
    return ResponseEntity.ok(userInfo);
  }


  @Operation(summary = "로그인", description = "회원이 로그인을 합니다.")
  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody LoginRequestDTO loginRequestDTO) {
    logger.info("로그인 요청 username: " + loginRequestDTO.getUsername());

    try {
      Authentication authentication = authenticationManager.authenticate(
          new UsernamePasswordAuthenticationToken(loginRequestDTO.getUsername(), loginRequestDTO.getPassword())
      );
      UserDetails userDetails = (UserDetails) authentication.getPrincipal();
      String token = tokenProvider.generateToken(authentication.getName());

      // 로그인한 계정 권한 로그 찍기
      Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
      logger.info("로그인한 계정 권한: " + authorities);

      // JwtResponseDTO를 사용해서 응답
      JwtResponseDTO jwtResponse = new JwtResponseDTO(token);
      jwtResponse.setUserId(userDetails.getUsername()); // userId
//      jwtResponse.setUserName(userDetails.getUsername());  주석

      logger.info("로그인 성공: " + userDetails.getUsername() + ", 생성된 토큰: " + token); // 토큰 값 확인하기

      return ResponseEntity.ok(jwtResponse); // JwtResponseDTO 객체 반환

//    } catch (UsernameNotFoundException e) { // <--- 예외를 구체적으로 처리했으나 보안때문에 주석 처리함
//      Map<String, String> errorResponse = new HashMap<>();
//      errorResponse.put("message", "아이디를 찾을 수 없습니다.");
//      System.err.println("로그인 실패 (사용자 없음): " + e.getMessage()); // System.err로 변경
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
//
//    } catch (BadCredentialsException e) { // <--- 예외를 구체적으로 처리했으나 보안때문에 주석 처리함
//      Map<String, String> errorResponse = new HashMap<>();
//      errorResponse.put("message", "비밀번호가 일치하지 않습니다.");
//      System.err.println("로그인 실패 (비밀번호 불일치): " + e.getMessage()); // System.err로 변경
//      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);

    } catch (UsernameNotFoundException | BadCredentialsException e) {
      // 아이디가 없거나 비밀번호가 틀렸을 때 모두 같은 메시지 반환
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "아이디 또는 비밀번호가 일치하지 않습니다.");
      System.err.println("로그인 실패 (인증 실패): " + e.getMessage());
      return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);


    } catch (Exception e) { // <--- 그 외 예상치 못한 예외
      Map<String, String> errorResponse = new HashMap<>();
      errorResponse.put("message", "서버 오류가 발생했습니다.");
      e.printStackTrace(); // 스택 트레이스 출력 (가장 중요)
      System.err.println("로그인 실패 (예상치 못한 오류): " + e.getMessage()); // System.err로 변경
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("errorResponse"); // 500으로 변경
    }
  }

}