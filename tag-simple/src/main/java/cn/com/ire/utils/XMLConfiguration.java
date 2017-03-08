package cn.com.ire.utils;

import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

public class XMLConfiguration {

	public static Map<String,Object> rayResource = new HashMap<String,Object>();
	private String String;
	
	public  void addResource(String resourceParh) {
		init(resourceParh);
	}

	public  void addResource(Map resm) {
		this.rayResource.putAll(resm);
	}
	public String getString(String key){
		return this.rayResource.get(key)==null?null:(String) rayResource.get(key);
	}
	
	public Object getObj(String key){
		return this.rayResource.get(key);
	}
	
	public void add(String key,Object value){
		this.rayResource.put(key, value);
	}
	
	public void addString(String key,String value){
		this.rayResource.put(key, value);
	}

	private static void init(String path) {

		SAXReader reader = new SAXReader();
		Document document = null;
		try {
			document = reader.read(new File(path));
			Element rootElement = document.getRootElement();
			Element recordEle = null;
			
			Element properties = (Element) rootElement.elementIterator("properties").next();
			for (Iterator it = properties.elementIterator("property"); 
					it.hasNext();) {
				recordEle = (Element) it.next();
				rayResource.put(recordEle.elementTextTrim("name").replaceAll("\\s*", ""), recordEle.elementTextTrim("value").replaceAll("\\s*", ""));
			}

		} catch (DocumentException e) {
			e.printStackTrace();
		}
	}

	public Integer getRNum(String key) {
		Integer a= Integer.parseInt((String) this.rayResource.get(key),0);
		 return a==null?-1:a;
	}
	
	public Integer getRNum(String key,Integer v) {
		Integer a= getRNum( key);
		 return a==null?v:a;
	}

	public String getString(String k, String v) {
		String v1= getString(k);
		return v1==null?v:v1;
	}

}
