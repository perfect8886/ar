package dsp.ar.crawler.module.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

import dsp.ar.crawler.module.AbstractCrawler;

public class CrawlerAdPic extends AbstractCrawler {
	public Pattern pattern;
	public int i;

	public CrawlerAdPic(String startSite, String patern) {
		super(startSite, patern);
		pattern = Pattern.compile(patern);
	}

	public void processHtml(String url) throws ParserException, Exception {
		searchedSite.add(url);
		System.out.println("searching ... :" + url);
		String directoryName = "D:/temp/ad/";
		// System.out.println("html pages save to : " + directoryName);
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}

		// save the html page
		if (savePage) {
			saveContent(directoryName, url);
		}

		String adRecordFilename = directoryName + "adRecord" + i + ".txt";
		File adRecord = new File(adRecordFilename);
		if (!adRecord.exists()) {
			adRecord.createNewFile();
		} else if (adRecord.length() > 1024 * 1024) {
			i++;
			processHtml(url);
			return;
		}
		FileWriter fileWriter = new FileWriter(adRecord, true);
		writer = new PrintWriter(fileWriter);

		URL urldb = new URL(url);
		HttpURLConnection conn = (HttpURLConnection) urldb.openConnection();
		conn.setRequestProperty("User-Agent",
				"Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
		conn.connect();
		parser.setConnection(conn);
		// parser.setURL(url);
		parser.setEncoding("UTF-8");
		// URLConnection uc = parser.getConnection();

		// uc.getLastModified();
		NodeIterator nit = parser.elements();

		while (nit.hasMoreNodes()) {
			Node node = nit.nextNode();
			parserNode(node);
		}
		writer.close();
		fileWriter.close();
	}

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
				Matcher matcher = pattern.matcher(link);
				String link_end_ = link.endsWith("/") ? link.substring(0, link
						.lastIndexOf("/")) : (link + "/");
				if (!isSearched(searchedSite, link) && matcher.matches()
						&& !queue.contains(link) && !queue.contains(link_end_)) {
					if (queue.add(link)) {
						System.out.println(link);// 打印加入链接
					}
				}
			}
		}
	}

	public void parserNode(Node node) throws Exception {
		if (node instanceof Tag) {// 判断是否是标签库结点
			Tag atag = (Tag) node;

			if (atag instanceof LinkTag) {// 判断是否是标LINK结点
				LinkTag linkatag = (LinkTag) atag;
				// System.out.println("Link : " + atag.getText());
				checkLink(linkatag.getLink(), super.getLinkList());
			}

			// image
			if (atag instanceof ImageTag) {
				ImageTag image = (ImageTag) atag;
				String alt = image.getAttribute("alt");
				writer.write("标题:" + alt.replaceAll("图片缩略图", "") + "\n");
				writer.write("图片:" + image.getImageURL() + "\n");
				writer.write("\n");
			}
			dealTag(atag);
		}
	}

	public static void main(String[] args) {
		CrawlerAdPic crawler = new CrawlerAdPic(
				"http://www.niutuku.com/hd/guanggaotupian/",
				"http://www.niutuku.com/hd/guanggaotupian/index([0-9]{1,2}).html");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}
