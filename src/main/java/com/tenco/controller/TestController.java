package com.tenco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

import com.tenco.model.UserDAO;
import com.tenco.model.UserDAOImpl;
import com.tenco.model.UserDTO;

@WebServlet("/test/*")
public class TestController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO;
       
    public TestController() {
        super();
    }

    @Override
    public void init() throws ServletException {
    	userDAO = new UserDAOImpl();
    }
    
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String action = request.getPathInfo();
		switch (action) {
		case "/byId":
			// http://localhost:8080/mvc/test/byId
			//userDAO.getUserById(1);
			// userDAO.getUserByUsername("홍길동");
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

		default:
			break;
		}
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
	}
}
