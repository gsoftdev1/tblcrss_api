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
import com.tablecross.api.dao.UsersDAO;
import com.tablecross.api.model.UsersDTO;
import com.tablecross.api.object.UpdateUserResponse;
import com.tablecross.api.util.ConvertUtil;
import com.tablecross.api.util.ValidateUtil;

public class UpdateUser extends HttpServlet {
	private static Logger log = Logger.getLogger(UpdateUser.class);

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
		String outStr = doUpdateUser(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doUpdateUser(HttpServletRequest req) {
		UpdateUserResponse response = new UpdateUserResponse();
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
			String email = req.getParameter("email");
			String mobile = req.getParameter("mobile");
			String birthday = req.getParameter("birthday");

			log.info("---doUpdateUser   email: " + email + "|mobile: " + mobile
					+ "|birthday: " + birthday);

			if (ValidateUtil.validateEmail(email)
					|| ValidateUtil.validateMobile(mobile)
					|| ValidateUtil.validateBirthday(birthday)) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_PARAMS_INVALID);
				response.setErrorMess(ConstantParams.ERROR_MESS_PARAMS_INVALID);
				return ConvertUtil.convertObjectToJson(response);
			}

			if (!email.equals(userDTO.getEmail())
					&& UsersDAO.getUserByEmail(email) != null) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_EMAIL_IS_EXIST);
				response.setErrorMess(ConstantParams.ERROR_MESS_EMAIL_IS_EXIST);
				return ConvertUtil.convertObjectToJson(response);
			}
			userDTO.setEmail(email);
			userDTO.setMobile(mobile);
			userDTO.setBirthday(ConvertUtil.parseFormatDateView(birthday));
			UsersDAO.updateUser(userDTO);

			session.setAttribute(ConstantParams.LOGIN_USER_INFO, userDTO);
			response.setSuccess(true);
			response.setErrorCode(ConstantParams.ERROR_CODE_SUCCESS);
			response.setErrorMess(ConstantParams.ERROR_MESS_SUCCESS);
			return ConvertUtil.convertObjectToJson(response);
		} catch (Exception e) {
			e.printStackTrace();
			response.setErrorCode(ConstantParams.ERROR_CODE_SYSTEM_ERROR);
			response.setErrorMess(ConstantParams.ERROR_MESS_SYSTEM_ERROR + ": "
					+ e.getMessage());
			return ConvertUtil.convertObjectToJson(response);
		}

	}
}
