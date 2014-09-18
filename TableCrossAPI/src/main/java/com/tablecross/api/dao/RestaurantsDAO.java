package com.tablecross.api.dao;

import java.math.BigDecimal;
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

public class RestaurantsDAO {
	private static Logger log = Logger.getLogger(RestaurantsDAO.class);

	public static List<RestaurantsDTO> searchRestaurant(int searchType,
			Integer userId, String searchKey, BigDecimal longitude,
			BigDecimal latitude, int total) throws Exception {
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
				sql = "select r.* from orders o "
						+ "inner join restaurants r on o.Restaurants_restaurants_id=r.restaurants_id "
						+ "where o.users_user_id="
						+ userId
						+ " group by r.restaurants_id order by o.orderdate desc limit "
						+ total;
				break;
			case ConstantParams.SEARCH_TYPE_DISTANCE:
				sql = "select * from restaurants order by restaurants_name limit "
						+ total;
				break;
			case ConstantParams.SEARCH_TYPE_KEY_WORD:
				sql = "select * from restaurants order by restaurants_name limit "
						+ total;
				break;
			default:
				sql = "select * from restaurants order by restaurants_name limit "
						+ total;
				break;
			}

			log.info(sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				RestaurantsDTO res = new RestaurantsDTO();
				// area.setAreaId(rs.getInt("area_id"));
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
