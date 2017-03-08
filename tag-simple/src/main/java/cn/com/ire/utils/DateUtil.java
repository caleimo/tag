package cn.com.ire.utils;

/**
 * @author xlm
 */
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class  DateUtil {
	/**
	 * 
	 * @param strDate
	 * @param dateFormat
	 * @return
	 */
	public static String formatDate(String strDate,String dateFormat) {
		DateFormat DATE_FORMAT = new SimpleDateFormat(dateFormat);
		Calendar calendar = Calendar.getInstance();
		if (strDate == null) {
			calendar.add(Calendar.DAY_OF_MONTH, -1);
		} else {
			try {
				calendar.setTime(DATE_FORMAT.parse(strDate));
			} catch (Exception e) {
				try {
					calendar.add(Calendar.DAY_OF_MONTH,Integer.parseInt(strDate));
				} catch (Exception ee) {
					calendar.add(Calendar.DAY_OF_MONTH, -1);
				}
			}
		}
		strDate = DATE_FORMAT.format(calendar.getTime());
		return strDate;
	}
	/**
	 * das 浠ｈ〃 after
	 * @param days
	 * @param format
	 * @return
	 */
	public static String DateBefAft(int days, String format) {
		if (format == null || "".equals(format))
			format = "yyyyMMdd";
		Calendar now = Calendar.getInstance();
		SimpleDateFormat formatter = new SimpleDateFormat(format);
		now.add(Calendar.DAY_OF_YEAR, days);
		return formatter.format(now.getTime());
	}
	
	public static String DateBefAft(int days){
		return DateBefAft(days,null);
	}
	
	/**
	 * 是否是有效的日期
	 */
	public static boolean isValidDate(String s) {
		try {
			SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
			dateFormat.setLenient(false);
			dateFormat.parse(s);
			return true;
		} catch (Exception e) {
			// 如果throw java.text.ParseException或者NullPointerException，就说明格式不对
			return false;
		}
	}
	
	public static void main(String[] args) {
		System.out.println(DateBefAft(5,"yyyyMMdd"));
	}
}
