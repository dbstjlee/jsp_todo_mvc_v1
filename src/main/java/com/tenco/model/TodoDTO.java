package com.tenco.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@NoArgsConstructor
@AllArgsConstructor
@ToString
@Builder
public class TodoDTO {

	private int id;
	private int userId;
	private String title;
	private String description;
	private String dueDate;
	private String completed;// "1", "0"
	
	// completed를 데이터 변환 메서드를 만들자.
	public String completedToString() {
		
		// 1이면 완료, 0이면 미완료
		return this.completed.equals("1") ? "true" : "false";
	}
}
