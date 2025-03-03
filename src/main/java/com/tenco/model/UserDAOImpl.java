package com.tenco.model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.sql.DataSource;

import com.tenco.utils.DBUtil;

public class UserDAOImpl implements UserDAO {

	// HikariCP(connection pool)의 추상적 개념
	private DataSource dataSource;

	public UserDAOImpl() {
		try {
			InitialContext ctx = new InitialContext();
			dataSource = (DataSource) ctx.lookup("java:comp/env/jdbc/MyDB");
			// dataSource = (DataSource) DBUtil.getConnection();
			// 메모리에 올라갈 때 여기에 connection 객체가 담김.
		} catch (NamingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public int addUser(UserDTO userDTO) {

		// DB에 넣는거 작성
		int resultCount = 0;
		String sql = " INSERT INTO users(username, password, email) VALUES (?, ?, ?) ";

		try (Connection conn = dataSource.getConnection()) {
			// 트랜잭션 시작
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, userDTO.getUsername());
				pstmt.setString(2, userDTO.getPassword());
				pstmt.setString(3, userDTO.getEmail());
				resultCount = pstmt.executeUpdate();

				// 트랜잭션 커밋
				conn.commit();

			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			} // end of PreparedStatement

		} catch (Exception e) {
			e.printStackTrace();
		} // end of Connection
		return resultCount;
	}

	/**
	 * select 에서 트랜잭션 처리 안 함. but 정합성 때문에 해야하기도 함. 
	 * 왜 select 구문에 트랜잭션을 해야 하는가? 
	 * 1. 임시 데이터 뽑음 2. update 3. select -> insert 
	 * ==> 하나의 트랜잭션으로 묶을 수 있음. 
	 * ==> 중간에 update 해서 값이 바뀌면 데이터가 꼬일 수 있음. 
	 * ==> 잠깐 막아줘야 함.(트랜잭션 걸어야 함.)
	 */

	/**
	 * SELECT에서는 일단 트랜잭션 처리를 하지 말자. 하지만 팬텀리드현상
	 * (정합성을 위해서 처리하는 것도 옳은 방법이다.)
	 */

	@Override
	public UserDTO getUserById(int id) {

		String sql = " select * from users where id = ? ";
		UserDTO userDTO = null;

		try (Connection conn = dataSource.getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setInt(1, id);
				ResultSet rs = pstmt.executeQuery();
				// 단일행 ...다중행이면 while문 사용
				if (rs.next()) {
					userDTO = new UserDTO();
					userDTO.setId(rs.getInt("id")); // 데이터 추출, 파싱
					userDTO.setUsername(rs.getString("username"));
					userDTO.setPassword(rs.getString("password"));
					userDTO.setEmail(rs.getString("email"));
					userDTO.setCreatedAt(rs.getString("created_at"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("UserDTO : " + userDTO.toString());
		return userDTO;
	}

	@Override
	public UserDTO getUserByUsername(String username) {
		String sql = " select * from users where username = ? ";
		UserDTO userDTO = null;

		try (Connection conn = dataSource.getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				pstmt.setString(1, username);
				ResultSet rs = pstmt.executeQuery();
				if (rs.next()) {
					userDTO = new UserDTO();
					userDTO.setId(rs.getInt("id")); // 데이터 추출, 파싱
					userDTO.setUsername(rs.getString("username"));
					userDTO.setPassword(rs.getString("password"));
					userDTO.setEmail(rs.getString("email"));
					userDTO.setCreatedAt(rs.getString("created_at"));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// TODO - 삭제 예정
			System.out.println("UserDTO By Username : " + userDTO.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
		return userDTO;
	}

	@Override
	public List<UserDTO> getAllUsers() {
		String sql = " select * from users ";
		// 자료구조를 사용할 때 일단 생성 시키자.
		// 메모리에 올라가 있어야 넣을 수 있기 때문에 메모리 공간을 띄움.
		List<UserDTO> list = new ArrayList<>();

		try (Connection conn = dataSource.getConnection()) {
			try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
				ResultSet rs = pstmt.executeQuery();
				while (rs.next()) {
					UserDTO userDTO = new UserDTO();
					userDTO.setId(rs.getInt("id")); // 데이터 추출, 파싱
					userDTO.setUsername(rs.getString("username"));
					userDTO.setPassword(rs.getString("password"));
					userDTO.setEmail(rs.getString("email"));
					userDTO.setCreatedAt(rs.getString("created_at"));
					list.add(userDTO);
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.out.println("UserList All: " + list.toString());
		return list;
	}

	@Override
	public int updateUser(UserDTO user, int principalId) {
		int rowCount = 0;
		String sql = " UPDATE users SET password = ?, email = ? WHERE id = ? ";
		try (Connection conn = dataSource.getConnection()){
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setString(1, user.getPassword());
				pstmt.setString(2, user.getEmail());
				pstmt.setInt(3, principalId);
				rowCount = pstmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowCount;
	}

	@Override
	public int deleteUser(int id) {
		
		int rowCount = 0;
		String sql = " DELETE FROM users WHERE id = ? ";
		try (Connection conn = dataSource.getConnection()){
			conn.setAutoCommit(false);
			try (PreparedStatement pstmt = conn.prepareStatement(sql)){
				pstmt.setInt(1, id);
				rowCount = pstmt.executeUpdate();
				conn.commit();
			} catch (Exception e) {
				conn.rollback();
				e.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return rowCount;
	}
}
