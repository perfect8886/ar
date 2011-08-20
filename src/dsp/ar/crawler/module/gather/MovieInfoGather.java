package dsp.ar.crawler.module.gather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dsp.ar.Constants;
import dsp.ar.IAR;
import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.AreaEntity;
import dsp.ar.domain.DirectorEntity;
import dsp.ar.domain.MovieEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.impl.ARImpl;

public class MovieInfoGather {
	private String path;
	private IAR ar;

	public MovieInfoGather(String path) {
		this.path = path;
		ar = new ARImpl();
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public void process() throws IOException {
		File file = new File(this.getPath());
		if (!file.exists()) {
			System.out.println("Path Error!");
			return;
		}
		System.out.println("File size : " + file.length());
		BufferedReader br = new BufferedReader(new InputStreamReader(
				new FileInputStream(file)));
		String line = null;
		MovieEntity movie = new MovieEntity();
		while ((line = br.readLine()) != null) {
			if (line.trim().equalsIgnoreCase("")) {
				if (movie.getName() != null
						&& movie.getName().trim().length() > 0) {
					// System.out
					// .println("movie : " + movie.getName() + "create!");
					ar.CreateMovieEntity(movie);
				}
				movie = null;
				movie = new MovieEntity();
			} else if (line.startsWith(Constants.Tag.TITLE)) {
				String title = line.substring(line.indexOf(":") + 1);
				// System.out.println("名称：" + title);
				movie.setName(title);
			} else if (line.startsWith(Constants.Tag.IMAGE)) {
				String imageUrl = line.substring(line.indexOf(":") + 1);
				// System.out.println("图片：" + imageUrl);
				movie.setImageUrl(imageUrl);
			} else if (line.startsWith(Constants.Tag.TYPE)) {
				String[] types = line.substring(line.indexOf(":") + 1).split(
						"/");
				if (types != null && types.length > 0) {
					List<TypeEntity> typeList = new ArrayList<TypeEntity>();
					for (int i = 0; i < types.length; i++) {
						String typeStr = types[i];
						TypeEntity type = ar.getTypeByName(typeStr);
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (type == null) {
							// create a new type
							type = new TypeEntity();
							type.setName(typeStr);
						} else {
							// old one, need to add its movie set
							movies = ar.getTypeMovies(type.getId());
						}
						movies.add(movie);
						type.setMovies(movies);
						typeList.add(type);
						// System.out.println("类型：" + type.getName());
					}
					movie.setTypes(typeList);
				}
			} else if (line.startsWith(Constants.Tag.DIRECTOR)) {
				String[] directors = line.substring(line.indexOf(":") + 1)
						.split("/");
				if (directors != null && directors.length > 0) {
					List<DirectorEntity> directorList = new ArrayList<DirectorEntity>();
					for (int i = 0; i < directors.length; i++) {
						DirectorEntity director = ar
								.getDirectorByName(directors[i].replaceAll("'",
										"·"));
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (director == null) {
							// create a new director
							director = new DirectorEntity();
							director.setName(directors[i].replaceAll("'", "·"));
						} else {
							// old one, need to add its movie set
							movies = ar.getDirectorMovies(director.getId());
						}
						movies.add(movie);
						director.setMovies(movies);
						directorList.add(director);
						// System.out.println("导演：" + director.getName());
					}
					movie.setDirectors(directorList);
				}
			} else if (line.startsWith(Constants.Tag.ACTOR)) {
				String[] actors = line.substring(line.indexOf(":") + 1).split(
						"/");
				if (actors != null && actors.length > 0) {
					List<ActorEntity> actorList = new ArrayList<ActorEntity>();
					for (int i = 0; i < actors.length; i++) {
						ActorEntity actor = ar.getActorByName(actors[i]
								.replaceAll("'", "·"));
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (actor == null) {
							// create a new actor
							actor = new ActorEntity();
							actor.setName(actors[i].replaceAll("'", "·"));
						} else {
							// old one, need to add its movie set
							movies = actor.getMovies();
						}
						movies.add(movie);
						actor.setMovies(movies);
						actorList.add(actor);
						// System.out.println("主演：" + actor.getName());
					}
					movie.setActors(actorList);
				}
			} else if (line.startsWith(Constants.Tag.AREA)) {
				String[] areas = line.substring(line.indexOf(":") + 1).split(
						"/");
				if (areas != null && areas.length > 0) {
					List<AreaEntity> areaList = new ArrayList<AreaEntity>();
					for (int i = 0; i < areas.length; i++) {
						String areaStr = areas[i].replaceAll("USA", "美国")
								.replaceAll("UK", "英国")
								.replaceAll("香港", "中国香港").replaceAll("台湾",
										"中国台湾");
						AreaEntity area = ar.getAreaByName(areaStr);
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (area == null) {
							// create a new area
							area = new AreaEntity();
							area.setName(areaStr);
						} else {
							// old one, need to add its movie set
							movies = ar.getAreaMovies(area.getId());
						}
						movies.add(movie);
						area.setMovies(movies);
						areaList.add(area);
						// System.out.println("地域：" + area.getName());
					}
					movie.setAreas(areaList);
				}
			} else if (line.startsWith(Constants.Tag.PUB1)
					|| line.startsWith(Constants.Tag.PUB2)) {
				String publishTime = line.substring(line.indexOf(":") + 1);
				// System.out.println("上映时间：" + publishTime);
				movie.setPublishTime(publishTime);
			}
		}
		System.out.println("Process end.");
	}

	public static void main(String[] args) {
		MovieInfoGather gather = new MovieInfoGather(
				"D:/temp/douban/movieRecord0.txt");
		try {
			gather.process();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}