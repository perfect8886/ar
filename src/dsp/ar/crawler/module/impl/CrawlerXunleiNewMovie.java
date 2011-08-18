package dsp.ar.crawler.module.impl;

import org.htmlparser.Node;
import org.htmlparser.Tag;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;

import dsp.ar.crawler.module.AbstractCrawler;

public class CrawlerXunleiNewMovie extends AbstractCrawler {
	CrawlerXunleiNewMovie(String startSite, String patern) {
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
			if (atag instanceof Div) {
				Div div = (Div) atag;
				String className = div.getAttribute("class");
				if (className != null && className.startsWith("movList")) {
					for (int i = 0; i < div.getChildCount(); i++) {
						Node ul = div.getChild(i);
						if (ul instanceof Tag) {
							Tag ulTag = (Tag) ul;

							for (int j = 0; j < ulTag.getChildren().size(); j++) {
								Node li = ulTag.getChildren().elementAt(j);
								if (li instanceof Tag) {
									Tag liTag = (Tag) li;
									// System.out.println();
									writer.append("\n");
									for (int k = 0; k < liTag.getChildren()
											.size(); k++) {
										Node multi = liTag.getChildren()
												.elementAt(k);

										if (multi instanceof Tag) {
											Tag multiTag = (Tag) multi;
											String tagName = multiTag
													.getTagName();
											if (tagName.equalsIgnoreCase("a")) {
												// System.out.print("Image:");
												writer.append("Image:");
												for (int l = 0; l < multiTag
														.getChildren().size(); l++) {
													Node image = multiTag
															.getChildren()
															.elementAt(l);
													if (image instanceof ImageTag) {
														ImageTag imageTag = (ImageTag) image;
														// System.out
														// .println(imageTag
														// .getImageURL()
														// .trim());
														writer.append(imageTag
																.getImageURL()
																.trim());
														writer.append("\n");
													}
												}
											} else if (tagName
													.equalsIgnoreCase("h3")) {
												// System.out.print("Name:");
												writer.append("Name:");
												for (int l = 0; l < multiTag
														.getChildren().size(); l++) {
													Node name = multiTag
															.getChildren()
															.elementAt(l);
													if (name instanceof Tag) {
														Tag nameTag = (Tag) name;
														String tagNameInner = nameTag
																.getTagName();
														if (tagNameInner
																.equalsIgnoreCase("a")) {
															// System.out
															// .println(nameTag
															//.toPlainTextString
															// ()
															// .trim());
															writer
																	.append(nameTag
																			.toPlainTextString()
																			.trim());
															writer.append("\n");
														}
													}
												}
											} else if (tagName
													.equalsIgnoreCase("ul")) {
												for (int l = 0; l < multiTag
														.getChildren().size(); l++) {
													Node detail = multiTag
															.getChildren()
															.elementAt(l);
													if (detail instanceof Tag) {
														Tag detailTag = (Tag) detail;
														if (detailTag
																.getTagName()
																.equalsIgnoreCase(
																		"li")) {
															for (int m = 0; m < detailTag
																	.getChildren()
																	.size(); m++) {
																Node strong = detailTag
																		.getChildren()
																		.elementAt(
																				m);
																if (strong instanceof TextNode) {
																	String check = strong
																			.getText()
																			.trim();
																	if (check
																			.equals("导演:")) {
																		//System.
																		// out
																		//.print
																		// (
																		// "Director:"
																		// );
																		writer
																				.append("Director:");
																	} else if (check
																			.equals("主演:")) {
																		//System.
																		// out
																		//.print
																		// (
																		// "Actor:"
																		// );
																		writer
																				.append("Actor:");
																	} else if (check
																			.equals("类型:")) {
																		//System.
																		// out
																		//.print
																		// (
																		// "Type:"
																		// );
																		writer
																				.append("Type:");
																	} else if (check
																			.equals("地区:")) {
																		//System.
																		// out
																		//.print
																		// (
																		// "Area:"
																		// );
																		writer
																				.append("Area:");
																	} else if (check
																			.equals("上映时间:")) {
																		//System.
																		// out
																		//.print
																		// (
																		// "Publish_Time:"
																		// );
																		writer
																				.append("Publish_Time:");
																	}
																} else if (strong instanceof Tag) {
																	Tag strongTag = (Tag) strong;
																	if (strongTag
																			.getTagName()
																			.equalsIgnoreCase(
																					"a")
																			|| strongTag
																					.getTagName()
																					.equalsIgnoreCase(
																							"span")) {
																		//System.
																		// out
																		//.print
																		// (
																		// strongTag
																		// .
																		// toPlainTextString
																		// ());
																		writer
																				.append(strongTag
																						.toPlainTextString());
																		if (!(detailTag
																				.getChildren()
																				.size() - 1 == m)) {
																			// System
																			// .
																			// out
																			// .
																			// print
																			// (
																			// ","
																			// )
																			// ;
																			writer
																					.append(",");
																		}
																	}
																}
															}
														}
														// System.out.println();
														writer.append("\n");
													}
												}
											} else if (tagName
													.equalsIgnoreCase("p")) {
												// System.out.print("Desc:");
												writer.append("Desc:");
												// System.out.println(multiTag
												// .toPlainTextString()
												// .trim());
												writer.append(multiTag
														.toPlainTextString()
														.trim());
												writer.append("\n");
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

	public static void main(String[] args) {
		CrawlerXunleiNewMovie crawler = new CrawlerXunleiNewMovie(
				"http://movie.xunlei.com/new,movie/",
				"http://movie.xunlei.com/new,movie/");
		try {
			Thread search = new Thread(crawler);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}