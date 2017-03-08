package cn.com.iresearch.ray.urlanalyz;

public class UrlanalyOperator {
	private URLAnalyzCli client;
	private static UrlanalyOperator operator;

	public static UrlanalyOperator getInstance() {
		if (operator == null)
			synchronized (cn.com.iresearch.ray.urlanalyz.UrlanalyOperator.class) {
				if (operator == null)
					operator = new UrlanalyOperator();
			}
		return operator;
	}

	private UrlanalyOperator() {
		client = URLAnalyzCli.getInstance();
	}

	public String tagUrl(String url) {
		return client.translateUrl(url);
	}
	
	public static void main(String[] args) {
		System.out.println(UrlanalyOperator.getInstance().tagUrl("api.3g.tudou.com/pianku/videosserachtest?tags=120"));
	    System.out.println(UrlanalyOperator.getInstance().tagUrl("api.3g.tudou.com/pianku/videosxxxirexxxxxx?tags=120"));
		System.out.println(UrlanalyOperator.getInstance().tagUrl("api.3g.tudou.com/pianku/videosxxxxiex?tags=121"));
//		System.out.println(UrlanalyOperator.getInstance().tagUrl("list.jd.com/list.html?cat=111"));
//		System.out.println(UrlanalyOperator.getInstance().tagUrl("list.jd.com/list.html?cat=9987,10973,4811"));
//		System.out.println(UrlanalyOperator.getInstance().tagUrl("list.jd.com/list.html?cat=9987,10973,4812"));
//		System.out.println(UrlanalyOperator.getInstance().tagUrl("list.jd.com/list.html?cat=9987,10973,4812/nihao"));
//		System.out.println(UrlanalyOperator.getInstance().tagUrl("list.jd.com/list.html?cat=9987,10973,4813"));
	}
}
