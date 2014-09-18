package com.tablecross.api.model;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import snaq.db.ConnectionPool;

import com.tablecross.api.common.AppListener;

public class ConnectionDataBase {
	private static ConnectionPool pool = null;
	private static final Logger log = Logger.getLogger(ConnectionDataBase.class);

	private static void initPool() {

		try {
			Class c = Class.forName("com.mysql.jdbc.Driver");
			Driver driver = (Driver) c.newInstance();
			DriverManager.registerDriver(driver);
			pool = new ConnectionPool("pool", 1, 5, 100, 10000,
					AppListener.url, AppListener.username, AppListener.password);

		} catch (ClassNotFoundException e) {
			log.error("initPool() - " + e);
		} catch (SQLException e) {
			log.error("initPool() - " + e);
		} catch (InstantiationException e) {
			log.error("initPool() - " + e);
		} catch (IllegalAccessException e) {
			log.error("initPool() - " + e);
		} catch (Exception e) {
			log.error("initPool() - " + e);
		}
	}

	public static Connection getConnection() {
		if (pool == null || pool.isReleased()) {
			initPool();
		}
		try {
			return pool.getConnection(10000); // 10s timeout
		} catch (Exception e) {
			log.error("getConnection(): " + e);
		}
		return null;
	}

	public static void closeConnection(Connection conn) {
		try {
			if (conn != null)
				conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
}
