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
import com.tablecross.api.object.GetUserInfoResponse;
import com.tablecross.api.util.ConvertUtil;

public class GetUserInfo extends HttpServlet {
	private static Logger log = Logger.getLogger(GetUserInfo.class);

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
		String outStr = doGetUserInfo(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doGetUserInfo(HttpServletRequest req) {
		GetUserInfoResponse response = new GetUserInfoResponse();
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
			userDTO = UsersDAO.loadUser(userDTO.getId());
			session.setAttribute(ConstantParams.LOGIN_USER_INFO, userDTO);

			response.setUserId(userDTO.getId());
			response.setEmail(userDTO.getEmail());
			response.setMobile(userDTO.getMobile());
			response.setBirthday(ConvertUtil.getFormatDateView(userDTO
					.getBirthday()));
			response.setOrderCount(userDTO.getOrderCount());
			response.setPoint(String.valueOf(userDTO.getPoint()));
			response.setSuccess(true);
			response.setErrorCode(ConstantParams.ERROR_CODE_SUCCESS);
			response.setErrorMess(ConstantParams.ERROR_MESS_SUCCESS);
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
