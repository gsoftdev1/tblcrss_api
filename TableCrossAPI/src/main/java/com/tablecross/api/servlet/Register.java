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
import com.tablecross.api.object.RegisterResponse;
import com.tablecross.api.util.ConvertUtil;

public class Register extends HttpServlet {
	private static Logger log = Logger.getLogger(Register.class);

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
		String outStr = doRegister(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doRegister(HttpServletRequest req) {
		RegisterResponse response = new RegisterResponse();
		try {
			String email = req.getParameter("email");
			String password = SHA1Generator.encrypt(req
					.getParameter("password"));
			Integer refUserId = null;
			if (req.getParameter("refUserId") != null) {
				refUserId = Integer.parseInt(req.getParameter("refUserId"));
			}
			int areaId = Integer.parseInt(req.getParameter("areaId"));

			log.info("doRegister params email: " + email + "|password: "
					+ password + "|refUserId: " + refUserId + "|areaId: "
					+ areaId);

			if (email == null || email.equals("") || password == null
					|| password.equals("")) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_PARAMS_INVALID);
				response.setErrorMess(ConstantParams.ERROR_MESS_PARAMS_INVALID);
				return ConvertUtil.convertObjectToJson(response);
			}
			UsersDTO userDTO = UsersDAO.getUserByEmail(email);

			if (userDTO != null) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_EMAIL_IS_EXIST);
				response.setErrorMess(ConstantParams.ERROR_MESS_EMAIL_IS_EXIST);
				return ConvertUtil.convertObjectToJson(response);
			}
			UsersDTO refUser = UsersDAO.loadUser(refUserId);
			userDTO = new UsersDTO();
			userDTO.setEmail(email);
			userDTO.setPassword(password);
			userDTO.setAreaId(areaId);
			userDTO.setUserType(ConstantParams.LOGIN_TYPE_APP);
			if (refUser != null) {
				userDTO.setParentId(refUserId);
				userDTO.setLevel(refUser.getLevel() + 1);
			}
			Integer userId = UsersDAO.insert(userDTO);

			log.info("doRegister userId: " + userId);
			if (userId == null || userId == 0) {
				response.setErrorCode(ConstantParams.ERROR_CODE_SYSTEM_ERROR);
				response.setErrorMess(ConstantParams.ERROR_MESS_SYSTEM_ERROR);
				return ConvertUtil.convertObjectToJson(response);
			} else {
				userDTO.setId(userId);
			}
			response.setSuccess(true);
			response.setErrorCode(ConstantParams.ERROR_CODE_SUCCESS);
			response.setErrorMess(ConstantParams.ERROR_MESS_SUCCESS);
			return ConvertUtil.convertObjectToJson(response);
		} catch (NumberFormatException e) {
			response.setSuccess(false);
			response.setErrorCode(ConstantParams.ERROR_CODE_PARAMS_INVALID);
			response.setErrorMess(ConstantParams.ERROR_MESS_PARAMS_INVALID);
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
