package com.tenco.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * DTO : 데이터를 변환, 담는 개념, 메서드 사용할 수 있다. 
 * 데이터를 변화, 담는 개념 --> VO 객체
 */
@Data
@NoArgsConstructor // 기본 생성자
@AllArgsConstructor // 각 변수 생성자
@Builder
@ToString //개발할 때 편히 볼 수 있도록
public class UserDTO {
	
	private int id;
	private String username;
	private String password;
	private String email;
	private String createdAt;
	
	// 필요하다면 메서드 정의 가능
}
