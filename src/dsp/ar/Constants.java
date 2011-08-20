package dsp.ar;

import java.util.HashMap;
import java.util.Map;

public class Constants {
	public static final String DATE_FORMAT = "yyyyMMddHHmmss";
	public static final String DAY_FORMAT = "HHmmss";

	public final static class Factor {
		public static final double INTEREST = 0.5;
	}

	public final static class Tag {
		public static final String TITLE = "标题";
		public static final String IMAGE = "图片";
		public static final String TYPE = "类型";
		public static final String DIRECTOR = "导演";
		public static final String ACTOR = "主演";
		public static final String PUB1 = "上映日期";
		public static final String PUB2 = "首播日期";
		public static final String AREA = "制片国家";
	}

	public static Map<String, String> TYPE_MAP = new HashMap<String, String>();

	public static void mapIntialize() {
		TYPE_MAP.put("动作", "Action");
		TYPE_MAP.put("悬疑", "Suspense");
		TYPE_MAP.put("奇幻", "Fantasy");
		TYPE_MAP.put("家庭", "Family");
		TYPE_MAP.put("喜剧", "Comedy");
		TYPE_MAP.put("动画", "Animation");
		TYPE_MAP.put("冒险", "Adventure");
		TYPE_MAP.put("科幻", "Fiction");
		TYPE_MAP.put("儿童", "Child");
		TYPE_MAP.put("剧情", "Story");
		TYPE_MAP.put("爱情", "Love");
		TYPE_MAP.put("纪录片", "Documentory");
		TYPE_MAP.put("惊悚", "Thriller");
		TYPE_MAP.put("犯罪", "Crime");
		TYPE_MAP.put("短片", "Video");
		TYPE_MAP.put("恐怖", "Terror");
		TYPE_MAP.put("战争", "War");
		TYPE_MAP.put("西部", "West");
		TYPE_MAP.put("音乐", "Music");
		TYPE_MAP.put("运动", "Sport");
		TYPE_MAP.put("历史", "History");
		TYPE_MAP.put("传记", "Biography");
		TYPE_MAP.put("武侠", "MartialArts");
		TYPE_MAP.put("同性", "Homosexual");
		TYPE_MAP.put("鬼怪", "Phantom");
		TYPE_MAP.put("古装", "AncientCostume");
		TYPE_MAP.put("歌舞", "Dance");
		TYPE_MAP.put("情色", "Sex");
		TYPE_MAP.put("荒诞", "Absurd");
		TYPE_MAP.put("新闻", "News");
		TYPE_MAP.put("黑色电影", "Black");
		TYPE_MAP.put("成人", "Adult");
		TYPE_MAP.put("灾难", "Disaster");
	}
	// public final static class Type {
	// public static final String ACTION = "动作";
	// public static final String SUSPENSE = "悬疑";
	// public static final String FANTASY = "奇幻";
	// public static final String FAMILY = "家庭";
	// public static final String COMEDY = "喜剧";
	// public static final String ANIMATION = "动画";
	// public static final String ADVENTURE = "冒险";
	// public static final String FICTION = "科幻";
	// public static final String CHILD = "儿童";
	// public static final String STORY = "剧情";
	// public static final String LOVE = "爱情";
	// public static final String DOCUMENT = "纪录片";
	// public static final String THRILLER = "惊悚";
	// public static final String CRIME = "犯罪";
	// public static final String VIDEO = "短片";
	// public static final String TERROR = "恐怖";
	// public static final String WAR = "战争";
	// public static final String WEST = "西部";
	// public static final String MUSIC = "音乐";
	// public static final String SPORT = "运动";
	// public static final String HISTORY = "历史";
	// public static final String BIOGRAPHY = "传记";
	// public static final String MARTIAL_ARTS = "武侠";
	// public static final String HOMOSEXUAL = "同性";
	// public static final String PHANTOM = "鬼怪";
	// public static final String ANCIENT_COSTUME = "古装";
	// public static final String DANCE = "歌舞";
	// public static final String SEX = "情色";
	// }
}
