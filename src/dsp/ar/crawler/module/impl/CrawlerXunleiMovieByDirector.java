package dsp.ar.crawler.module.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.Queue;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

import dsp.ar.crawler.module.AbstractCrawler;

public class CrawlerXunleiMovieByDirector extends AbstractCrawler {
	String suffix;

	public CrawlerXunleiMovieByDirector(String startSite, String patern) {
		this(startSite, patern, "");
	}

	public CrawlerXunleiMovieByDirector(String startSite, String patern,
			String suffix) {
		super(startSite, patern);
		this.suffix = suffix;

	}

	public void processHtml(String url) throws ParserException, Exception {
		searchedSite.add(url);
		System.out.println("searching ... :" + url);
		String keyword = "张艺谋";
		String directoryName = "D:/temp/" + keyword + "/";
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
						&& link.endsWith(suffix) && !queue.contains(link)
						&& !queue.contains(link_end_)
						&& !link.contains("&page=1")) {
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
			if (atag.getTagName().equalsIgnoreCase("li")) {
				String className = atag.getAttribute("class");
				if (className != null
						&& className.trim().equalsIgnoreCase("reusltbox_list")) {
					writer.append("\n");
					for (int i = 0; i < atag.getChildren().size(); i++) {
						Node multi = atag.getChildren().elementAt(i);
						if (multi instanceof Tag) {
							Tag multiTag = (Tag) multi;
							String tagName = multiTag.getTagName();
							String classNameInner = multiTag
									.getAttribute("class");
							if (classNameInner != null
									&& classNameInner.trim().equalsIgnoreCase(
											"pic")) {
								System.out.print("Image:");
								writer.append("Image:");
								for (int j = 0; j < multiTag.getChildren()
										.size(); j++) {
									Node image = multiTag.getChildren()
											.elementAt(j);
									if (image instanceof ImageTag) {
										ImageTag imageTag = (ImageTag) image;
										System.out.println(imageTag
												.getImageURL().trim());
										writer.append(imageTag.getImageURL()
												.trim());
										writer.append("\n");
									}
								}
							} else if (tagName.equalsIgnoreCase("h2")) {
								for (int j = 0; j < multiTag.getChildren()
										.size(); j++) {
									Node title = multiTag.getChildren()
											.elementAt(j);
									if (title instanceof Tag) {
										Tag titleTag = (Tag) title;
										if (titleTag.getTagName()
												.equalsIgnoreCase("a")) {
											System.out.print("name:");
											System.out
													.println(titleTag
															.toPlainTextString()
															.trim());
											writer.append("name:");
											writer
													.append(titleTag
															.toPlainTextString()
															.trim());
											writer.append("\n");
										}
									}
								}
							} else if (classNameInner != null
									&& classNameInner.trim().equalsIgnoreCase(
											"reusltbox_list_detail")) {
								for (int j = 0; j < multiTag.getChildren()
										.size(); j++) {
									Node detailNode = multiTag.getChildren()
											.elementAt(j);
									if (detailNode instanceof Tag) {
										Tag detailTag = (Tag) detailNode;
										String text = detailTag
												.toPlainTextString().trim();
										if (text.startsWith("导演：")
												|| text.startsWith("上映日期：")) {
											System.out.println(text);
											writer.append(text);
											writer.append("\n");
										} else if (text.startsWith("主演：")
												|| text.startsWith("类型：")) {
											if (text.startsWith("主演")) {
												System.out.print("Actor:");
												writer.append("Actor:");
											} else if (text.startsWith("类型")) {
												System.out.print("Type:");
												writer.append("Type:");
											}
											for (int k = 0; k < detailTag
													.getChildren().size(); k++) {
												Node actorNode = detailTag
														.getChildren()
														.elementAt(k);
												if (actorNode instanceof Tag) {
													Tag actorTag = (Tag) actorNode;
													if (actorTag.getTagName()
															.equalsIgnoreCase(
																	"a")) {
														System.out
																.print(actorTag
																		.toPlainTextString()
																		.trim());
														writer
																.append(actorTag
																		.toPlainTextString()
																		.trim());
														if (k < detailTag
																.getChildren()
																.size() - 1) {
															System.out
																	.print(",");
															writer.append(",");
														}
													}
												}
											}
											System.out.println();
											writer.append("\n");
										}
									}
								}
							} else if (classNameInner != null
									&& classNameInner.trim().equalsIgnoreCase(
											"reusltbox_info")) {
								System.out.print("Desc:");
								System.out.println(multiTag.toPlainTextString()
										.replaceAll("详细&gt;&gt;", ""));
								writer.append("Desc:");
								writer.append(multiTag.toPlainTextString()
										.replaceAll("详细&gt;&gt;", ""));
								writer.append("\n");
							}
						}
					}
				}
			}
			dealTag(atag);
		}
	}

	public static void main(String[] args) {
		CrawlerXunleiMovieByDirector crawler = new CrawlerXunleiMovieByDirector(
				"http://search.xunlei.com/search.php?keyword=%E5%BC%A0%E8%89%BA%E8%B0%8B&t=0&sort=pub",
				"http://search.xunlei.com/search.php?keyword=%E5%BC%A0%E8%89%BA%E8%B0%8B",
				"&t=0&sort=pub");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}
