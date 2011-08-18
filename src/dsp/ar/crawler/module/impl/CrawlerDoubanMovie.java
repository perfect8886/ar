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
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

import dsp.ar.crawler.module.AbstractCrawler;

public class CrawlerDoubanMovie extends AbstractCrawler {
	public Pattern pattern;
	public int i;

	public CrawlerDoubanMovie(String startSite, String patern) {
		super(startSite, patern);
		pattern = Pattern.compile(patern);
	}

	public void processHtml(String url) throws ParserException, Exception {
		searchedSite.add(url);
		System.out.println("searching ... :" + url);
		String directoryName = "D:/temp/douban/";
		// System.out.println("html pages save to : " + directoryName);
		File directory = new File(directoryName);
		if (!directory.exists()) {
			directory.mkdir();
		}

		// save the html page
		if (savePage) {
			saveContent(directoryName, url);
		}

		String moiveRecordFilename = directoryName + "movieRecord" + i + ".txt";
		File movieRecord = new File(moiveRecordFilename);
		if (!movieRecord.exists()) {
			movieRecord.createNewFile();
		} else if (movieRecord.length() > 1024 * 1024) {
			i++;
			processHtml(url);
			return;
		}
		FileWriter fileWriter = new FileWriter(movieRecord, true);
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
			// title
			if (atag instanceof TitleTag) {
				TitleTag title = (TitleTag) atag;
				// System.out.println("标题:" + weap(title.getTitle()));
				writer.write("\n");
				writer.write("标题:" + weap(title.getTitle()) + "\n");
			}

			// image
			if (atag instanceof ImageTag) {
				ImageTag image = (ImageTag) atag;
				String rel = image.getAttribute("rel");
				if (rel != null && rel.equalsIgnoreCase("v:image")) {
					writer.write("图片:" + image.getImageURL() + "\n");
				}
			}
			// core info
			// String rel = atag.getAttribute("rel");
			// if (rel != null) {
			// if (rel.equalsIgnoreCase("v:directedBy")) {
			// System.out.print("导演：");
			// System.out.println(atag.toPlainTextString().trim());
			// } else if (rel.equalsIgnoreCase("v:starring")) {
			// System.out.print("主演：");
			// System.out.println(atag.toPlainTextString().trim());
			// } else if (rel.equalsIgnoreCase("v:genre")) {
			// System.out.print("类型：");
			// System.out.println(atag.toPlainTextString().trim());
			// }
			// }

			// other info
			String className = atag.getAttribute("class");
			if (className != null) {
				if (className.equalsIgnoreCase("pl")) {
					String text = atag.toPlainTextString().trim();
					if (text.startsWith("导演") || text.startsWith("主演")
							|| text.startsWith("类型")
							|| text.startsWith("制片国家/地区")
							|| text.startsWith("上映日期")
							|| text.startsWith("首播日期")) {
						String out = text;
						while ((node = node.getNextSibling()) != null) {
							String temp = node.toPlainTextString();
							if (checkEnd(temp)) {
								break;
							}
							out += temp;
						}
						// System.out.println(weapBlank(out));
						writer.write(weapBlank(out) + "\n");
					}
				}
			}
			dealTag(atag);
		}
	}

	public boolean checkEnd(String in) {
		String temp = in.trim();
		if (temp.startsWith("导演") || temp.startsWith("主演")
				|| temp.startsWith("类型") || temp.startsWith("制片国家/地区")
				|| temp.startsWith("语言") || temp.startsWith("上映日期")
				|| temp.startsWith("片长") || temp.startsWith("官方网站")
				|| temp.startsWith("又名") || temp.startsWith("IMDb链接")
				|| temp.startsWith("官方小站") || temp.startsWith("集数")
				|| temp.startsWith("单集片长")) {
			return true;
		}
		return false;
	}

	public String weapBlank(String in) {
		return in.replaceAll("\\s*|\t|\r|\n", "");
	}

	public String weap(String in) {
		return in.trim().replaceAll("\\(豆瓣\\)", "");
	}

	public static void main(String[] args) {
		CrawlerDoubanMovie crawler = new CrawlerDoubanMovie(
				"http://movie.douban.com/",
				"http://movie.douban.com/subject/([0-9]{1,8})/");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}