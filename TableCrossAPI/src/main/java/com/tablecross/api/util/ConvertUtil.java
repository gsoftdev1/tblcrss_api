package com.tablecross.api.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.google.gson.Gson;

public class ConvertUtil {
	private static SimpleDateFormat formatDateInsert = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	private static SimpleDateFormat formatDateView = new SimpleDateFormat(
			"dd/MM/yyyy");
	private static SimpleDateFormat formatDateOrder = new SimpleDateFormat(
			"dd/MM/yyyy HH:mm:ss");

	public static String getFormatDateView(Date date) {
		if (date == null) {
			return null;
		} else {
			return formatDateView.format(date);
		}
	}

	public static Date parseFormatDateView(String date) throws ParseException {
		return formatDateView.parse(date);
	}

	public static String getFormatDateInsert(Date date) {
		if (date == null) {
			return null;
		} else {
			return formatDateInsert.format(date);
		}
	}

	public static Date parseFormatDateOrder(String date) throws ParseException {
		return formatDateOrder.parse(date);
	}

	public static String convertObjectToJson(Object o) {
		Gson gson = new Gson();
		String json = gson.toJson(o);
		return json;
	}
}
