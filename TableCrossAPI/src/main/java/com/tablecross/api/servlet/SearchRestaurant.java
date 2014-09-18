package com.tablecross.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import com.tablecross.api.common.ConstantParams;
import com.tablecross.api.dao.RestaurantsDAO;
import com.tablecross.api.model.RestaurantsDTO;
import com.tablecross.api.model.UsersDTO;
import com.tablecross.api.object.SearchRestaurantResponse;
import com.tablecross.api.util.ConvertUtil;

public class SearchRestaurant extends HttpServlet {
	private static Logger log = Logger.getLogger(SearchRestaurant.class);

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
		String outStr = doSearchRestaurant(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doSearchRestaurant(HttpServletRequest req) {
		SearchRestaurantResponse response = new SearchRestaurantResponse();
		try {
			HttpSession session = req.getSession(true);

			UsersDTO userDTO = (UsersDTO) session
					.getAttribute(ConstantParams.LOGIN_USER_INFO);

			int searchType = Integer.parseInt(req.getParameter("searchType"));

			int total = -1;
			if (req.getParameter("total") != null) {
				total = Integer.parseInt(req.getParameter("total"));
			}
			String searchKey = req.getParameter("searchType");
			BigDecimal longitude = null;
			BigDecimal latitude = null;

			if (searchType == ConstantParams.SEARCH_TYPE_HISTORY) {
				if (userDTO == null) {
					response.setSuccess(false);
					response.setErrorCode(ConstantParams.ERROR_CODE_USER_IS_NOT_LOGIN);
					response.setErrorMess(ConstantParams.ERROR_MESS_USER_IS_NOT_LOGIN);
					return ConvertUtil.convertObjectToJson(response);
				}

				longitude = new BigDecimal(req.getParameter("longitude"));
				latitude = new BigDecimal(req.getParameter("latitude"));
			}
			List<RestaurantsDTO> lst = RestaurantsDAO.searchRestaurant(
					searchType, (userDTO != null ? userDTO.getId() : null),
					searchKey, longitude, latitude, total);
			response.setQuantity(lst.size());
			response.setItems(lst.toArray(new RestaurantsDTO[lst.size()]));
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
