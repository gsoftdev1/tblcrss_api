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
import com.tablecross.api.object.LoginResponse;
import com.tablecross.api.util.ConvertUtil;

public class Login extends HttpServlet {
	private static Logger log = Logger.getLogger(Login.class);

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
		String outStr = doLogin(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doLogin(HttpServletRequest req) {
		LoginResponse response = new LoginResponse();
		try {
			HttpSession session = req.getSession(true);
			UsersDTO userDTO = (UsersDTO) session
					.getAttribute(ConstantParams.LOGIN_USER_INFO);
			String email = req.getParameter("email");
			int loginType = Integer.parseInt(req.getParameter("loginType"));
			int areaId = 0;
			if (req.getParameter("areaId") != null
					&& !req.getParameter("areaId").isEmpty()) {
				areaId = Integer.parseInt(req.getParameter("areaId"));
			}
			String password = SHA1Generator.encrypt(req
					.getParameter("password"));
			log.info("---doLogin   email: " + email + "|password: " + password);

			if (userDTO != null) {
				log.info("user is login ! userDTO.getEmail(): "
						+ userDTO.getEmail() + "|userDTO.getPassword(): "
						+ userDTO.getPassword());
				if (userDTO.getEmail() != null
						&& userDTO.getEmail().equals(email)
						&& ((userDTO.getPassword() != null && userDTO
								.getPassword().equals(password)) || loginType != ConstantParams.LOGIN_TYPE_APP)) {
					response.setSuccess(true);
					response.setEmail(email);
					response.setMobile(userDTO.getMobile() == null ? ""
							: userDTO.getMobile());
					response.setPoint(String.valueOf(userDTO.getPoint() == null ? 0
							: userDTO.getPoint()));
					response.setUserId(userDTO.getId());
					response.setSessionId(req.getSession().getId());
					response.setErrorCode(ConstantParams.ERROR_CODE_SUCCESS);
					response.setErrorMess(ConstantParams.ERROR_MESS_SUCCESS);
					return ConvertUtil.convertObjectToJson(response);
				}
			}
			log.info("---login---");
			userDTO = UsersDAO.getUserByEmail(email);
			if (userDTO == null && loginType == ConstantParams.LOGIN_TYPE_APP) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_EMAIL_NOT_EXIST);
				response.setErrorMess(ConstantParams.ERROR_MESS_EMAIL_NOT_EXIST);
				return ConvertUtil.convertObjectToJson(response);
			}

			if (userDTO == null && loginType != ConstantParams.LOGIN_TYPE_APP) {
				userDTO = new UsersDTO();
				userDTO.setEmail(email);
				userDTO.setPassword(password);
				userDTO.setAreaId(areaId);
				userDTO.setUserType(loginType);
				Integer userId = UsersDAO.insert(userDTO);
				log.info("new user create userId: " + userId);
				if (userId == null || userId == 0) {
					response.setErrorCode(ConstantParams.ERROR_CODE_SYSTEM_ERROR);
					response.setErrorMess(ConstantParams.ERROR_MESS_SYSTEM_ERROR);
					return ConvertUtil.convertObjectToJson(response);
				} else {
					userDTO.setId(userId);
				}
			}

			session.setAttribute(ConstantParams.LOGIN_USER_INFO, userDTO);
			response.setSuccess(true);
			response.setEmail(email);
			response.setMobile(userDTO.getMobile());
			response.setPoint(String.valueOf(userDTO.getPoint() == null ? 0
					: userDTO.getPoint()));
			response.setUserId(userDTO.getId());
			response.setSessionId(req.getSession().getId());
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
