package dsp.ar.dto;

import java.io.Serializable;
import java.util.List;

public class AdManageDTO implements Serializable {
	private int id;
	private String name;
	private String imageUrl;
	private String desc;
	private String publishTime;
	private List<String> actors;
	private List<String> directors;
	private List<String> types;
	private List<String> areas;

	private String publishStartDate;
	private String publishEndDate;

	private String startDayTime;
	private String endDayTime;

	private List<String> ipPrefix;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public List<String> getActors() {
		return actors;
	}

	public void setActors(List<String> actors) {
		this.actors = actors;
	}

	public List<String> getDirectors() {
		return directors;
	}

	public void setDirectors(List<String> directors) {
		this.directors = directors;
	}

	public List<String> getTypes() {
		return types;
	}

	public void setTypes(List<String> types) {
		this.types = types;
	}

	public List<String> getAreas() {
		return areas;
	}

	public void setAreas(List<String> areas) {
		this.areas = areas;
	}

	public String getPublishStartDate() {
		return publishStartDate;
	}

	public void setPublishStartDate(String publishStartDate) {
		this.publishStartDate = publishStartDate;
	}

	public String getPublishEndDate() {
		return publishEndDate;
	}

	public void setPublishEndDate(String publishEndDate) {
		this.publishEndDate = publishEndDate;
	}

	public String getStartDayTime() {
		return startDayTime;
	}

	public void setStartDayTime(String startDayTime) {
		this.startDayTime = startDayTime;
	}

	public String getEndDayTime() {
		return endDayTime;
	}

	public void setEndDayTime(String endDayTime) {
		this.endDayTime = endDayTime;
	}

	public List<String> getIpPrefix() {
		return ipPrefix;
	}

	public void setIpPrefix(List<String> ipPrefix) {
		this.ipPrefix = ipPrefix;
	}
}
