package cn.com.ire.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Match {
	
	private static Pattern patternip = Pattern.compile("[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}");
	private static Pattern patternts = Pattern.compile("[0-9]*");
	private static Pattern patternnum = Pattern.compile("(\\d+\\.)+\\d+");
	
	private static  boolean match(String str,String rule) {
		Pattern pattern = Pattern.compile(rule);
		Matcher math = pattern.matcher(str);
		return math.matches();
	}
	
	public static  boolean isIPV4(String ip){
		if(ip.indexOf("/")>-1) ip=ip.split("/")[0];
		if(ip.indexOf(":")>-1) ip=ip.split(":")[0];
		return patternip.matcher(ip).matches();
	}
	
	public static boolean is10TimeStamp(String ts){
		if(ts.length()<10) return false;
		return patternts.matcher(ts).matches();
	}
	
	public static boolean isNum(String str){
		return patternnum.matcher(str).matches();
	}
	
	
	public static void main(String[] args) {
		System.out.println(isIPV4("100.168.0.1"));
		System.out.println(is10TimeStamp("1111111111"));
		System.out.println(isNum("100.00.00"));
	}
}
