package cn.com.ire.utils;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Properties;

/**
 * manager my.properties file
 */
public class RayConfManager {
	private static Properties pro = new Properties();
	static {
		try {
			pro.load(RayConfManager.class.getResourceAsStream("my.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("unchecked")
	public static <T> T getProperty(String key,Class<T> c){
		if( "java.lang.String".equals(c.getName())  )
			return (T) getString(key);
		try {
			Method sAge = c.getMethod("valueOf", String.class);
			return (T) sAge.invoke(c , getString(key));
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		}
		return null;
	}
	
	private static <T> T getv(String className,String val){
		 try {
			//得到类对象
			Class c = Class.forName(className);
			//Object yourObj = c.newInstance();
			//得到方法
			Method methlist[] = c.getDeclaredMethods();
			for (int i = 0; i < methlist.length; i++) {
			Method m = methlist[i];
			}
			//获取到方法对象,假设方法的参数是一个int,method名为setAge
			Method sAge = c.getMethod("valueOf", new Class[] {String.class});
			//获得参数Object
			Object[] arguments = new Object[] {val};
			//执行方法
			return (T) sAge.invoke(c, arguments);
			} catch (Exception e) {
				e.printStackTrace();
			}
		return null;
	}
	
	public static String getString(String key){
		return pro.getProperty(key);
	}

}
