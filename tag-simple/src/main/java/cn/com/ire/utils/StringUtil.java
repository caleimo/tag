package cn.com.ire.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Stack;

public class StringUtil {
	private static Stack<String> stack =new Stack<String>();
	private static StringBuffer sb ;
	//字符串�?�?
	public static String  reverseString(String str,String split,String cs){
		if(str==null || split==null) return null;
		for (String s : str.split(split)) {
			stack.push(s);
		}
		sb= new StringBuffer();
			while(!stack.isEmpty()){
				sb.append(stack.pop()).append(cs);
			}
		return sb.substring(0, sb.lastIndexOf(cs));
	}
	
	public static String  reverseString(String str,String split){
		return reverseString(str,split,split);
	}
	
	 /**
     * A hashing method that changes a string (like a URL) into a hash suitable for using as a
     * disk filename.
     */
    public static String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private static String bytesToHexString(byte[] bytes) {
        // http://stackoverflow.com/questions/332079
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
    
    /**
     * 获取�?��字符串中，（从左到右）第�?��目标字符串的后面的内�?
     * @param srcStr
     * @param indStr
     * @return
     */
    public static String subRight(String srcStr,String indStr){
    	if(srcStr==null) return null;
    	if(indStr==null) return srcStr;
    	if(srcStr.indexOf(indStr)>-1){
    		if(srcStr.length()==indStr.length()) return "";
    		srcStr=srcStr.substring(srcStr.lastIndexOf(indStr)+indStr.length());}
		return srcStr;
    }
    
    /**
     * 获取�?��字符串中，（从左到右）第�?��目标字符串的后面的内�?
     * @param srcStr
     * @param indStr
     * @return
     */
    public static String subLastRight(String srcStr,String indStr){
    	if(srcStr==null) return null;
    	if(indStr==null) return srcStr;
    	if(srcStr.indexOf(indStr)>-1){
    		if(srcStr.length()==indStr.length()) return "";
    		srcStr=srcStr.substring(srcStr.lastIndexOf(indStr)+indStr.length());}
    	//srcStr=srcStr.replaceFirst(indStr, "");
		return srcStr;
    }
    /**
     * 获取�?��字符串中，（从左到右）第�?��目标字符串的前面的内�?
     * @param srcStr
     * @param indStr
     * @return
     */
    public static String subLeft(String srcStr,String indStr){
    	if(srcStr==null) return null;
    	if(indStr==null) return srcStr;
    	if(srcStr.indexOf(indStr)>-1)
    		srcStr=srcStr.substring(0,srcStr.indexOf(indStr));
		return srcStr;
    }
    /**
     * 获取�?��字符串中，（从右到左）第�?��目标字符串的前面的内�?
     * @param srcStr
     * @param indStr
     * @return
     */
    public static String subLastLeft(String srcStr,String indStr){
    	if(srcStr==null) return null;
    	if(indStr==null) return srcStr;
    	if(srcStr.indexOf(indStr)>-1)
    		srcStr=srcStr.substring(0,srcStr.lastIndexOf(indStr));
		return srcStr;
    }
    
    /**
     * 获取start的最初位置到到start后end的最晚出现位置中间内容（end在start后面�?
     * @param srcStr
     * @param start
     * @param end
     * @return
     */
    public static String subLagerMiddle(String srcStr,String start,String end){
    	return subLastLeft(subRight(srcStr, start),end);
    }
    
    /**
     * 获取start的最后出现位置到start后end的最早出现位置中间内容（end在start后面�?
     * @param srcStr
     * @param start
     * @param end
     * @return
     */
    public static String subMiddle(String srcStr,String start,String end){
    	return subLeft(subLastRight(srcStr, start),end);
    }
    
    //字符串数组反转
    private static String[] reverse(String[] array){
		String temp;
		for(int i=0;i<array.length / 2;i++){
			temp =array[i];
			array[i]=array[array.length-1-i];
			array[array.length-1-i]=temp;
		}
		return array;
	}
	

	public static void main(String[] args) {
		//System.out.println(reverseString("ha|nime|gege", "\\|","|"));
		//System.out.println(reverseString("hanimegege", "\\|","|"));
		//System.out.println("sd|ssd".indexOf("|"));
		/*String aa="TW96aWxsYS81LjAgKGlQYWQ7IFU7IENQVSBPUyA4XzQgbGlrZSBNYWMgT1MgWD"
				+ "sgemgtQ047IGlQYWQ0LDQpIEFwcGxlV2ViS2l0LzUzNC40NiAoS0hUTUwsIGxpa2UgR2Vja28pIFVDQnJvd3Nlci8yLjguOC42NTIgVTMvIE1vYmlsZS8xMEE0MDMgU2FmYXJpLzc1NDMuNDguMw==";
		System.out.println(hashKeyForDisk(aa));*/
		System.out.println(subLagerMiddle("http://buy.yhd.com/checkoutV3/index.do?fastBuyFlag=1&returnUrl=http://item.yhd.com/item/44767206?", "returnUrl=","?"));
	}
}
