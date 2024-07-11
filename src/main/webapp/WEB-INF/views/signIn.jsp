<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<!DOCTYPE html>
<html lang="ko">
<head>
    <meta charset="UTF-8">
    <title>로그인</title>
    <style>
        body {
            display: flex;
            justify-content: center;
            align-items: center;
            height: 100vh;
            margin: 0;
            font-family: 'Helvetica Neue', Arial, sans-serif;
            background: linear-gradient(120deg, #f6d365 0%, #fda085 100%);
        }
        .container {
            display: flex;
            flex-direction: column;
            justify-content: center;
            align-items: center;
            background-color: #ffffff;
            padding: 30px 40px;
            border-radius: 12px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
            animation: fadeIn 0.5s ease-in-out;
        }
        h1 {
            margin-bottom: 30px;
            color: #333333;
        }
        .form-group {
            display: flex;
            flex-direction: column;
            width: 100%;
            margin-bottom: 20px;
        }
        label {
            margin-bottom: 8px;
            color: #555555;
        }
        input[type="text"], input[type="password"] {
            padding: 12px;
            border: 1px solid #dddddd;
            border-radius: 8px;
            width: 100%;
            transition: border-color 0.3s;
        }
        input[type="text"]:focus, input[type="password"]:focus {
            border-color: #007bff;
            outline: none;
        }
        .btn {
            padding: 12px;
            border: none;
            border-radius: 8px;
            background-color: #007bff;
            color: #ffffff;
            font-size: 16px;
            cursor: pointer;
            width: 100%;
            transition: background-color 0.3s;
        }
        .btn:hover {
            background-color: #0056b3;
        }
        @keyframes fadeIn {
            from {
                opacity: 0;
                transform: scale(0.9);
            }
            to {
                opacity: 1;
                transform: scale(1);
            }
        }
    </style>
</head>
<body>
    <div class="container">
        <h1>로그인</h1>
        <form action="${pageContext.request.contextPath}/user/signIn" method="post">
            <div class="form-group">
                <label for="username">아이디</label>
                <input type="text" id="username" name="username" required>
            </div>
            <div class="form-group">
                <label for="password">비밀번호</label>
                <input type="password" id="password" name="password" required>
            </div>
            <button type="submit" class="btn">로그인</button>
        </form>
    </div>
</body>
</html>
<%-- 	<!-- http://localhost:8080/mvc/user/signIn -->
	<h2>로그인</h2>
	<!-- 회원가입 성공 메시지 출력 -->
	<%
	// String errorMessage = (String) request.getAttribute("message");
	// 쿼리 스트링에서 뽑아야 되는 메서드는 getParameter로 뽑아야 함.
	String success = (String) request.getParameter("message");
	if(success != null){
	%>
		<p style="color: blue"> <%=success%></p>
		
	<%	}  %>
	<!-- 절대 경로 사용해보기 -->
	<!-- 보안 -> post -->
	<form action="/mvc/user/signIn" method="post"> 
		<label for="username">사용자 이름 :</label>
		<input type="text" id="username" name="username" value="야스오1">
		<label for="password">비밀번호 :</label>
		<input type="password" id="password" name="password" value="1234">
		<button type="submit">로그인</button>
	</form>	 --%>