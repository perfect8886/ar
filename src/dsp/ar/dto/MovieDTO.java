package dsp.ar.dto;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;

public class MovieDTO implements Serializable {
	private int id;
	private String name;
	private String imageUrl;
	private String desc;
	private String publishTime;
	private List<String> actors;
	private List<String> directors;
	private List<String> types;
	private List<String> areas;

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

	public static void main(String[] args) {
		List<String> actors = new ArrayList<String>();
		actors.add(new String("niujin"));
		actors.add(new String("wangxg"));
		MovieDTO movie = new MovieDTO();
		movie.setActors(actors);
		movie.setId(10000001);
		movie.setName("hot hot");

		List<MovieDTO> list = new ArrayList<MovieDTO>();
		list.add(movie);
		list.add(movie);
		Gson gson = new Gson();
		String json = gson.toJson(list);
		System.out.println(json);
		json = gson.toJson(movie);
		System.out.println(json);
	}
}
