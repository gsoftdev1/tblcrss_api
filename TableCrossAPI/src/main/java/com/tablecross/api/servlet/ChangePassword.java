package com.tablecross.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tablecross.api.common.ConstantParams;
import com.tablecross.api.common.SHA1Generator;
import com.tablecross.api.dao.UsersDAO;
import com.tablecross.api.model.UsersDTO;
import com.tablecross.api.object.ChangePasswordResponse;
import com.tablecross.api.util.ConvertUtil;

public class ChangePassword extends HttpServlet {
	private static Logger log = Logger.getLogger(ChangePassword.class);

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		doPost(req, resp);

	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException {
		// TODO Auto-generated method stub
		String outStr = doChangePassword(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doChangePassword(HttpServletRequest req) {
		ChangePasswordResponse response = new ChangePasswordResponse();
		try {
			HttpSession session = req.getSession(true);

			UsersDTO userDTO = (UsersDTO) session
					.getAttribute(ConstantParams.LOGIN_USER_INFO);

			if (userDTO == null) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_USER_IS_NOT_LOGIN);
				response.setErrorMess(ConstantParams.ERROR_MESS_USER_IS_NOT_LOGIN);
				return ConvertUtil.convertObjectToJson(response);
			}
			String oldPassword = SHA1Generator.encrypt(req
					.getParameter("oldPassword"));
			String newPassword = SHA1Generator.encrypt(req
					.getParameter("newPassword"));

			log.info("---doChangePassword   oldPassword: " + oldPassword
					+ "|newPassword: " + newPassword);

			if (!userDTO.getPassword().equals(oldPassword)) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_WRONG_PASSWORD);
				response.setErrorMess(ConstantParams.ERROR_MESS_WRONG_PASSWORD);
				return ConvertUtil.convertObjectToJson(response);
			}

			if (newPassword.equals("")) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_NEW_PASSWORD_INVALID);
				response.setErrorMess(ConstantParams.ERROR_MESS_NEW_PASSWORD_INVALID);
				return ConvertUtil.convertObjectToJson(response);
			}
			UsersDAO.changePassword(userDTO.getId(), newPassword);
			userDTO.setPassword(newPassword);
			session.setAttribute(ConstantParams.LOGIN_USER_INFO, userDTO);
			response.setSuccess(true);
			response.setErrorCode(ConstantParams.ERROR_CODE_SUCCESS);
			response.setErrorMess(ConstantParams.ERROR_MESS_SUCCESS);
			return ConvertUtil.convertObjectToJson(response);
		} catch (NumberFormatException e) {
			response.setSuccess(false);
			response.setErrorCode(ConstantParams.ERROR_CODE_USER_ID_NOT_EXIST);
			response.setErrorMess(ConstantParams.ERROR_MESS_USER_ID_NOT_EXIST);
			return ConvertUtil.convertObjectToJson(response);
		} catch (Exception e) {
			log.error("ERROR: ", e);
			response.setSuccess(false);
			response.setErrorCode(ConstantParams.ERROR_CODE_SYSTEM_ERROR);
			response.setErrorMess(ConstantParams.ERROR_MESS_SYSTEM_ERROR + ": "
					+ e.getMessage());
			return ConvertUtil.convertObjectToJson(response);
		}

	}
}
