package com.tablecross.api.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Date;

import org.apache.log4j.Logger;

import com.tablecross.api.model.ConnectionDataBase;
import com.tablecross.api.model.OrdersDTO;
import com.tablecross.api.util.ConvertUtil;

public class OrdersDAO {
	private static Logger log = Logger.getLogger(OrdersDAO.class);

	public static Integer insert(OrdersDTO order) throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		Integer userId = 0;
		if (conn == null) {
			log.error("Can not connect to Database");
			return 0;
		}
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		try {
			conn.setAutoCommit(false);
			sql = "INSERT INTO orders(users_user_id, Restaurants_restaurants_id,quantity, createdby, createddate, orderdate, status) VALUES("
					+ order.getUserId()
					+ ","
					+ order.getRestaurantId()
					+ ","
					+ order.getQuantity()
					+ ",'"
					+ order.getCreatedBy()
					+ "','"
					+ ConvertUtil.getFormatDateInsert(new Date())
					+ "','"
					+ ConvertUtil.getFormatDateInsert(order.getOrderDate())
					+ "'," + order.getStatus() + ")";
			log.info(sql);
			st = conn.createStatement();
			st.addBatch(sql);
			st.executeBatch();
			String sqlGetUserId = "select LAST_INSERT_ID() as orders_id from orders";
			log.info(sqlGetUserId);
			st = conn.createStatement();
			rs = st.executeQuery(sqlGetUserId);
			while (rs.next()) {
				userId = rs.getInt("orders_id");
			}
			conn.commit();
		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while insert order: " + re.toString());
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
}
