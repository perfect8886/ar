package dsp.ar.crawler.test;

import java.io.BufferedReader;
import java.io.FileOutputStream;
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
import org.htmlparser.filters.StringFilter;
import org.htmlparser.nodes.RemarkNode;
import org.htmlparser.nodes.TextNode;
import org.htmlparser.tags.Div;
import org.htmlparser.tags.ImageTag;
import org.htmlparser.tags.LinkTag;
import org.htmlparser.tags.TitleTag;
import org.htmlparser.util.NodeIterator;
import org.htmlparser.util.NodeList;
import org.htmlparser.util.ParserException;

public class Spider implements Runnable {
	int total = 0;

	boolean search_key_words = false;
	int count = 0;
	int limitsite = 10;
	int countsite = 1;
	String keyword = "movie";// 搜索关键字
	Parser parser = new Parser();
	// List linklist = new ArrayList();
	String startsite = "";// 搜索的起始站点
	SearchResultBean srb;// 保存搜索结果
	List<SearchResultBean> resultlist = new ArrayList<SearchResultBean>();// 搜索到关键字链接列表
	List<String> searchedsite = new ArrayList<String>();// 已经被搜索站点列表
	Queue<String> linklist = new LinkedList<String>();// 需解析的链接列表
	HashMap<String, ArrayList<String>> disallowListCache = new HashMap<String, ArrayList<String>>();
	private int totalNum = 1;

	public Spider(String keyword, String startsite) {
		this.keyword = keyword;
		this.startsite = startsite;
		linklist.add(startsite);
		srb = new SearchResultBean();
	}

	public void run() {
		// TODO Auto-generated method stub
		search(linklist);
	}

