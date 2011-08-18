package dsp.ar.crawler.module;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import org.htmlparser.Node;
import org.htmlparser.Parser;
import org.htmlparser.Tag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public abstract class AbstractCrawler implements Runnable {
	public int htmlNum = 1;
	public String patern = "";
	public Parser parser = new Parser();

	public String startSite = "";// 搜索的起始站点

	public List<String> searchedSite;// 已经被搜索站点列表
	public Queue<String> linkList;// 需解析的链接列表
	public HashMap<String, ArrayList<String>> disallowListCache;
	public PrintWriter writer;

	public boolean savePage = false;

	public AbstractCrawler(String startSite, String patern) {
		// 用户定义
		this.startSite = startSite;
		this.patern = patern;

		// 初始化
		htmlNum = 1;
		parser = new Parser();
		searchedSite = new ArrayList<String>();
		linkList = new LinkedList<String>();
		disallowListCache = new HashMap<String, ArrayList<String>>();

		linkList.add(startSite);
	}

	public void run() {
		search(linkList);
	}

	public void search(Queue<String> queue) {
		String url = "";
		while (!queue.isEmpty()) {
			url = queue.peek().toString();// 查找列队
			try {
				// if (!isSearched(searchedSite, url)) {
				if (isRobotAllowed(new URL(url)))// 检查该链接是否被允许搜索
					processHtml(url);
				else
					System.out.println("this page is disallowed to search");
				// }
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			queue.remove();
		}
	}

	/**
	 * 解析HTML
	 * 
	 * @param url
	 * @throws ParserException
	 * @throws Exception
	 */
	public void processHtml(String url) throws ParserException, Exception {
		searchedSite.add(url);
		System.out.println("searching ... :" + url);
		String[] split = patern.split("/");
		String directoryName = "D:/temp/" + split[split.length - 1] + "/";
		System.out.println("html pages save to : " + directoryName);
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}

		// save the html page
		if (savePage) {
			saveContent(directoryName, url);
		}
		String moiveRecordFilename = directoryName + "movieRecord.txt";
		File moiveRecord = new File(moiveRecordFilename);
		if (!moiveRecord.exists()) {
			moiveRecord.createNewFile();
		}
		FileWriter fileWriter = new FileWriter(moiveRecord, true);
		writer = new PrintWriter(fileWriter);
		parser.setURL(url);
		parser.setEncoding("GBK");
		URLConnection uc = parser.getConnection();
		uc.connect();
		// uc.getLastModified();
		NodeIterator nit = parser.elements();

		while (nit.hasMoreNodes()) {
			Node node = nit.nextNode();
			parserNode(node);
		}
		writer.close();
		fileWriter.close();
	}

	public void saveContent(String path, String url) {
		try {
			URL urlStd = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlStd
					.openStream()));
			PrintWriter pw = new PrintWriter(
					new OutputStreamWriter(new FileOutputStream(path
							+ "movie_new" + htmlNum + ".html")), true);
			String in = null;
			while ((in = br.readLine()) != null) {
				pw.println(in);
			}
			htmlNum++;
			pw.close();
			br.close();

		} catch (MalformedURLException x) {
			System.out.println("地址错误");
		} catch (IOException xx) {
			System.out.println("文件读写错误");
		}

	}

	/**
	 * 处理HTML标签
	 * 
	 * @param tag
	 * @throws Exception
	 */
	public void dealTag(Tag tag) throws Exception {
		NodeList list = tag.getChildren();
		if (list != null) {
			NodeIterator it = list.elements();
			while (it.hasMoreNodes()) {
				Node node = it.nextNode();
				parserNode(node);
			}
		}
	}

	/**
	 * 处理HTML标签结点
	 * 
	 * @param node
	 * @throws Exception
	 */
	public void parserNode(Node node) throws Exception {
	}

	/*
	 * 检查链接是否需要加入列队
	 */
	public void checkLink(String link, Queue<String> queue) {
		if (link != null && !link.equals("") && link.indexOf("#") == -1) {
			if (!link.startsWith("http://") && !link.startsWith("ftp://")
					&& !link.startsWith("www.")) {
				link = "file:///" + link;
			} else if (link.startsWith("www.")) {
				link = "http://" + link;
			}
			if (queue.isEmpty())
				queue.add(link);
			else {
				String link_end_ = link.endsWith("/") ? link.substring(0, link
						.lastIndexOf("/")) : (link + "/");
				if (!isSearched(searchedSite, link) && link.startsWith(patern)
						&& !queue.contains(link) && !queue.contains(link_end_)
						&& !link.endsWith("page1") && !link.endsWith("page1/")
						&& !link.contains("&page=1")) {
					if (queue.add(link)) {
						System.out.println(link);// 打印加入链接
					}
				}
			}
		}
	}

	/**
	 * 检查该链接是否已经被扫描
	 * 
	 * @param list
	 * @param url
	 * @return
	 */
	public boolean isSearched(List<String> list, String url) {
		String url_end_ = "";
		if (url.endsWith("/")) {
			url_end_ = url.substring(0, url.lastIndexOf("/"));
		} else {
			url_end_ = url + "/";
		}
		if (list.size() > 0) {
			if (list.indexOf(url) != -1 || list.indexOf(url_end_) != -1) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查URL是否被允许搜索
	 * 
	 * @param urlToCheck
	 * @return
	 */
	public boolean isRobotAllowed(URL urlToCheck) {
		String host = urlToCheck.getHost().toLowerCase();// 获取给出RUL的主机
		// System.out.println("主机="+host);
		// 获取主机不允许搜索的URL缓存
		ArrayList<String> disallowList = disallowListCache.get(host);
		// 如果还没有缓存,下载并缓存。
		if (disallowList == null) {
			disallowList = new ArrayList<String>();
			try {
				URL robotsFileUrl = new URL("http://" + host + "/robots.txt");
				BufferedReader reader = new BufferedReader(
						new InputStreamReader(robotsFileUrl.openStream()));
				// 读robot文件，创建不允许访问的路径列表。
				String line;
				while ((line = reader.readLine()) != null) {
					if (line.indexOf("Disallow:") == 0) {// 是否包含"Disallow:"
						String disallowPath = line.substring("Disallow:"
								.length());// 获取不允许访问路径
						// 检查是否有注释。
						int commentIndex = disallowPath.indexOf("#");
						if (commentIndex != -1) {
							disallowPath = disallowPath.substring(0,
									commentIndex);// 去掉注释
						}
						disallowPath = disallowPath.trim();
						disallowList.add(disallowPath);
					}
				}
				for (Iterator<String> it = disallowList.iterator(); it
						.hasNext();) {
					System.out.println("Disallow is :" + it.next());
				}
				// 缓存此主机不允许访问的路径。
				disallowListCache.put(host, disallowList);
			} catch (Exception e) {
				return true; // web站点根目录下没有robots.txt文件,返回真
			}
		}
		String file = urlToCheck.getFile();
		// System.out.println("文件getFile()="+file);
		for (int i = 0; i < disallowList.size(); i++) {
			String disallow = disallowList.get(i);
			if (file.startsWith(disallow)) {
				return false;
			}
		}
		return true;
	}

	public int getHtmlNum() {
		return htmlNum;
	}

	public void setHtmlNum(int htmlNum) {
		this.htmlNum = htmlNum;
	}

	public String getPatern() {
		return patern;
	}

	public void setPatern(String patern) {
		this.patern = patern;
	}

	public Parser getParser() {
		return parser;
	}

	public void setParser(Parser parser) {
		this.parser = parser;
	}

	public String getStartSite() {
		return startSite;
	}

	public void setStartSite(String startSite) {
		this.startSite = startSite;
	}

	public List<String> getSearchedSite() {
		return searchedSite;
	}

	public void setSearchedSite(List<String> searchedSite) {
		this.searchedSite = searchedSite;
	}

	public Queue<String> getLinkList() {
		return linkList;
	}

	public void setLinkList(Queue<String> linkList) {
		this.linkList = linkList;
	}

	public HashMap<String, ArrayList<String>> getDisallowListCache() {
		return disallowListCache;
	}

	public void setDisallowListCache(
			HashMap<String, ArrayList<String>> disallowListCache) {
		this.disallowListCache = disallowListCache;
	}
}