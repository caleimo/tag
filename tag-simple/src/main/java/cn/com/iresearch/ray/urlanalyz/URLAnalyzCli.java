package cn.com.iresearch.ray.urlanalyz;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serializable;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.lang3.StringUtils;

import cn.com.ire.utils.FileHandler;

public class URLAnalyzCli implements Serializable {
	private static final String DICT_NAME = "dict_category.txt";
	private static Log LOG = LogFactory.getLog(URLAnalyzCli.class);

	/**
	 * url_structure
	 */
	public static class URLStructure implements Serializable {

		private String siteType;
		private String webSite;
		private String url;
		private Map<String, String> params;

		private String action;
		private String value;
		private String[] otherParams;
		private String describe;

		public URLStructure(String siteType, String webSite, String url,
				String[] otherParams) {
			this.siteType = siteType;
			this.webSite = webSite;
			this.otherParams = otherParams;
			this.params = new HashMap<String, String>();

			String[] us = url.split("\\?");
			this.url = us[0];
			if (us.length == 2) {
				String p = url.split("\\?")[1];
				for (String pv : p.split("&"))
					if (pv.contains("="))
						this.params.put(pv.split("=")[0],
								pv.substring(pv.indexOf("=") + 1, pv.length()));
					else
						this.params.put(pv, null);
			}
		}

		@Override
		public String toString() {
			return (new StringBuilder()).append("URLStructure{siteType='")
					.append(siteType).append('\'').append(", webSite='")
					.append(webSite).append('\'').append(", url='").append(url)
					/*
					 * .append('\'').append(", param='").append(param)
					 * .append('\'').append(", value='").append(value)
					 * .append('\'').append(", action='").append(action)
					 */
					.append('\'').append('}').toString();

		}

		public Map<String, String> getParams() {
			return params;
		}

		public void setParams(Map<String, String> params) {
			this.params = params;
		}

		public String getSiteType() {
			return siteType;
		}

		public void setSiteType(String siteType) {
			this.siteType = siteType;
		}

		public String getWebSite() {
			return webSite;
		}

		public void setWebSite(String webSite) {
			this.webSite = webSite;
		}

		public String getUrl() {
			return url;
		}

		public void setUrl(String url) {
			this.url = url;
		}

		public String getAction() {
			return action;
		}

		public void setAction(String action) {
			this.action = action;
		}

		public String getValue() {
			return value;
		}

		public void setValue(String value) {
			this.value = value;
		}

		public String[] getOtherParams() {
			return otherParams;
		}

		public void setOtherParams(String[] otherParams) {
			this.otherParams = otherParams;
		}

		public String getDescribe() {
			return describe;
		}

		public void setDescribe(String describe) {
			this.describe = describe;
		}

		public boolean matchUrlParam(String params) {
			if ((params == null) && (this.params.isEmpty()))
				return true;
			if ((params == null) && (!(this.params.isEmpty()))) {
				return false;
			}
			String[] pvs = params.split("&+");
			if (pvs.length < this.params.size()) {
				return false;
			}
			int cnt = 0;
			if (!(params == null || params.isEmpty())) {
				for (String key : this.params.keySet()) {
					for (String string : pvs) {
						int index = string.indexOf("=");
						String param = string;
						if (index > -1) {
							param = string.substring(0, index);
						}

						if (key.equals(param)) {
							if (this.params.get(key) == null) {
								++cnt;
							} else {
								String val = string.substring(index + 1,
										string.length());
								if (!(((String) this.params.get(key))
										.equals(val)))
									continue;
								++cnt;
							}
						}
					}
				}
			}
			return (cnt == this.params.size());
		}
	}

	public static class URLObj {

		private String domain;
		private ListTrie paths;

		public void addUrl(String url, String name, String siteType,
				String otherClasses[]) {
			String p = URLAnalyzCli.getPath(url);
			List list = new LinkedList();
			URLStructure urlTransObj = new URLStructure(siteType, name, url,otherClasses);
			list.add(urlTransObj);
			paths.insert(p, list);
		}

