package com.wis.basis.common.utils.security;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * @author Daniel Woo
 * 
 */
public class MD5 {

	static final byte[] HEX_CHAR_TABLE = { (byte) '0', (byte) '1', (byte) '2',
			(byte) '3', (byte) '4', (byte) '5', (byte) '6', (byte) '7',
			(byte) '8', (byte) '9', (byte) 'a', (byte) 'b', (byte) 'c',
			(byte) 'd', (byte) 'e', (byte) 'f' };

	private static String getHexString(byte[] raw) throws UnsupportedEncodingException 
	{
		byte[] hex = new byte[2 * raw.length];
		int index = 0;

		for (byte b : raw) {
			int v = b & 0xFF;
			hex[index++] = HEX_CHAR_TABLE[v >>> 4];
			hex[index++] = HEX_CHAR_TABLE[v & 0xF];
		}
		return new String(hex, "ASCII");
	}

	public static String digest(String text) throws NoSuchAlgorithmException, UnsupportedEncodingException 
	{
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(text.getBytes("utf8"), 0, text.length());
		byte[] sha1hash = md.digest();
		return getHexString(sha1hash);
	}
	
}
