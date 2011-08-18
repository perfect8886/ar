package dsp.ar.crawler.module.impl;

public class CrawlerXunleiComedyMovie extends CrawlerXunleiActionMovie {
	CrawlerXunleiComedyMovie(String startSite, String patern) {
		super(startSite, patern);
	}

	public static void main(String[] args) {
		CrawlerXunleiActionMovie crawler = new CrawlerXunleiActionMovie(
				"http://movie.xunlei.com/type,genre,style/movie,Comedy,list/",
				"http://movie.xunlei.com/type,genre,style/movie,Comedy,list/");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}
