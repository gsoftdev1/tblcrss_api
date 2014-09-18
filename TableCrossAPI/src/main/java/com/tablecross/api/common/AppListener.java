package com.tablecross.api.common;

import java.io.FileInputStream;
import java.util.Properties;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import org.apache.log4j.Logger;

import com.tablecross.api.util.IoUtils;

public class AppListener implements ServletContextListener {
	private static Logger log = Logger.getLogger(AppListener.class);
	public static String rootpath = "";
	public static String url;
	public static String username;
	public static String password;

	public void contextDestroyed(ServletContextEvent arg0) {
		try {
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void contextInitialized(ServletContextEvent arg0) {
		String rootPath = arg0.getServletContext().getRealPath("/");
		rootpath = rootPath;
		log.info("rootpath: " + rootpath);
		FileInputStream fis = null;
		try {
			fis = new FileInputStream(rootPath + "WEB-INF/config.properties");
			Properties prop = new Properties();
			prop.load(fis);
			// config DB
			url = prop.getProperty("database.url");
			username = prop.getProperty("database.username");
			password = prop.getProperty("database.password");

			log.info("url:" + url + "|username: " + username + "|password: "
					+ password);

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			IoUtils.closeFile(fis);
		}

	}
}
