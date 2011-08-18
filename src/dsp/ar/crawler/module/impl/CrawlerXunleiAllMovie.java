package dsp.ar.crawler.module.impl;

public class CrawlerXunleiAllMovie extends CrawlerXunleiActionMovie {
	CrawlerXunleiAllMovie(String startSite, String patern) {
		super(startSite, patern);
		this.savePage = false;
	}

	public static void main(String[] args) {
		CrawlerXunleiAllMovie crawler = new CrawlerXunleiAllMovie(
				"http://movie.xunlei.com/type,style/movie,list/",
				"http://movie.xunlei.com/type,style/movie,list/");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}