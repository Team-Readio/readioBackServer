package com.team.teamreadioserver.user.auth.jwt;

import com.team.teamreadioserver.user.auth.service.CustomUserDetailsService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtAuthenticationFilter extends OncePerRequestFilter {

  private final JwtTokenProvider tokenProvider;
  private final CustomUserDetailsService userDetailsService;

  // 디버깅용 로거 사용
  private static final Logger logger = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

  public JwtAuthenticationFilter(JwtTokenProvider tokenProvider, CustomUserDetailsService userDetailsService) {
    this.tokenProvider = tokenProvider;
    this.userDetailsService = userDetailsService;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request,
                                  HttpServletResponse response,
                                  FilterChain filterChain) throws ServletException, IOException {

    String requestURI = request.getRequestURI();
    logger.info("---- JWT Filter 시작: 요청 URI = {}", requestURI);

    //  /api/clicks 경로는 인증 없이 통과시킴
    if (requestURI.startsWith("/api/clicks")) {
      logger.info("✅ /api/clicks 요청은 인증 없이 통과");
      filterChain.doFilter(request, response);
      return;
    }

    String token = getTokenFromRequest(request);
    logger.info("JWT 토큰: " + token);        // 주석 해도 되고 안해도 되고
//        메인 페이지가 permitAll()로 설정되어 있고, 로그인 직후 새로고침되므로 null 값 반환은 정상
//        실제로는 정상발급되며, 네트워크 확인해보면 토큰값 들어있음. 다른 경로로 이동시에도 토큰값이 정상적으로 출력됨

    if (token != null && tokenProvider.validateToken(token)) {
      String username = tokenProvider.getUserIdFromJWT(token);
      logger.info("토큰에서 추출한 username: " + username);

      try {
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        UsernamePasswordAuthenticationToken authentication =
            new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());

        logger.debug("  >> SecurityContextHolder에 {} 사용자로 인증 설정 시도.", username);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        logger.debug("  >> SecurityContextHolder에 {} 사용자 인증 성공. 현재 컨텍스트: {}", username, SecurityContextHolder.getContext().getAuthentication().getName()); // 현재 인증된 사용자 이름 확인

      } catch (Exception e) {
        logger.error("사용자 인증 처리 중 오류 발생", e);
        SecurityContextHolder.clearContext();
      }
    } else {
      logger.info("JWT 토큰이 없거나 토큰이 만료/비정상입니다. SecurityContextHolder 초기화");
      SecurityContextHolder.clearContext();  // 인증 초기화
    }
    filterChain.doFilter(request, response);
    logger.info("---- JWT Filter 종료: 요청 URI = {}", requestURI); // 추가된 디버깅 로그
  }

  private String getTokenFromRequest(HttpServletRequest request) {
    String bearer = request.getHeader("Authorization");
    if (bearer != null && bearer.startsWith("Bearer ")) {
      String token = bearer.substring(7).trim();
      if (token.isEmpty() || "null".equalsIgnoreCase(token) || "undefined".equalsIgnoreCase(token)) {
        return null;
      }
      return token;
    }
    return null;

//    private String getTokenFromRequest(HttpServletRequest request) {
//        String bearer = request.getHeader("Authorization");
//        if (bearer != null && bearer.startsWith("Bearer ")) {
//            return bearer.substring(7);
//        }
//        return null;
//    }
  }
}
