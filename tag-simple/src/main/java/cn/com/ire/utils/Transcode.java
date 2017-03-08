package cn.com.ire.utils;

public class Transcode  {
	/**
	 * transfrom base64
	 * 
	 * @param string
	 * @param int only 1 or 2
	 * @return string
	 */
	public static String evaluate(String string, int flag) throws Exception {

		switch (flag) {
		case 1:
			return Base64.getFromBASE64(string);
		case 2:
			return Base64.toBASE64(string);
		case 3:
			return UrlUtils.Utf8URLdecode(string);// url解码
		case 4:
			return UrlUtils.Utf8URLencode(string);// url加码
		default:
			throw new Exception("please set the second param with 1 or 2 or 3 or 4");
		}
	}
}
