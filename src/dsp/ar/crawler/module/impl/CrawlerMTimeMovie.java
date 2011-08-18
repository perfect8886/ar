package dsp.ar.crawler.module.impl;

import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.net.URLConnection;
import java.util.Queue;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.ParserException;

import dsp.ar.crawler.module.AbstractCrawler;

public class CrawlerMTimeMovie extends AbstractCrawler {
	public Pattern pattern;
	public int i;

	public CrawlerMTimeMovie(String startSite, String patern) {
		super(startSite, patern);
		pattern = Pattern.compile(patern);
	}

	public void processHtml(String url) throws ParserException, Exception {
		searchedSite.add(url);
		System.out.println("searching ... :" + url);
		String directoryName = "D:/temp/mtime/";
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
		parser.setURL(url);
		parser.setEncoding("UTF-8");
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
				// System.out.println(weap(title.getTitle()));
				writer.write("\n"+"名称：");
				writer.write(weap(title.getTitle()) + "\n");
			}

			if (atag instanceof Tag) {
				String className = atag.getAttribute("class");
				// part two
				if (className != null
						&& "fl pl12 movie_film_info_list lh20".equals(className
								.trim())) {
					for (int i = 0; i < atag.getChildren().size(); i++) {
						// li
						Node node1 = atag.getChildren().elementAt(i);
						if (node1 instanceof Tag) {
							Tag tag1 = (Tag) node1;
							if (tag1.getTagName().equalsIgnoreCase("li")) {
								String text = tag1.toPlainTextString().trim();
								if (text.startsWith("国家/地区")
										|| text.startsWith("分级")
										|| text.startsWith("对白语言")) {
									// System.out.println(weap(text));
									writer.write(weap(text) + "\n");
								}
							}
						}
					}
				}
				// part one
				if (className != null
						&& "clearfix __r_c_".equals(className.trim())) {
					for (int i = 0; i < atag.getChildren().size(); i++) {
						Node node1 = atag.getChildren().elementAt(i);
						if (node1 instanceof Tag) {
							Tag tag1 = (Tag) node1;
							String className1 = tag1.getAttribute("class");
							if (tag1.getTagName().trim().equalsIgnoreCase("a")) {
								for (int j = 0; j < node1.getChildren().size(); j++) {
									Node node2 = node1.getChildren().elementAt(
											j);
									if (node2 instanceof ImageTag) {
										ImageTag imageTag = (ImageTag) node2;
										// System.out.print("Image:");
										// System.out.println(imageTag
										// .getImageURL());
										writer.write("Image:"
												+ weap(imageTag.getImageURL())
												+ "\n");
									}
								}
							} else if (className1 != null
									&& className1
											.equalsIgnoreCase("fr movie_film_r")) {
								for (int j = 0; j < node1.getChildren().size(); j++) {
									Node node2 = node1.getChildren().elementAt(
											j);
									// div
									if (node2 instanceof Div) {
										Div div2 = (Div) node2;
										String className2 = div2
												.getAttribute("class");
										if (className2 != null
												&& className2.trim()
														.equalsIgnoreCase(
																"clearfix")) {
											for (int k = 0; k < div2
													.getChildren().size(); k++) {
												// div
												Node node3 = div2.getChildren()
														.elementAt(k);
												if (node3 instanceof Div) {
													Div div3 = (Div) node3;
													String className3 = div3
															.getAttribute("class");
													if (div3.getChildCount() != 0
															&& (className3 == null || (className3 != null && className3
																	.equalsIgnoreCase("w345 fl")))) {
														for (int l = 0; l < div3
																.getChildren()
																.size(); l++) {
															// ul
															Node node4 = node3
																	.getChildren()
																	.elementAt(
																			l);
															if (node4 instanceof Tag) {
																for (int m = 0; m < node4
																		.getChildren()
																		.size(); m++) {
																	// li
																	Node node5 = node4
																			.getChildren()
																			.elementAt(
																					m);
																	if (node5 instanceof Tag) {
																		Tag tag5 = (Tag) node5;
																		if (tag5
																				.getTagName()
																				.equalsIgnoreCase(
																						"li")) {
																			String text = tag5
																					.toPlainTextString()
																					.trim();
																			if (text
																					.startsWith("导演")
																					|| text
																							.startsWith("主演")
																					|| text
																							.startsWith("类型")
																					|| text
																							.startsWith("上映日期")) {
																				// System
																				// .
																				// out
																				// .
																				// println
																				// (
																				// weap
																				// (
																				// text
																				// )
																				// )
																				// ;
																				writer
																						.write(weap(text)
																								+ "\n");
																			}
																		}
																	}
																}
															}
														}
													}
												}
											}
										}
									}
								}
							}
						}
					}
				}
			}
			dealTag(atag);
		}
	}

	public String weap(String in) {
		return in.trim().replaceAll("/", "").replaceAll("&nbsp", "")
				.replaceAll("&gt;", "").replaceAll("更多", "")
				.replaceAll(";", "");
	}

	public static void main(String[] args) {
		CrawlerMTimeMovie crawler = new CrawlerMTimeMovie(
				"http://movie.mtime.com/",
				"http://movie.mtime.com/([0-9]{1,6})/");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}
