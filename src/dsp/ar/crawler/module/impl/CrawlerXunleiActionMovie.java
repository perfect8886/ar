package dsp.ar.crawler.module.impl;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;

import dsp.ar.crawler.module.AbstractCrawler;

public class CrawlerXunleiActionMovie extends AbstractCrawler {
	CrawlerXunleiActionMovie(String startSite, String patern) {
		super(startSite, patern);
	}

	/**
	 * 处理HTML标签结点
	 * 
	 * @param node
	 * @throws Exception
	 */
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
								// System.out.print("Image:");
								writer.append("Image:");
								for (int j = 0; j < multiTag.getChildren()
										.size(); j++) {
									Node image = multiTag.getChildren()
											.elementAt(j);
									if (image instanceof ImageTag) {
										ImageTag imageTag = (ImageTag) image;
										// System.out.println(imageTag
										// .getImageURL().trim());
										writer.append(imageTag.getImageURL()
												.trim());
										writer.append("\n");
									}
								}
							} else if (tagName.equalsIgnoreCase("h4")) {
								for (int j = 0; j < multiTag.getChildren()
										.size(); j++) {
									Node title = multiTag.getChildren()
											.elementAt(j);
									if (title instanceof Tag) {
										Tag titleTag = (Tag) title;
										if (titleTag.getTagName()
												.equalsIgnoreCase("a")) {
											// System.out.print("name:");
											// System.out
											// .println(titleTag
											// .toPlainTextString()
											// .trim());
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
										if (text.startsWith("导演:")
												|| text.startsWith("年份:")) {
											// System.out.println(text);
											writer.append(text);
											writer.append("\n");
										} else if (text.startsWith("主演:")) {
											// System.out.print("Actor:");
											writer.append("Actor:");
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
														// System.out
														// .print(actorTag
														// .toPlainTextString()
														// .trim());
														writer
																.append(actorTag
																		.toPlainTextString()
																		.trim());
														if (k < detailTag
																.getChildren()
																.size() - 2) {
															// System.out
															// .print(",");
															writer.append(",");
														}
													}
												}
											}
											// System.out.println();
											writer.append("\n");
										}
									}

								}
							} else if (classNameInner != null
									&& classNameInner.trim().equalsIgnoreCase(
											"reusltbox_info")) {
								// System.out.print("Desc:");
								//System.out.println(multiTag.toPlainTextString(
								// )
								// .replaceAll("详细&gt;&gt;", ""));
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
		CrawlerXunleiActionMovie crawler = new CrawlerXunleiActionMovie(
				"http://movie.xunlei.com/type,genre,style/movie,Action,list/",
				"http://movie.xunlei.com/type,genre,style/movie,Action,list/");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}