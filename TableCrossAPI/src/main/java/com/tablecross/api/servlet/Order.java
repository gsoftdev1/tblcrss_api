package com.tablecross.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.ParseException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tablecross.api.common.ConstantParams;
import com.tablecross.api.dao.OrdersDAO;
import com.tablecross.api.model.OrdersDTO;
import com.tablecross.api.model.UsersDTO;
import com.tablecross.api.object.OrderResponse;
import com.tablecross.api.util.ConvertUtil;

public class Order extends HttpServlet {
	private static Logger log = Logger.getLogger(Order.class);

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
		String outStr = doOrder(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doOrder(HttpServletRequest req) {
		OrderResponse response = new OrderResponse();
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
			Integer restaurantId = Integer.parseInt(req
					.getParameter("restaurantId"));
			Integer quantity = Integer.parseInt(req.getParameter("quantity"));
			String orderDate = req.getParameter("orderDate");
			log.info("doOrder restaurantId: " + restaurantId + "|quantity: "
					+ quantity + "|orderDate: " + orderDate);
			OrdersDTO order = new OrdersDTO();
			order.setUserId(userDTO.getId());
			order.setRestaurantId(restaurantId);
			order.setCreatedBy(userDTO.getEmail());
			order.setQuantity(quantity);
			order.setOrderDate(ConvertUtil.parseFormatDateOrder(orderDate));
			order.setStatus(ConstantParams.ORDER_STATUS_PENDING);

			Integer orderId = OrdersDAO.insert(order);

			log.info("order insert orderId: " + orderId);
			if (orderId == null || orderId.intValue() == 0) {
				response.setSuccess(false);
				response.setErrorCode(ConstantParams.ERROR_CODE_SYSTEM_ERROR);
				response.setErrorMess(ConstantParams.ERROR_MESS_SYSTEM_ERROR);
				return ConvertUtil.convertObjectToJson(response);
			}

			response.setSuccess(true);
			response.setErrorCode(ConstantParams.ERROR_CODE_SUCCESS);
			response.setErrorMess(ConstantParams.ERROR_MESS_SUCCESS);
			return ConvertUtil.convertObjectToJson(response);
		} catch (Exception e) {
			log.error("ERROR: ", e);
			response.setSuccess(false);
			if (e instanceof NumberFormatException
					|| e instanceof NullPointerException
					|| e instanceof ParseException) {
				response.setErrorCode(ConstantParams.ERROR_CODE_PARAMS_INVALID);
				response.setErrorMess(ConstantParams.ERROR_MESS_PARAMS_INVALID);
				return ConvertUtil.convertObjectToJson(response);
			}
			response.setErrorCode(ConstantParams.ERROR_CODE_SYSTEM_ERROR);
			response.setErrorMess(ConstantParams.ERROR_MESS_SYSTEM_ERROR + ": "
					+ e.getMessage());
			return ConvertUtil.convertObjectToJson(response);
		}
	}
}
