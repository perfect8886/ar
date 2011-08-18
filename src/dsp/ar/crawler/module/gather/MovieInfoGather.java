package dsp.ar.crawler.module.gather;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import dsp.ar.crawler.IAR;
import dsp.ar.crawler.domain.ActorEntity;
import dsp.ar.crawler.domain.DirectorEntity;
import dsp.ar.crawler.domain.MovieEntity;
import dsp.ar.crawler.domain.TypeEntity;
import dsp.ar.crawler.impl.ARImpl;

public class MovieInfoGather {
	private String path;
	private IAR ar;

	private static final String TITLE_TAG = "标题";
	private static final String IMAGE_TAG = "图片";
	private static final String TYPE_TAG = "类型";
	private static final String DIRECTOR_TAG = "导演";
	private static final String ACTOR_TAG = "主演";
	private static final String PUB_TAG1 = "上映日期";
	private static final String PUB_TAG2 = "首播日期";
	private static final String AREA_TAG = "制片国家";

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
					System.out
							.println("movie : " + movie.getName() + "create!");
					ar.CreateMovieEntity(movie);
				}
				movie = null;
				movie = new MovieEntity();
			} else if (line.startsWith(TITLE_TAG)) {
				String title = line.substring(line.indexOf(":") + 1);
				System.out.println("名称：" + title);
				movie.setName(title);
			} else if (line.startsWith(IMAGE_TAG)) {
				String imageUrl = line.substring(line.indexOf(":") + 1);
				System.out.println("图片：" + imageUrl);
				movie.setImageUrl(imageUrl);
			} else if (line.startsWith(TYPE_TAG)) {
				String[] types = line.substring(line.indexOf(":") + 1).split(
						"/");
				if (types != null && types.length > 0) {
					List<TypeEntity> typeList = new ArrayList<TypeEntity>();
					for (int i = 0; i < types.length; i++) {
						TypeEntity type = ar.getTypeByName(types[i]);
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (type == null) {
							// create a new type
							type = new TypeEntity();
							type.setName(types[i]);
						} else {
							// old one, need to add its movie set
							movies = ar.getTypeMovies(type.getId());
						}
						movies.add(movie);
						type.setMovies(movies);
						typeList.add(type);
						System.out.println("类型：" + type.getName());
					}
					movie.setTypes(typeList);
				}
			} else if (line.startsWith(DIRECTOR_TAG)) {
				String[] directors = line.substring(line.indexOf(":") + 1)
						.split("/");
				if (directors != null && directors.length > 0) {
					List<DirectorEntity> directorList = new ArrayList<DirectorEntity>();
					for (int i = 0; i < directors.length; i++) {
						DirectorEntity director = ar
								.getDirectorByName(directors[i]);
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (director == null) {
							// create a new director
							director = new DirectorEntity();
							director.setName(directors[i]);
						} else {
							// old one, need to add its movie set
							movies = ar.getDirectorMovies(director.getId());
						}
						movies.add(movie);
						director.setMovies(movies);
						directorList.add(director);
						System.out.println("导演：" + director.getName());
					}
					movie.setDirectors(directorList);
				}
			} else if (line.startsWith(ACTOR_TAG)) {
				String[] actors = line.substring(line.indexOf(":") + 1).split(
						"/");
				if (actors != null && actors.length > 0) {
					List<ActorEntity> actorList = new ArrayList<ActorEntity>();
					for (int i = 0; i < actors.length; i++) {
						ActorEntity actor = ar.getActorByName(actors[i]);
						List<MovieEntity> movies = new ArrayList<MovieEntity>();
						if (actor == null) {
							// create a new actor
							actor = new ActorEntity();
							actor.setName(actors[i]);
						} else {
							// old one, need to add its movie set
							movies = actor.getMovies();
						}
						movies.add(movie);
						actor.setMovies(movies);
						actorList.add(actor);
						System.out.println("主演：" + actor.getName());
					}
					movie.setActors(actorList);
				}
			} else if (line.startsWith(PUB_TAG1) || line.startsWith(PUB_TAG2)) {
				String publishTime = line.substring(line.indexOf(":") + 1);
				System.out.println("上映时间：" + publishTime);
				movie.setPublishTime(publishTime);
			} else if (line.startsWith(AREA_TAG)) {
				String area = line.substring(line.indexOf(":") + 1);
				System.out.println("制片国家/地区：" + area);
				movie.setArea(area);
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
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}