		private void initTrie(String s) {
		}

		public String getDomain() {
			return domain;
		}

		public void setDomain(String domain) {
			this.domain = domain;
		}

		public URLObj(String url, String name, String siteType,String otherClasses[]) {
			String p = URLAnalyzCli.getPath(url);
			Map mp = new HashMap();
			List list = new LinkedList();
			URLStructure urlTransObj = new URLStructure(siteType, name, url,otherClasses);
			list.add(urlTransObj);
			mp.put(p, list);
			paths = new ListTrie();
			paths.insert(p, list);
			domain = URLAnalyzCli.getDomain(url);
		}
	}

	protected Trie urlRuleTrie;
	private static URLAnalyzCli client = null;

	public static URLAnalyzCli getInstance() {
		if (client == null)
			synchronized (URLAnalyzCli.class) {
				if (client == null)
					client = new URLAnalyzCli();
			}
		return client;
	}

	protected URLAnalyzCli() {
		urlRuleTrie = null;
		try {
			init(DICT_NAME);
		} catch (Exception e) {
			LOG.error(e);
		}
	}

	@SuppressWarnings("unchecked")
	private void init(String dictName) throws IOException {
		String propertyName="ire.tag.path";
		InputStream is;
		is = RourseCenter.getResourceAsInputSteam(propertyName,dictName);

		if (is == null) {
			LOG.error((new StringBuilder())
					.append("load dictfile error! dictFile ").append(dictName)
					.append(" is not found! ").toString());
			return;
		}
		try {
			List lines = FileHandler.readFileToList(is);
			Map<String,URLObj> urlRule2nodeMap = new HashMap<String,URLObj>();
			Iterator it = lines.iterator();
			do {
				if (!it.hasNext())
					break;
				String line = (String) it.next();
				if (!line.isEmpty() && !line.startsWith("#")) {
					String split[] = line.split("\\|");
					if (split.length < 2) {
						System.err.println((new StringBuilder())
								.append("error data").append(split.length)
								.append("\t").append(line).toString());
					} else {
						String urlRule = split[2].trim().toLowerCase();
						String sitetype = split[0].trim().toLowerCase();
						String name = split[1].trim().toLowerCase();
						String oc[] = (String[]) (String[]) ArrayUtils
								.subarray(split, 3, split.length);
						String domain = getDomain(urlRule);
						URLObj urlObj =  urlRule2nodeMap.get(domain);
						if (urlObj == null) {
							urlObj = new URLObj(urlRule, name, sitetype, oc);
							urlRule2nodeMap.put(domain, urlObj);
						} else
							urlObj.addUrl(urlRule, name, sitetype, oc);
					}
				}
			} while (true);
			this.urlRuleTrie = new Trie(urlRule2nodeMap);
			is.close();
		} catch (Exception e) {
			LOG.error((new StringBuilder())
					.append("load url rule file failed! file: ")
					.append(dictName).append(e.getMessage()).toString());
			LOG.error(e);
		}
		return;
	}

