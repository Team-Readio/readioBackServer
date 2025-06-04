package com.team.teamreadioserver.user.mapper;

import com.team.teamreadioserver.user.dto.JoinRequestDTO;
import com.team.teamreadioserver.user.dto.UserEditRequestDTO;
import com.team.teamreadioserver.user.dto.UserInfoResponseDTO;
import com.team.teamreadioserver.user.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {

  /** 회원가입 **/
  int insertUser(JoinRequestDTO joinRequestDTO);

  /** 아이디 중복 체크 **/
  int countByUserId(String userId);

  /** 이메일 중복 체크 **/
  int countByUserEmail(String userEmail);

  /** 전화번호 중복 체크 **/
  int countByUserPhone(String userPhone);

  /** 로그인(아이디 확인) **/
  User findByUserId(@Param("userId") String userId);

  /** 로그인(비밀번호 확인) **/
  String getPasswordByUserId(@Param("userId") String userId);

  /** 내 정보 수정 진입 전 UserId 로 비밀번호 확인(검증) **/
  UserInfoResponseDTO selectUserById(@Param("userId") String userId);

  /** 회원정보수정 **/
  int updateUser(UserEditRequestDTO userEditRequestDTO);

  String selectPasswordByUserId(@Param("userId") String userId);

  /** 회원정보수정시 중복확인(이메일,전화번호만) **/
  int countByEmailExceptSelf(@Param("userEmail") String userEmail, @Param("currentUserId") String currentUserId);
  int countByPhoneExceptSelf(@Param("userPhone") String userPhone, @Param("currentUserId") String currentUserId);

  /** 이름과 전화번호로 아이디 조회 **/
  String findIdByNameAndPhone(@Param("userName") String userName, @Param("userPhone") String userPhone);

  /** 아이디와 이메일로 비밀번호 재설정 가능 여부 확인 **/
  int existsByUserIdAndEmail(@Param("userId") String userId, @Param("userEmail") String email);

  /** 비밀번호 업데이트 **/
  int updatePassword(@Param("userId") String userId, @Param("newPassword") String newPassword);

  /** 회원탈퇴 **/
  int deleteUserById(@Param("userId") String userId);

  /*카카오 로그인*/
  int insertUserEntity(User user);

}
