package com.tablecross.api.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import com.tablecross.api.common.ConstantParams;
import com.tablecross.api.dao.AreaDAO;
import com.tablecross.api.model.AreaDTO;
import com.tablecross.api.object.GetAreasResponse;
import com.tablecross.api.util.ConvertUtil;

public class GetAreas extends HttpServlet {
	private static Logger log = Logger.getLogger(GetAreas.class);

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
		String outStr = doGetAreas(req);
		log.info("Response: " + outStr);
		resp.setContentType("application/json");
		PrintWriter out = resp.getWriter();
		out.println(outStr);
		out.flush();
		out.close();
	}

	private String doGetAreas(HttpServletRequest req) {
		GetAreasResponse response = new GetAreasResponse();
		try {
			List<AreaDTO> lst = AreaDAO.getAreas();
			response.setQuantity(lst.size());
			response.setItems(lst.toArray(new AreaDTO[lst.size()]));
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
