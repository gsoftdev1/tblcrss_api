package com.tablecross.api.util;

public class ValidateUtil {
	public static boolean validateEmail(String email) {
		if (email == null || email.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean validateMobile(String mobile) {
		if (mobile == null || mobile.isEmpty()) {
			return true;
		}
		return false;
	}

	public static boolean validateBirthday(String birthday) {
		if (birthday == null || birthday.isEmpty()) {
			return true;
		}
		try {
			ConvertUtil.parseFormatDateView(birthday);
		} catch (Exception ex) {
			return true;
		}
		return false;
	}
}
