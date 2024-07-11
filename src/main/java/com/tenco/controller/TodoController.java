package com.tenco.controller;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import com.tenco.model.TodoDAO;
import com.tenco.model.TodoDAOImpl;
import com.tenco.model.TodoDTO;
import com.tenco.model.UserDTO;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

// .../mvc/todo/xxx
@WebServlet("/todo/*")
public class TodoController extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private TodoDAO todoDAO;

	public TodoController() {
		todoDAO = new TodoDAOImpl(); // 업캐스팅
	}

	// http://localhost:8080/mvc/todo/todoForm
	// http://localhost:8080/mvc/todo/list
	// http://localhost:8080/mvc/todo/detail
	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String action = request.getPathInfo();

		// 로그인한 사용자만 접근을 허용하도록 설계
		HttpSession session = request.getSession();
		// JsessionID + principal 활용 예정
		UserDTO principal = (UserDTO) session.getAttribute("principal"); // session에 존재하는 object를 끄집어냄.
		// 로그인 할 때 session이 생성됨(setAttribute)
		// principal key 값에는 user 정보가 전부 담겨 있음.
		// UserDTO에 담겨서(반환되서) principal에 담김.

		// 인증 검사
		if (principal == null) {
			// 로그인을 안한 상태 -> 로그인 페이지로 돌려보냄.
			response.sendRedirect("/mvc/user/signIn?message=invalid");// 예외 처리
			return;
		}

		System.out.println("action : " + action);
		switch (action) {
		case "/todoForm":
			todoFormPage(request, response);
			break;
		case "/list":
			// user의 id를 받아와야 하기 때문
			todoListPage(request, response, principal.getId());
			break;
		case "/detail":
			todoDetailPage(request, response, principal.getId());
			break;
		case "/delete":
			deleteTodo(request, response, principal.getId());
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404 던짐
			break;
		}
	}

	/**
	 * todo 작성 페이지 이동
	 * 
	 * @param request
	 * @param response
	 * @throws IOException
	 * @throws ServletException
	 */
	private void todoFormPage(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ServletException {
		request.getRequestDispatcher("/WEB-INF/views/todoForm.jsp").forward(request, response); // 예외 처리
	}

	// 필터 : WAS 에 들어오기 전에 정상적인 사용자인지 걸러내는 것

	/**
	 * 사용자별 todo 리스트 화면 이동
	 * 
	 * @param request
	 * @param response
	 * @param principalId
	 * @throws IOException
	 * @throws ServletException
	 */

	private void todoListPage(HttpServletRequest request, HttpServletResponse response, int principalId)
			throws IOException, ServletException {

		// request.getPathInfo() --> URL 요청에 올 때 데이터 추출
		// request.getParameter() --> URL 요청에 올 때 데이터 추출
		// request.getAttribute() --> 뷰를 내릴 속성에 값을 뽑아서 뷰로 내릴 때

		List<TodoDTO> list = todoDAO.getTodosByUserId(principalId);
		request.setAttribute("list", list);// list 값을 넣어줌.
		// DB에서 조회해서 데이터를 담아서 던질 예정

		// todoList.jsp 페이지로 내부에서 이동 처리
		request.getRequestDispatcher("/WEB-INF/views/todoList.jsp").forward(request, response);

	}

	/**
	 * 상세보기 화면
	 * 
	 * @param request
	 * @param response
	 * List에서 상세 페이지로 넘어옴.
	 * @throws IOException 
	 */
	// http://localhost:8080/mvc/todo/detail?id=2;
	private void todoDetailPage(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		// detail?id=1
		try {
			
			// todo - PK (여러개) => 1, 3, 5(야스오)
			// todo - PK => 2, 4, 6(홍길동)
			int todoId = Integer.parseInt(request.getParameter("id")); // "id" 값을 int로 변경하여 todoId에 담음
			// 해당하는 todo 값을 select 한 후에 올려야 함.
			TodoDTO dto = todoDAO.getTodoById(todoId); 
			// todoDAO의 getTodoById 메서드에 int로 변경된 id 값이 담긴 todoId를 넣음
			// todoId를 넣음 (= where 문으로 필터링 된 select 문이 돌아가서 모든 정보를 출력해서 
			// TodoDTO에 담긴(반환한) 값이 dto에 담김.
			
			// dto에 담긴 값인 UserId와 user 값을 가지고 있는 principalId 값이 동일할 때
			if(dto.getUserId() == principalId) { 
				// 상세보기 화면으로 이동 처리
				
				// 뿌릴 데이터가 필요함.(dto를 담아서 뿌려줌)
				request.setAttribute("todo", dto);
				request.getRequestDispatcher("/WEB-INF/views/todoDetail.jsp").forward(request, response);
			} else {
				// 권한이 없습니다. or 잘못된 접근입니다. 
				response.setContentType("text/html;charset=UTF-8");
				PrintWriter out = response.getWriter();
				out.println("<script> alert('권한이 없습니다.'); history.back(); </script>"); // 뒤로 돌아가기
				}
			
			// get 방식은 스택이 아래로 점점 쌓여서 뒤로가기 하면 뒤로 가짐. (하지만 post는 안 됨)
			
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/todo/list?error=invalid");
		}
	}

	/**
	 * todo 삭제 기능
	 * @param request
	 * @param response
	 * @param principalId
	 * @throws IOException
	 */
	private void deleteTodo(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {

		try {
			int todoId = Integer.parseInt(request.getParameter("id"));
			todoDAO.deleteTodo(todoId, principalId);
		} catch (Exception e) {
			response.sendRedirect(request.getContextPath() + "/todo/list?error=invalid");
		}
		response.sendRedirect(request.getContextPath() + "/todo/list");
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		// TODO - 인증검사 추후 추가 처리
		HttpSession session = request.getSession();
		UserDTO principal = (UserDTO) session.getAttribute("principal");

		// principal == null 이라면 -> 로그인 페이지로 이동 처리
		if (principal == null) {
			response.sendRedirect(request.getContextPath() + "/user/signIn?error=invalid");
			return;
		}

		String action = request.getPathInfo();
		System.out.println("action : " + action);
		switch (action) {
		case "/add":
			addTodo(request, response, principal.getId());
			break; // 없으면 밑에 코드 바로 실행됨.
		case "/update":
			updateTodo(request, response, principal.getId());
			break;
		default:
			response.sendError(HttpServletResponse.SC_NOT_FOUND); // 404 던짐
			break;
		}
	}

	/**
	 * 세션별 사용자 todo 등록
	 * 
	 * @param request
	 * @param response
	 * @param principalId : 세션에 담겨 있는 UserId 값
	 * @throws IOException
	 */
	private void addTodo(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		String title = request.getParameter("title");
		String description = request.getParameter("description");
		String dueDate = request.getParameter("dueDate");

		// checkbox는 여러개 선택 가능한 태그 : String[] 배열로 선언했음
		// 이번에 checkbox는 하나만 사용중
		// 체크박스가 선택되지 않았으면 null을 반환하고 체크가 되어있다면 on(문자열)으로 넘어 온다.
		boolean completed = "on".equalsIgnoreCase(request.getParameter("completed"));// on 과 같다면 true

		TodoDTO dto = TodoDTO
				.builder()
				.userId(principalId)
				.title(title)
				.description(description)
				.dueDate(dueDate)
				.completed(String.valueOf(completed))
				.build();
		todoDAO.addTodo(dto, principalId);
		response.sendRedirect(request.getContextPath() + "/todo/list");
	}
	/**
	 * todo 수정 기능
	 * @param request
	 * @param response
	 * @param principalId - 세션 ID 값
	 * @throws IOException
	 */
	
	private void updateTodo(HttpServletRequest request, HttpServletResponse response, int principalId) throws IOException {
		try {
			int todoId = Integer.parseInt(request.getParameter("id"));
			String title = request.getParameter("title");
			String description = request.getParameter("description");
			String dueDate = request.getParameter("dueDate");
			System.out.println("33333 : " + request.getParameter("completed"));
			boolean completed = "on".equalsIgnoreCase(request.getParameter("completed"));// on 과 같다면 true
			System.out.println("completed : " + completed);
			TodoDTO dto = TodoDTO
					.builder()
					.id(todoId)
					.userId(principalId)
					.title(title)
					.description(description)
					.dueDate(dueDate)
					.completed(String.valueOf(completed)).build();
			
			todoDAO.updateTodo(dto, principalId);
			
			System.out.println("todoId :" + todoId);
		} catch (Exception e) {
			response.setContentType("text/html;charset=UTF-8");
			PrintWriter out = response.getWriter();
			out.println("<script> alert('잘못된 요청입니다.'); history.back(); </script>"); 
		}
		response.sendRedirect(request.getContextPath() + "/todo/list");
	}

}
