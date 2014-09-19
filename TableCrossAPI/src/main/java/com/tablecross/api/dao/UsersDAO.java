package com.tablecross.api.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.tablecross.api.common.SHA1Generator;
import com.tablecross.api.model.ConnectionDataBase;
import com.tablecross.api.model.UsersDTO;
import com.tablecross.api.util.ConvertUtil;

public class UsersDAO {
	private static Logger log = Logger.getLogger(UsersDAO.class);

	public static Integer insert(UsersDTO user) throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		Integer userId = 0;
		if (conn == null) {
			log.error("Can not connect to Database");
			return 0;
		}
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		String dateNow = ConvertUtil.getFormatDateInsert(new Date());
		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO users (parent_id, level, email, password, mobile, createby, createddate, point, user_type, ordercount, notify_order, notify_restaurant, "
					+ "notify_beforedate, birthday, Area_area_id) "
					+ "	VALUES("
					+ user.getParentId()
					+ ","
					+ (user.getLevel() == null ? 0 : user.getLevel())
					+ ",'"
					+ user.getEmail()
					+ "','"
					+ (user.getPassword() == null ? SHA1Generator
							.encrypt("123456") : user.getPassword())
					+ "','"
					+ (user.getMobile() == null ? "" : user.getMobile())
					+ "','"
					+ user.getCreateBy()
					+ "','"
					+ dateNow
					+ "',"
					+ (user.getPoint() == null ? 0 : user.getPoint())
					+ ","
					+ user.getUserType()
					+ ","
					+ (user.getOrderCount() == null ? 0 : user.getOrderCount())
					+ ","
					+ (user.getNotifyOrder() == null ? 0 : user
							.getNotifyOrder())
					+ ","
					+ (user.getNotifyRestaurant() == null ? 0 : user
							.getNotifyRestaurant())
					+ ","
					+ (user.getNotifyBeforeDate() == null ? 0 : user
							.getNotifyBeforeDate())
					+ ",'"
					+ (user.getBirthday() == null ? dateNow : ConvertUtil
							.getFormatDateInsert(user.getBirthday()))
					+ "',"
					+ user.getAreaId() + ")";
			log.info(sql);
			st = conn.createStatement();
			st.addBatch(sql);
			st.executeBatch();
			String sqlGetUserId = "select LAST_INSERT_ID() as user_id from users";
			log.info(sqlGetUserId);
			st = conn.createStatement();
			rs = st.executeQuery(sqlGetUserId);
			while (rs.next()) {
				userId = rs.getInt("user_id");
			}
			conn.commit();
		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while insert user: " + re.toString());
			log.error("Failed SQL statement:  " + sql);
			throw re;
		} finally {
			if (conn != null)
				ConnectionDataBase.closeConnection(conn);
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}
		return userId;

	}

	public static UsersDTO loadUser(Integer userId) throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		if (conn == null) {
			log.error("Can not connect to Database");
			return null;
		}
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		UsersDTO usersDTO = null;
		try {
			conn.setAutoCommit(false);
			sql = "select * from users where user_id=" + userId;
			log.info(sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				usersDTO = new UsersDTO();
				usersDTO.setId(rs.getInt("user_id"));
				usersDTO.setEmail(rs.getString("email"));
				usersDTO.setPassword(rs.getString("password"));
				usersDTO.setMobile(rs.getString("mobile"));
				usersDTO.setPoint(rs.getBigDecimal("point"));
				usersDTO.setUserType(rs.getInt("user_type"));
				usersDTO.setLevel(rs.getInt("level"));
				usersDTO.setOrderCount(rs.getLong("ordercount"));
				usersDTO.setBirthday(rs.getDate("birthday"));
			}

		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while getUserByEmail: " + re.toString());
			log.error("Failed SQL statement:  " + sql);
			throw re;
		} finally {
			if (conn != null)
				ConnectionDataBase.closeConnection(conn);
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return usersDTO;

	}

	public static UsersDTO getUserByEmail(String email) throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		if (conn == null) {
			log.error("Can not connect to Database");
			return null;
		}
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		UsersDTO usersDTO = null;
		try {
			conn.setAutoCommit(false);
			sql = "select * from users where email like '" + email + "'";
			log.info(sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				usersDTO = new UsersDTO();
				usersDTO.setId(rs.getInt("user_id"));
				usersDTO.setEmail(rs.getString("email"));
				usersDTO.setPassword(rs.getString("password"));
				usersDTO.setMobile(rs.getString("mobile"));
				usersDTO.setPoint(rs.getBigDecimal("point"));
				usersDTO.setUserType(rs.getInt("user_type"));
			}

		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while getUserByEmail: " + re.toString());
			log.error("Failed SQL statement:  " + sql);
			throw re;
		} finally {
			if (conn != null)
				ConnectionDataBase.closeConnection(conn);
			if (rs != null)
				try {
					rs.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

		return usersDTO;

	}

	public static void changePassword(long userId, String newPassword)
			throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		if (conn == null) {
			log.error("Can not connect to Database");
			return;
		}
		Statement st = null;
		String sql = null;
		try {
			conn.setAutoCommit(false);
			sql = "UPDATE users SET password='" + newPassword
					+ "'  WHERE user_id=" + userId;
			log.info(sql);
			st = conn.createStatement();
			st.addBatch(sql);
			st.executeBatch();
			conn.commit();
		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while changePassword: " + re.toString());
			log.error("Failed SQL statement:  " + sql);
			throw re;
		} finally {
			if (conn != null)
				ConnectionDataBase.closeConnection(conn);
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}

	public static void updateUser(UsersDTO userNew) throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		if (conn == null) {
			log.error("Can not connect to Database");
			return;
		}
		Statement st = null;
		String sql = null;
		try {
			conn.setAutoCommit(false);
			sql = "UPDATE users SET email='" + userNew.getEmail()
					+ "',mobile='" + userNew.getMobile() + "',birthday='"
					+ ConvertUtil.getFormatDateInsert(userNew.getBirthday())
					+ "'  WHERE user_id=" + userNew.getId();
			log.info(sql);
			st = conn.createStatement();
			st.addBatch(sql);
			st.executeBatch();
			conn.commit();
		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while updateUser: " + re.toString());
			log.error("Failed SQL statement:  " + sql);
			throw re;
		} finally {
			if (conn != null)
				ConnectionDataBase.closeConnection(conn);
			if (st != null)
				try {
					st.close();
				} catch (SQLException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
		}

	}
}
