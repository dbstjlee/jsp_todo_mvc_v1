package com.tenco.model;

import java.util.List;

public interface TodoDAO {
	
	// 일정 추가 기능
	void addTodo(TodoDTO dto, int principalId);
	
	// Id로 조회
	TodoDTO getTodoById(int id);
	
	// 사용자 아이디 기준으로 todoList
	List<TodoDTO> getTodosByUserId(int userId);
	
	//전체 select (where 절X)
	List<TodoDTO> getAllTodos(); 
	
	// 일정 수정
	void updateTodo(TodoDTO dto, int principalId);
	
	// 일정 삭제
	void deleteTodo(int id, int principalId);
}
