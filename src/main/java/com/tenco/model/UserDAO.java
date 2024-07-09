package com.tenco.model;

import java.util.List;

public interface UserDAO {
	
	// 기능 설계
	// 회원가입 기능
	int addUser(UserDTO userDTO); // insert 구문이라 void 도 가능
	
	// id로 사용자 정보 조회
	UserDTO getUserById(int id);
	
	// username으로 사용자 정보 조회
	UserDTO getUserByUsername(String username);
	
	// 관리자 기능
	List<UserDTO> getAllUsers(); // 다중행
	
	// 사용자 정보 수정 - 외부에서 변경할 값 받아와야 하나 UserDTO 사용하면 됨.
	int updateUser(UserDTO user, int principalId); // 권한(나의 정보는 나만 변경) - 인증 검사(세션 ID 필요)
	// UserDTO user, int principalId: 수정할 데이터와 권한
	
	// 정보 삭제
	int deleteUser(int id); // 인증 검사 - 세션에 같은 아이디인지 확인
	
}
