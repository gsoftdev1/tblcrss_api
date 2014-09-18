package com.tablecross.api.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tablecross.api.common.ConstantParams;
import com.tablecross.api.model.ConnectionDataBase;
import com.tablecross.api.model.RestaurantsDTO;
import com.tablecross.api.util.ConvertUtil;

public class RestaurantsDAO {
	private static Logger log = Logger.getLogger(RestaurantsDAO.class);

	public static List<RestaurantsDTO> searchRestaurant(int searchType,
			Integer userId, String searchKey, Double longitude,
			Double latitude, Float distance, int total) throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		if (conn == null) {
			log.error("Can not connect to Database");
			return null;
		}
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		List<RestaurantsDTO> lst = new ArrayList<RestaurantsDTO>();
		try {
			conn.setAutoCommit(false);
			switch (searchType) {
			case ConstantParams.SEARCH_TYPE_HISTORY:
				sql = "select r.*,o.orderdate from orders o "
						+ "inner join restaurants r on o.Restaurants_restaurants_id=r.restaurants_id "
						+ "where o.users_user_id="
						+ userId
						+ " group by r.restaurants_id order by o.orderdate desc";
				if (total != -1) {
					sql = sql + " limit " + total;
				}
				break;
			case ConstantParams.SEARCH_TYPE_DISTANCE:
				sql = "SELECT r.*, (6371 * ACOS(COS( radians("
						+ latitude
						+ ") )* COS( radians( r.latitude ) )* COS( radians( r.longitude ) - radians("
						+ longitude + ") )+ SIN( radians(" + latitude
						+ ") )* SIN( radians( r.latitude ) ))) AS distance "
						+ "FROM restaurants r HAVING distance <=" + distance;
				if (searchKey != null && !searchKey.equals("")) {
					sql = sql + " and (r.restaurants_name like '%" + searchKey
							+ "%' or r.address like '%" + searchKey + "%')";
				}
				sql = sql + " ORDER BY distance";
				if (total != -1) {
					sql = sql + " limit " + total;
				}
				break;
			case ConstantParams.SEARCH_TYPE_KEY_WORD:
				sql = "SELECT r.* FROM restaurants r WHERE true";
				if (searchKey != null && !searchKey.equals("")) {
					sql = sql + " and (r.restaurants_name like '%" + searchKey
							+ "%' or r.address like '%" + searchKey + "%')";
				}
				sql = sql + " ORDER BY r.restaurants_name";
				if (total != -1) {
					sql = sql + " limit " + total;
				}
				break;
			default:
				sql = "SELECT r.* FROM restaurants r WHERE true";
				if (searchKey != null && !searchKey.equals("")) {
					sql = sql + " and (r.restaurants_name like '%" + searchKey
							+ "%' or r.address like '%" + searchKey + "%')";
				}
				sql = sql + " ORDER BY r.restaurants_name";
				if (total != -1) {
					sql = sql + " limit " + total;
				}
				break;
			}

			log.info(sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				RestaurantsDTO res = new RestaurantsDTO();
				res.setRestaurantId(rs.getInt("restaurants_id"));
				res.setRestaurantName(rs.getString("restaurants_name"));
				res.setAddress(rs.getString("address"));
				res.setImageUrl(rs.getString("image"));
				res.setWebsite(rs.getString("website"));
				res.setLongitude(rs.getDouble("longitude"));
				res.setLatitude(rs.getDouble("latitude"));
				if (searchType == ConstantParams.SEARCH_TYPE_HISTORY) {
					res.setOrderDate(ConvertUtil.getFormatDateView(rs
							.getDate("orderdate")));
				}
				lst.add(res);
			}

		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while searchRestaurant: " + re.toString());
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

		return lst;

	}
}
