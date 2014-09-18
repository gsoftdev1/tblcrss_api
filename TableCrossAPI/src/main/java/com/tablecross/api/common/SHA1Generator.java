package com.tablecross.api.common;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import org.apache.commons.codec.binary.Base64;

public class SHA1Generator {
	public static String encrypt(String plaintext) {
		if (plaintext == null || plaintext.equals("")) {
			return "";
		}
		MessageDigest md = null;
		try {
			md = MessageDigest.getInstance("SHA");
			md.update(plaintext.getBytes("UTF-8"));
			byte raw[] = md.digest();
			String hash = new String(Base64.encodeBase64(raw));

			return hash;
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		return "";
	}

	public static void main(String[] args) {
		System.out.println(encrypt("123"));
	}
}
