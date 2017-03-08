package cn.com.ire.utils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URI;
import java.util.Map;



public class UrlUtils {
	/**
	 * 转换编码 ISO-8859-1到GB2312
	 * 
	 * @param text
	 * @return
	 */
	public String ISO2GB(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("ISO-8859-1"), "GB2312");
		} catch (UnsupportedEncodingException ex) {
			result = ex.toString();
		}
		return result;
	}

	public String GB2UTF8(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("GB2312"), "UTF-8");
		} catch (UnsupportedEncodingException e) {
			result = e.toString();
		}
		return result;
	}

	/**
	 * 转换编码 GB2312到ISO-8859-1
	 * 
	 * @param text
	 * @return
	 */
	public String GB2ISO(String text) {
		String result = "";
		try {
			result = new String(text.getBytes("GB2312"), "ISO-8859-1");
		} catch (UnsupportedEncodingException ex) {
			ex.printStackTrace();
		}
		return result;
	}

	/**
	 * Utf8URL编码
	 * 
	 * @param s
	 * @return
	 */
	public static String Utf8URLencode(String text) {
		StringBuffer result = new StringBuffer();
		for (int i = 0; i < text.length(); i++) {
			char c = text.charAt(i);
			if (c >= 0 && c <= 255) {
				result.append(c);
			} else {
				byte[] b = new byte[0];
				try {
					b = Character.toString(c).getBytes("UTF-8");
				} catch (Exception ex) {
				}
				for (int j = 0; j < b.length; j++) {
					int k = b[j];
					if (k < 0)
						k += 256;
					result.append("%" + Integer.toHexString(k).toUpperCase());
				}
			}
		}
		return result.toString();
	}

	/**
	 * Utf8URL解码
	 * 
	 * @param text
	 * @return
	 */
	public static String Utf8URLdecode(String text) {
		String result = "";
		int p = 0;
		if (text != null && text.length() > 0) {
			text = text.toLowerCase();
			p = text.indexOf("%e");
			if (p == -1)
				return text;
			while (p != -1) {
				result += text.substring(0, p);
				text = text.substring(p, text.length());
				if (text == "" || text.length() < 9)
					return result;
				result += CodeToWord(text.substring(0, 9));
				text = text.substring(9, text.length());
				p = text.indexOf("%e");
			}
		}
		return result + text;
	}

	/**
	 * utf8URL编码转字�?
	 * 
	 * @param text
	 * @return
	 */
	private static String CodeToWord(String text) {
		String result;
		if (Utf8codeCheck(text)) {
			byte[] code = new byte[3];
			code[0] = (byte) (Integer.parseInt(text.substring(1, 3), 16) - 256);
			code[1] = (byte) (Integer.parseInt(text.substring(4, 6), 16) - 256);
			code[2] = (byte) (Integer.parseInt(text.substring(7, 9), 16) - 256);
			try {
				result = new String(code, "UTF-8");
			} catch (UnsupportedEncodingException ex) {
				result = null;
			} catch(NumberFormatException e){
				result = text;
			}
		} else {
			result = text;
		}
		return result;
	}

	/**
	 * 编码是否有效
	 * 
	 * @param text
	 * @return
	 */
	private static boolean Utf8codeCheck(String text) {
		String sign = "";
		if (text.startsWith("%e"))
			for (int i = 0, p = 0; p != -1; i++) {
				p = text.indexOf("%", p);
				if (p != -1)
					p++;
				sign += p;
			}
		return sign.equals("147-1");
	}

	/**
	 * 是否Utf8Url编码
	 * 
	 * @param text
	 * @return
	 */
	public boolean isUtf8Url(String text) {
		text = text.toLowerCase();
		int p = text.indexOf("%");
		if (p != -1 && text.length() - p > 9) {
			text = text.substring(p, p + 9);
		}
		return Utf8codeCheck(text);
	}

	/**
	 * 获得搜索和购物车的关键字或产品id
	 * 
	 * @param url
	 * @param rule
	 * @return
	 */
	public static String getSearchKey(String url) {
		if (url == null)
			return null;
		try {
			if (url.indexOf("baidu.com") > -1 && url.indexOf("wd=") > -1)
				return StringUtil.subMiddle(url, "wd=", "&");
			if (url.indexOf("baidu.com") > -1 && url.indexOf("word=") > -1)
				return StringUtil.subMiddle(url, "word=", "&");
			if (url.indexOf("haosou.com") > -1 && url.indexOf("q=") > -1)
				return StringUtil.subMiddle(url, "q=", "&");
			// 手机淘宝
			if (url.indexOf("s.m.taobao.com") > -1 && url.indexOf("&q=") > -1)
				return StringUtil.subMiddle(url, "q=", "&");
			// 搜狗
			if (url.indexOf("www.sogou.com") > -1 && url.indexOf("query=") > -1)
				return StringUtil.subMiddle(url, "query=", "&");
			if (url.indexOf("m.sogou.com") > -1 && url.indexOf("keyword=") > -1)
				return StringUtil.subMiddle(url, "keyword=", "&");

			// 京东
			if (url.indexOf("earch.jd.com") > -1
					&& url.indexOf("earch?keyword=") > -1)
				return StringUtil.subMiddle(url, "earch?keyword=", "&");
			if (url.indexOf("m.jd.com") > -1
					&& url.indexOf("search.action?") > -1)
				return StringUtil.subMiddle(url, "keyword=", "&");
			if (url.indexOf("wq.jd.com") > -1
					&& url.indexOf("search?key=") > -1)
				return StringUtil.subMiddle(url, "search?key=", "&");

			// 苏宁
			if (url.indexOf("search.suning.com/") > -1)
				// 取苏宁搜索url中的搜索关键�?search.suning.com/***/中间的一段再进行html解码
				return StringUtil.subMiddle(url, "search.suning.com/", "/");
			if (url.indexOf("m.suning.com/") > -1
					&& url.indexOf("keyword=") > -1)
				return StringUtil.subMiddle(url, "keyword=", "/");

			// 爱奇�?
			if (url.indexOf("so.iqiyi.com") > -1 && url.indexOf("q_") > -1)
				return StringUtil.subMiddle(url, "q_", "?source");
			if (url.indexOf("m.iqiyi.com/search.html?key=") > -1)
				return StringUtil.subRight(url, "m.iqiyi.com/search.html?key=");

			// 搜狐
			if (url.indexOf("tv.sohu.com/") > -1 && url.indexOf("wd=") > -1)// 搜狐
				return StringUtil.subRight(url, "wd=");

			// 优酷
			if (url.indexOf("soku.com/search") > -1 && url.indexOf("q_") > -1)// 优酷
				return StringUtil.subMiddle(url, "q_", "?");
			if (url.indexOf("soku.com/m/t/video?q=") > -1)// 优酷
				return StringUtil.subRight(url, "soku.com/m/t/video?q=");

			// 乐视
			if (url.indexOf("letv.com/") > -1 && url.indexOf("wd") > -1) {
				return StringUtil.subMiddle(url, "wd=", "&");
			}
			// �?���?
			if (url.indexOf("search.m.yhd.com/search/k") > -1) {
				// return UrlUtils.Utf8URLdecode(StringUtil.subMiddle(url,
				// "search.m.yhd.com/search/k","?"));
				return StringUtil.subMiddle(url, "search.m.yhd.com/search/k",
						"?");

			}
			if (url.indexOf("search.yhd.com/c0-0/k") > -1) {
				return StringUtil.subMiddle(url, "search.yhd.com/c0-0/k", "/");
				// return UrlUtils.Utf8URLdecode(StringUtil.subMiddle(url,
				// "search.yhd.com/c0-0/k","/"));
			}
			if (url.indexOf("baofeng.com/q_") > -1)
				return Base64.getFromBASE64(StringUtil.subRight(url,
						"baofeng.com/q_"));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * 获取购物车物�?)
	 * 
	 * @param url
	 * @return
	 */
	public static String getProducts(String url) {
		if (url == null)
			return null;
		try {
			if (url.indexOf("cart.jd.com") > -1 && url.indexOf("pid=") > -1)// pid=sefsfef
				return "http://item.jd.com/"
						+ StringUtil.subMiddle(url, "pid=", "&") + ".html";

			if ((url.indexOf("buy.yhd.com") > -1 || (url
					.indexOf("cart.suning.com") > -1))
					&& url.indexOf("returnurl=") > -1) {
				url = StringUtil.subRight(url, "returnurl=");
				if (url.indexOf("product.suning.com") > -1
						|| url.indexOf("item.yhd.com") > -1)
					return StringUtil.subMiddle(url, "returnurl=", "?");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	/**
	 * @param url
	 * @param map
	 *            <domain,>
	 * @return kw
	 */
	public static String getSearchKW(String url, Map<String, String> map) {
		String kw = null;
		for (String key : map.keySet()) {
			if (url.indexOf(key) > -1) {
				return kw = StringUtil.subMiddle(url, key, map.get(key));
			}
		}
		return kw;
	}

	public static void main(String[] args) throws IOException {
		/*
		 * BufferedReader br=new BufferedReader(new FileReader("F://ssss.txt"));
		 * BufferedWriter bw=new BufferedWriter(new FileWriter("F://ss")); int
		 * n=0; int max=100;//Integer.parseInt(args[2]); String temp=null;
		 * String url=""; String skw=null; String pro=null; temp=br.readLine();
		 * while(temp!=null){ url=temp; skw=getSearchKey(url); if(skw!=null)
		 * skw=UrlUtils.Utf8URLdecode(skw); pro=getProducts(url); if(pro!=null)
		 * pro=UrlUtils.Utf8URLdecode(pro);
		 * 
		 * 
		 * if(skw!=null || pro!=null){ if(n>max) { bw.close();br.close();
		 * return;} bw.write(url+"\t"+skw+"\t"+pro);bw.newLine();n++;
		 * System.out.println(n); bw.flush(); }else System.out.println(url);
		 * temp=br.readLine();//继续读下�?�� } bw.close();br.close();
		 */
	}

//	@Test
	public void test() {
		// String a =
		// "http://search.jd.com/Search?keyword=%E5%8D%8E%E4%B8%BA(HUAWEI)-%E6%89%8B%E6%9C%BA&enc=utf-8&wq=%E4%BD%B3%E8%83%BD&pvid=yqqdo3fi.ltk7hi ";
		// String b ="http://search.suning.com/%E5%B0%8F%E7%B1%B34/cityId=9173";
		/*
		 * String c =
		 * "http://www.google.com.hk/search?nord=1&num=100&start=100&q=/s?rsv_idx=1&tn=se_hldp08000_gyunaabb&wd=%e9%9a%8b%e5%94%90%e6%bc%94%e4%b9%8&nord=1"
		 * ; //System.out.println(isUtf8Url(aa)+" "+isUtf8Url(a)); String
		 * urlEncoder1 = Utf8URLdecode(c); //String urlEncoder1 =
		 * Utf8URLdecode(b); //System.out.println(urlEncoder+"\n"+urlEncoder1);
		 * /
		 * /urlEncoder=urlEncoder.substring(urlEncoder.indexOf("search?keyword="
		 * )+15,urlEncoder.indexOf("&"));
		 * System.out.println("----"+urlEncoder1);
		 * System.out.println(Utf8URLdecode(urlEncoder1));
		 * urlEncoder1=urlEncoder1
		 * .substring(urlEncoder1.indexOf("search.suning.com/")+18);
		 * System.out.println(urlEncoder1.substring(0,urlEncoder1.indexOf("/")
		 * )); System.out.println(); String searchKeyWord=
		 * UrlUtils.Utf8URLdecode(c);
		 * searchKeyWord=searchKeyWord.substring(searchKeyWord
		 * .indexOf("search.suning.com/")+18); if(searchKeyWord.indexOf("/")>0){
		 * searchKeyWord=searchKeyWord.substring(0,searchKeyWord.indexOf("/"));
		 * System.out.println(searchKeyWord); System.out.println();
		 */

		// System.out.println(getProducts(Utf8URLdecode("http://cart.suning.com/emall/OrderItemDisplay?langId=-7&storeId=10052&catalogId=10051&returnURL=http://product.suning.com/106508985.html")));
		// System.out.println(Utf8URLdecode("%c2%f3%bf%cf%ce%fd%b4%ab%c6%e6"));
		// System.out.println(Utf8URLdecode("http://www.sogou.com/web?query=%D1%CE%B3%C7%CA%D0%CC%B0%B8%AF%B9%D9%D4%B1%D3%EB%C7%E9%B8%BE%CD%BC%C6%AC"));
		// System.out.println("sesf"+null);
		String searchKeyWord = "";
		String searchKwRef = "";
		searchKeyWord = searchKeyWord == null ? searchKwRef
				: (searchKwRef == null ? searchKeyWord : searchKeyWord + "|"
						+ searchKwRef);
		System.out.println(searchKeyWord);
		System.out.println(searchKeyWord.equals("|"));

	}
//	@Test
	public  void get() {
		String url = "http://show.m.mediav.com/update?sdkv=1124&nsdkv=1127&imei=868256020588637&model=Letv%20X500&channelid=1&appv=15.0&appvc=560&apppkg=com.netease.newsreader.activity&brand=Letv";
		URI create = URI.create(url);
		System.out.println(create.getQuery());
		// 1.5-3T,没压缩
		// tupe pa,结构化 200g,多个文件，spark输出的，文件的格式
		// oss 省流量 @3 ，mont到硬盘的，tmp目录 流量收费，存储包。0.01/10000次
		// 存储式
		// nas 内测

		//
	}

}
