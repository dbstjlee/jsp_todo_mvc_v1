package com.tenco.controller;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

import com.tenco.model.UserDAO;
import com.tenco.model.UserDAOImpl;
import com.tenco.model.UserDTO;

// 주소 설계
// http://localhost:8080/mvc/user/
// http://localhost:8080/mvc/user/xxx 
// URL mapping -> /user/*로 받을 수 있음
// user 밑으로 들어오는 모든 경로를 받아라는 의미
@WebServlet("/user/*")
public class UserController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private UserDAO userDAO; // 포함관계

	public UserController() {
		super();
	}

	@Override
	public void init() throws ServletException {
		userDAO = new UserDAOImpl();
	} // 한번만 올라가도록 메모리에 올림.

	// GET 방식으로 들어올 때
	// http://localhost:8080/mvc/user/signUp -> 회원가입 페이지
	// http://localhost:8080/mvc/user/signIn -> 로그인 페이지
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		// path variable 받아내는 방법
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		switch (action) {
		case "/signIn":
			// 로그인 페이지로 보내는 동작 처리
			request.getRequestDispatcher("/WEB-INF/views/signIn.jsp").forward(request, response);
			break;
		case "/signUp":
			// 회원가입 페이지로 보내는 동작 처리
			request.getRequestDispatcher("/WEB-INF/views/signUp.jsp").forward(request, response);
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	// http://localhost:8080/mvc/views/todoForm.jsp -> servlet 거치지 않고 폴더로 바로 옴.
	// 보안 폴더 (URL로 응답 못 받음) => WEB-INF나 META-INF(둘 다 보안 폴더임) 하위로 폴더 옮기기

	// 로그인 기능 요청(자원에 요청 -- GET 방식 예외적인 처리_ 보안)
	// POST 요청 시 - 로그인 기능 구현, 회원 가입 기능 구현
	// POST -> 웹 브라우저의 주소창에 칠 수 없음.
	// Talent API, postman => 도구로 사용해야 함.

	// http://localhost:8080/mvc/user/signUp
	// http://localhost:8080/mvc/user/signIn
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		String action = request.getPathInfo();
		System.out.println("action : " + action);
		switch (action) {
		case "/signIn":
			signIn(request, response);
			break;
		case "/signUp":
			signUp(request, response); //ctrl + 1 : 메서드 생성
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
			break;
		}
	}

	/**
	 * 로그인 처리 기능
	 * @param request
	 * @param response
	 * @throws IOException 
	 */
	private void signIn(HttpServletRequest request, HttpServletResponse response) throws IOException {
		// URL mapping, 인증 검사, 유효성 검사, 서비스 로직, DAO에게 전달 역할, view 호출
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		
		// 유효성 검사
		// 이름과 비밀번호가 null이면 로그인 페이지로 던짐.
		// .trim().isEmpty(): 공백이 있는 문자열도 빈문자열로 인식
		if(username == null || password.trim().isEmpty()) {
			response.sendRedirect("signIn?message=invalid");
			return;
		}
		
		UserDTO user =  userDAO.getUserByUsername(username);// username으로 유저 확인
		// 빠른 평가
		if(user != null && user.getPassword().equals(password)) {
			// 사용자의 세션 메모리에 로그인 정보를 넣음(세션 기반 인증 처리)
			HttpSession session = request.getSession();
			session.setAttribute("principal", user); // key 값(principal)은 임의로 설정, 사용자 정보(user) 통으로 넣음.
			// 로그인 시 session 생성
			// DB에 있는 데이터까지 전부 들어가 있음.
			
			// 로그인 성공 --> 자동으로 todoFrom 화면으로 이동 처리
			response.sendRedirect("/mvc/todo/todoForm");
			System.out.println("로그인 처리 완료");
		} else {
			response.sendRedirect("signIn?message=invalid"); // 예외 처리
		}
		
		// null <-- 회원가입 X(등록되지 않은 사람)
		// 비밀번호 == dto.getPassword(); (password 틀림)
	}

	/**
	 * 회원 가입 기능
	 * @param request
	 * @param response
	 * @throws IOException 
	 * @throws ServletException 
	 */
	private void signUp(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		
		// 인증 검사가 필요 없는 기능(회원가입)
		
		String username = request.getParameter("username");
		String password = request.getParameter("password");
		String email = request.getParameter("email");
		
		// 방어적 코드 작성(username 확인)
		if(username == null || username.trim().isEmpty()) {
			request.setAttribute("errorMessage", "사용자 이름을 입력하시오.");
			request.getRequestDispatcher("/WEB-INF/views/signUp.jsp").forward(request, response);
			return;
		}
		
		// 방어적 코드 작성 (password 확인) - 생략
		// 방어적 코드 작성 (email 확인) - 생략
		
		UserDTO userDTO = UserDTO.builder()
				.username(username)
				.password(password)
				.email(email)
				.build();
		
		//int resultRowCount = 0;
		int resultRowCount = userDAO.addUser(userDTO);
		System.out.println("resultRowCount : " + resultRowCount);
		if(resultRowCount == 1) {
			response.sendRedirect("signIn?message=success");
		} else {
			// 절대경로
			//response.sendRedirect("/mvc/user/signIn?message=error");
			// 상대경로
			// get 방식으로 돌아옴
			response.sendRedirect("signIn?message=error");
		}
		
		// result.jsp?message
		
	}

}
