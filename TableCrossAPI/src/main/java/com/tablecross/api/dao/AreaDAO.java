package com.tablecross.api.dao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.tablecross.api.model.AreaDTO;
import com.tablecross.api.model.ConnectionDataBase;

public class AreaDAO {
	private static Logger log = Logger.getLogger(AreaDAO.class);

	public static List<AreaDTO> getAreas() throws Exception {
		Connection conn = ConnectionDataBase.getConnection();
		if (conn == null) {
			log.error("Can not connect to Database");
			return null;
		}
		Statement st = null;
		String sql = null;
		ResultSet rs = null;
		List<AreaDTO> lst = new ArrayList<AreaDTO>();
		try {
			conn.setAutoCommit(false);
			sql = "select * from area order by area_name";
			log.info(sql);
			st = conn.createStatement();
			rs = st.executeQuery(sql);
			while (rs.next()) {
				AreaDTO area = new AreaDTO();
				area.setAreaId(rs.getInt("area_id"));
				area.setAreaName(rs.getString("area_name"));
				lst.add(area);
			}

		} catch (Exception re) {
			try {
				conn.rollback();
			} catch (SQLException e) {
				log.error("Rollback exception: ", e);
			}
			log.error("SQLException while getAreas: " + re.toString());
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
