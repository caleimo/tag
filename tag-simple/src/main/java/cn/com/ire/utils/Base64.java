/*** Eclipse Class Decompiler plugin, copyright (c) 2012 Chao Chen (cnfree2000@hotmail.com) ***/
package cn.com.ire.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

public final class Base64 {
	public static BASE64Encoder be = new BASE64Encoder();
	public static BASE64Decoder bd = new BASE64Decoder();

	public static String encode(String data) {
		return be.encode(data.getBytes());
	}

	public static String decode(String encode) {
		try {
			return new String(bd.decodeBuffer(encode), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	public static String toBASE64(String s) {
		if (s == null)
			return null;
		return (new sun.misc.BASE64Encoder()).encode(s.getBytes());
	}

	public static String getFromBASE64(String s) {
		if (s == null)
			return null;
		BASE64Decoder decoder = new BASE64Decoder();
		try {
			byte[] b = decoder.decodeBuffer(s);
			return new String(b);
		} catch (Exception e) {
			return null;
		}
	}
	
	public static void main(String[] args) throws IOException {
		String data = "aHR0cDovL3Byb2R1Y3Quc3VuaW5nLmNvbS8xMjcwODk1MDMuaHRtbAnoi4/lroHmmJPotK0=";
		System.out.println(getFromBASE64(data));
		/*System.out.println(encode(getFromBASE64(data)));
		System.out.println(decode(encode(data)));*/
		
	}
}