	@SuppressWarnings("unchecked")
	public List<URLStructure> match(String input) {
		if ((this.urlRuleTrie == null) || (input == null))
			return null;

		input = input.trim().toLowerCase();
		if (input.startsWith("http://"))
			input = input.replace("http://", "");
		else if (input.startsWith("https://"))
			input = input.replace("https://", "");
		String path = getPath(input);
		String domain = getDomain(input);
		String paramsStr = getParams(input);

		String _domain = domain;
		URLObj urlObj = null;
		while (urlObj == null) {
			urlObj = (URLObj) this.urlRuleTrie.findLongestline(_domain);
			if (urlObj == null);
			String[] arr = _domain.split("\\.");
			if (arr.length <= 1)
				return null;
			arr = (String[]) Arrays.copyOfRange(arr, 1, arr.length);
			_domain = StringUtils.join(arr, ".");
		}

		String _path = path;
		List res = null;
		List ret = new LinkedList();

/*		res = (List<URLStructure>) urlObj.paths.find(path);
		if (res != null) {
			for (URLStructure urlTransObj : (List<URLStructure>) res) {
				if (urlTransObj.matchUrlParam(paramsStr)) {
					ret.add(urlTransObj);
				}
			}
			if (!(ret.isEmpty()))
				return ret;
			res = null;

			String[] arr = _path.split("/");
			arr = (String[]) Arrays.copyOfRange(arr, 0, arr.length - 1);
			_path = StringUtils.join(arr, "/");
			if (_path.trim().length() == 0) {
				_path = "/";
			}
		}*/

		while (res == null) {
			res = (List<URLStructure>) urlObj.paths.findLongestline(_path);
			if (res != null) {
				for (URLStructure urlTransObj : (List<URLStructure>) res) {
					if (urlTransObj.matchUrlParam(paramsStr)) {
						ret.add(urlTransObj);
					}
				}
				if (!(ret.isEmpty()))
					return ret;
			}
			if (_path.equals("/"))
				return null;

			String[] arr = _path.split("/");
			arr = (String[]) Arrays.copyOfRange(arr, 0, arr.length - 1);
			_path = StringUtils.join(arr, "/");
			if (_path.trim().length() == 0) {
				_path = "/";
			}
		}
		return null;
	}

	public String translateUrl(String input) {
		List urlTransObjs = match(input);
		if (urlTransObjs == null || urlTransObjs.isEmpty())
			return null;
		
		 input=input.indexOf("?")>0 ? input.substring(0, input.indexOf("?")): input ;
		
		for (Iterator it = urlTransObjs.iterator(); it.hasNext();) {
			URLStructure u = (URLStructure) it.next();
			String rule = u.getSiteType();
			
			StringBuilder sb = new StringBuilder(u.getWebSite());
			if(rep(input, u.getUrl()+rule)){
			String arr$[] = u.getOtherParams();
			int len$ = arr$.length;
			int i = 0;
			while (i < len$) {
				String c = arr$[i];
				sb.append("|").append(c);
				i++;}
			return sb.toString();
			}
		}
		return null;
	}
	
	public static  boolean rep(String str,String regex){
		if(regex==null || "".equals(regex)) return true;
		regex=regex.replaceAll("\\*", ".*").replaceAll("\\?", ".?").replaceAll("\\%", ".*");
		return Pattern.matches(regex, str);
	}
	
	public String translateUrlold(String input) {
		List urlTransObjs = match(input);
		if (urlTransObjs == null || urlTransObjs.isEmpty())
			return null;
		StringBuilder sb = new StringBuilder();
		sb.append(((URLStructure) urlTransObjs.get(0)).getWebSite());
		
		for (Iterator it = urlTransObjs.iterator(); it.hasNext();) {
			URLStructure u = (URLStructure) it.next();
			sb.append("|").append(u.getSiteType());
			String arr$[] = u.getOtherParams();
			int len$ = arr$.length;
			int i = 0;
			while (i < len$) {
				String c = arr$[i];
				sb.append("|").append(c);
				i++;
			}break;
		}

		return sb.toString();
	}

	public static String getPath(String url) {
		String arr[] = url.split("\\?")[0].split("/");
		arr = (String[]) Arrays.copyOfRange(arr, 1, arr.length);
		String p = (new StringBuilder()).append("/")
				.append(StringUtils.join(arr, "/")).toString();
		p = p.replaceAll("\\/\\/", "/");
		return p;
	}

	public static String getParams(String url) {
		String arr[] = url.split("\\?");
		if (arr.length <= 1)
			return null;
		else
			return arr[1];
	}

	public static String getDomain(String url) {
		return url.split("/")[0];
	}
}