	public void search(Queue<String> queue) {
		String url = "";
		while (!queue.isEmpty()) {
			url = queue.peek().toString();// 查找列队
			try {
				if (!isSearched(searchedsite, url)) {
					if (isRobotAllowed(new URL(url)))// 检查该链接是否被允许搜索
						processHtml(url);
					else
						System.out.println("this page is disallowed to search");
				}
			} catch (Exception ex) {
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
		searchedsite.add(url);
		count = 0;
		// System.out.println("searching ... :" + url);
		saveContent("D:/temp/", url);
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
		srb.setKeywords(keyword);
		srb.setUrl(url);
		srb.setCount_key_words(count);
		resultlist.add(srb);
		// System.out.println("count keywords is :" + count);
		// System.out.println("----------------------------------------------");
	}

	private void saveContent(String path, String url) {
		// TODO Auto-generated method stub
		try {
			URL urlStd = new URL(url);
			BufferedReader br = new BufferedReader(new InputStreamReader(urlStd
					.openStream()));
			PrintWriter pw = new PrintWriter(new OutputStreamWriter(
					new FileOutputStream(path + "movie_new" + totalNum
							+ ".html")), true);
			String in = null;
			while ((in = br.readLine()) != null) {
				pw.println(in);
				// System.out.println(in);
			}
			totalNum++;
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
		if (node instanceof TextNode) {// 判断是否是文本结点
			TextNode sNode = (TextNode) node;
			StringFilter sf = new StringFilter(keyword, false);
			search_key_words = sf.accept(sNode);
			if (search_key_words) {
				count++;
			}

			String text = sNode.getText().trim();
			// if (text.equals("导演：") || text.equals("导演:")) {
			// String name = sNode.getNextSibling().getText().trim();
			// Node next = null;
			// while (!name.equals("主演:")) {
			// System.out.println(name);
			// next = next.getNextSibling();
			// name = next.getText().trim();
			// }
			// }
			if (text != "" && text != null && text.length() > 0) {
				// System.out.println("TextNode : " + text);
				// if(text.equals("导演:")){
				// status = 0;
				// } else if(text.equals("主演:")){
				// status = 1;
				// }
			}
		} else if (node instanceof Tag) {// 判断是否是标签库结点
			Tag atag = (Tag) node;
			if (atag instanceof TitleTag) {// 判断是否是标TITLE结点
				// System.out.println("Title : " + atag.getText());
				srb.setTitle(atag.getText());
			}
			if (atag instanceof LinkTag) {// 判断是否是标LINK结点
				LinkTag linkatag = (LinkTag) atag;
				// System.out.println("Link : " + atag.getText());
				checkLink(linkatag.getLink(), linklist);
			}
			if (atag instanceof Div) {
				Div div = (Div) atag;
				String className = div.getAttribute("class");
				if (className != null && className.startsWith("movList")) {
					// System.out
					// .println(
					// "<--------------------Movie Info-------------------->");
					// System.out.println(div.getChildCount());
					// System.out.println(div.getChildrenHTML());
					for (int i = 0; i < div.getChildCount(); i++) {
						Node ul = div.getChild(i);
						if (ul instanceof Tag) {
							Tag ulTag = (Tag) ul;
							// System.out.println(ulTag.getTagName());
							// System.out.println(ulTag.getChildren().size());
							for (int j = 0; j < ulTag.getChildren().size(); j++) {
								Node li = ulTag.getChildren().elementAt(j);
								if (li instanceof Tag) {
									Tag liTag = (Tag) li;
									// System.out.println(liTag.getTagName());
									//System.out.println(liTag.getChildren().size
									// ());
									// System.out.println("Movie"
									// + total++);
									System.out.println();
									for (int k = 0; k < liTag.getChildren()
											.size(); k++) {
										Node multi = liTag.getChildren()
												.elementAt(k);

										if (multi instanceof Tag) {
											Tag multiTag = (Tag) multi;
											String tagName = multiTag
													.getTagName();
											if (tagName.equalsIgnoreCase("a")) {
												System.out.print("Image:");
												for (int l = 0; l < multiTag
														.getChildren().size(); l++) {
													Node image = multiTag
															.getChildren()
															.elementAt(l);
													if (image instanceof ImageTag) {
														ImageTag imageTag = (ImageTag) image;
														System.out
																.println(imageTag
																		.getImageURL()
																		.trim());
													}
												}
											} else if (tagName
													.equalsIgnoreCase("h3")) {
												System.out.print("Name:");
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
															System.out
																	.println(nameTag
																			.toPlainTextString()
																			.trim());
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
																		System.out
																				.print("Director:");
																	} else if (check
																			.equals("主演:")) {
																		System.out
																				.print("Actor:");
																	} else if (check
																			.equals("类型:")) {
																		System.out
																				.print("Type:");
																	} else if (check
																			.equals("地区:")) {
																		System.out
																				.print("Area:");
																	} else if (check
																			.equals("上映时间:")) {
																		System.out
																				.print("Publish_Time:");
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
																		System.out
																				.print(strongTag
																						.toPlainTextString());

																		if (!(detailTag
																				.getChildren()
																				.size() - 1 == m)) {
																			System.out
																					.print(",");
																		}
																	}
																}
															}
														}
														System.out.println();
													}
												}
											} else if (tagName
													.equalsIgnoreCase("p")) {
												System.out.print("Desc:");
												System.out.println(multiTag
														.toPlainTextString()
														.trim());
											}
										}
									}
								}
							}
						}
					}
				}
			}
			// else if (atag instanceof ImageTag) {
			// ImageTag imageTag = (ImageTag) atag;
			// String imageUrl = imageTag.getImageURL();
			// System.out.println("Image URL : " + imageUrl);
			// }
			dealTag(atag);
		} else if (node instanceof RemarkNode) {// 判断是否是注释
			// System.out.println("this is remark");
		}
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
				if (link.startsWith("http://movie.xunlei.com/new,movie/")
						&& !queue.contains(link) && !queue.contains(link_end_)) {
					// System.out.println(link);//打印加入链接
					queue.add(link);
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
	private boolean isRobotAllowed(URL urlToCheck) {
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

	// public String getText(String url) throws ParserException {
	// StringBean sb = new StringBean();
	//
	// // 设置不需要得到页面所包含的链接信息
	// sb.setLinks(true);
	// // 设置将不间断空格由正规空格所替代
	// sb.setReplaceNonBreakingSpaces(true);
	// // 设置将一序列空格由一个单一空格所代替
	// sb.setCollapse(true);
	// // 传入要解析的URL
	// sb.setURL(url);
	// // 返回解析后的网页纯文本信息
	// return sb.getStrings();
	// }

	public static void main(String[] args) {
		Spider ph = new Spider("?", "http://movie.xunlei.com/new,movie/");
		try {
			// ph.processHtml();
			Thread search = new Thread(ph);
			search.start();// 启动线程
		} catch (Exception ex) {
		}
	}
}