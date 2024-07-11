package com.tenco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.tenco.model.TodoDAO;
import com.tenco.model.TodoDAOImpl;
import com.tenco.model.TodoDTO;
import com.tenco.model.UserDAO;
import com.tenco.model.UserDAOImpl;
import com.tenco.model.UserDTO;

@WebServlet("/test/*")
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
	private TodoDAO todoDAO;
       
    public TestController() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	userDAO = new UserDAOImpl();
    	todoDAO = new TodoDAOImpl();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		switch (action) {
		// http://localhost:8080/mvc/test/byId
		case "/byId":
			
			// select - Id
//			userDAO.getUserById(1);
			
			// select - Username
//			userDAO.getUserByUsername("홍길동");
			
			// select - All
//			List<UserDTO> list = userDAO.getAllUsers();
//			if(list.size() == 0) {
//				
//			}
			// System.out.println(userDAO.deleteUser(5));
			UserDTO dto =  UserDTO.builder().password("999").email("h@naver.com").build();
			int count = userDAO.updateUser(dto, 3);
			// builder: username 필요 없으면 안 넣어도 됨.
			System.out.println("count : " + count);
			
			break;
			// http://localhost:8080/mvc/test/bytodo
		case "/bytodo":
			//select - Id
//			TodoDTO todoDTO = todoDAO.getTodoById(1);
//			System.out.println(todoDTO.toString());
			
			//select - UserId
//			List<TodoDTO> list = todoDAO.getTodosByUserId(1);
//			System.out.println(list.toString());
			
			
			//select - All
//			System.out.print(todoDAO.getAllTodos().toString());
			
			//update
//			TodoDTO dto1 = TodoDTO.builder()
//								  .title("타이틀333")
//								  .description("설명글333")
//								  .due_date()
//								  .Completed()
//								  .id()
//								  .user_id()
//								  .build();
//			int Count = todoDAO.updateTodo(dto1, 3);
//			System.out.println("Count : " + Count);
			
			//todoDAO.updateTodo(new TodoDTO(), 1);
			
			//delete
//			int count1 = todoDAO.deleteTodo(3, 2);
//			System.out.println("count1 : " + count1);
			//todoDAO.deleteTodo(5, 6);
			
		default:
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
