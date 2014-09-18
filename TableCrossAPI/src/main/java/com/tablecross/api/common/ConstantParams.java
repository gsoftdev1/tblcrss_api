package com.tablecross.api.common;

public class ConstantParams {

	/**
	 * LOGIN TYPE
	 */
	public static final int LOGIN_TYPE_APP = 0;
	public static final int LOGIN_TYPE_FACEBOOK = 1;
	/** LOGIN USER INFO **/
	public static final String LOGIN_USER_INFO = "LOGIN_USER_INFO";
	/**
	 * Search type
	 */
	public static final int SEARCH_TYPE_HISTORY = 0;
	public static final int SEARCH_TYPE_DISTANCE = 1;
	public static final int SEARCH_TYPE_KEY_WORD = 2;
	/**
	 * ERROR DEFINE
	 */
	public static int ERROR_CODE_SUCCESS = 1;
	public static String ERROR_MESS_SUCCESS = "SUCCESS";

	public static int ERROR_CODE_EMAIL_NOT_EXIST = 0;
	public static String ERROR_MESS_EMAIL_NOT_EXIST = "EMAIL_NOT_EXIST";

	public static int ERROR_CODE_USER_IS_NOT_LOGIN = 2;
	public static String ERROR_MESS_USER_IS_NOT_LOGIN = "USER_IS_NOT_LOGIN";

	public static int ERROR_CODE_OLD_PASSWORD_INVALID = 3;
	public static String ERROR_MESS_OLD_PASSWORD_INVALID = "OLD_PASSWORD_INVALID";

	public static int ERROR_CODE_NEW_PASSWORD_INVALID = 4;
	public static String ERROR_MESS_NEW_PASSWORD_INVALID = "NEW_PASSWORD_INVALID";

	public static int ERROR_CODE_USER_ID_NOT_EXIST = 5;
	public static String ERROR_MESS_USER_ID_NOT_EXIST = "USER_ID_NOT_EXIST";

	public static int ERROR_CODE_PARAMS_INVALID = 6;
	public static String ERROR_MESS_PARAMS_INVALID = "PARAMS_INVALID";

	public static int ERROR_CODE_EMAIL_IS_EXIST = 7;
	public static String ERROR_MESS_EMAIL_IS_EXIST = "EMAIL_IS_EXIST";

	public static int ERROR_CODE_WRONG_PASSWORD = 8;
	public static String ERROR_MESS_WRONG_PASSWORD = "WRONG_PASSWORD";

	public static int ERROR_CODE_SYSTEM_ERROR = 99;
	public static String ERROR_MESS_SYSTEM_ERROR = "SYSTEM_ERROR";

}
