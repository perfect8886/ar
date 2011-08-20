package dsp.ar.test;

import java.util.ArrayList;
import java.util.List;

import junit.framework.TestCase;

import org.apache.log4j.Logger;

import dsp.ar.dao.IARDao;
import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.DirectorEntity;
import dsp.ar.domain.InterestModelEntity;
import dsp.ar.domain.MovieEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.domain.UserEntity;
import dsp.ar.util.AppContext;

public class TestARHibernate extends TestCase {
	private static final Logger logger = Logger
			.getLogger(TestARHibernate.class);
	private IARDao arDao;

	protected void setUp() throws Exception {
		super.setUp();
		if (arDao == null) {
			logger.info("setup begin..");
			arDao = (IARDao) AppContext.getBean("arDao");
			logger.info("setup end.");
		}
	}

	protected void tearDown() throws Exception {
		super.tearDown();
	}

	public void testCreateUserEntity() {
		InterestModelEntity interestModel = new InterestModelEntity();
		interestModel.setTotal(1);
		interestModel.setActionFactor(1);
		UserEntity user = new UserEntity();
		user.setInterestModel(interestModel);
		arDao.createUserEntity(user);
	}

	public void testUpdateActor() {
		MovieEntity movie1 = new MovieEntity();
		movie1.setDesc("boxing");
		movie1.setImageUrl("http://...");
		movie1.setName("boxing2");
		movie1.setPublishTime("1999");

		MovieEntity movie2 = new MovieEntity();
		movie2.setDesc("boxing");
		movie2.setImageUrl("http://...");
		movie2.setName("boxing3");
		movie2.setPublishTime("1999");

		ActorEntity actor = arDao.getActorByName("牛津");

		List<MovieEntity> movies = actor.getMovies();

		movies.add(movie1);
		movies.add(movie2);

		actor.setMovies(movies);

		arDao.createActorEntity(actor);
	}

	public void testCreateMovie() {

		List<MovieEntity> movies = new ArrayList<MovieEntity>();
		MovieEntity movie1 = new MovieEntity();
		movie1.setDesc("boxing");
		movie1.setImageUrl("http://...");
		movie1.setName("boxing");
		movie1.setPublishTime("1999");
		movies.add(movie1);

		// actors
		List<ActorEntity> actors = new ArrayList<ActorEntity>();
		ActorEntity actor1 = new ActorEntity();
		ActorEntity actor2 = new ActorEntity();
		actor1.setMovies(movies);
		actor1.setName("牛津");
		actor2.setMovies(movies);
		actor2.setName("juzm");
		actors.add(actor1);
		actors.add(actor2);

		// directors
		List<DirectorEntity> directors = new ArrayList<DirectorEntity>();
		DirectorEntity director1 = new DirectorEntity();
		director1.setName("wangxg");
		director1.setMovies(movies);
		directors.add(director1);

		// type
		List<TypeEntity> types = new ArrayList<TypeEntity>();
		TypeEntity type1 = new TypeEntity();
		TypeEntity type2 = new TypeEntity();
		type1.setName("action");
		type1.setMovies(movies);
		type2.setName("love");
		type2.setMovies(movies);
		types.add(type1);
		types.add(type2);

		movie1.setActors(actors);
		movie1.setTypes(types);
		movie1.setDirectors(directors);

		int id = arDao.createMovieEntity(movie1);

		logger.info("movie id : " + id);
	}

	public void testFindMovies() {
		String movie = "boxing";

		String actor1 = "niujin";
		String actor2 = "juzm";
		List<String> actorList = new ArrayList<String>();
		actorList.add(actor1);
		actorList.add(actor2);

		String director1 = "wangxg";
		List<String> directorList = new ArrayList<String>();
		directorList.add(director1);

		String type1 = "action";
		String type2 = "love";
		List<String> typeList = new ArrayList<String>();
		typeList.add(type1);
		typeList.add(type2);

		// List<MovieEntity> movies1 = arDao.findMovies(null, null, null, null);
		// System.out.println("1 query : " + movies1.size());
		// List<MovieEntity> movies2 = arDao.findMovies(null, actorList, null,
		// null);
		// System.out.println("2 query : " + movies2.size());
		// List<MovieEntity> movies3 = arDao.findMovies(movie, actorList, null,
		// null);
		// System.out.println("3 query : " + movies3.size());
		// List<MovieEntity> movies4 = arDao.findMovies(movie, actorList,
		// typeList, directorList);
		// System.out.println("4 query : " + movies4.size());

		// MovieEntity movie1 = movies4.get(0);
		// System.out.println(movie1.getActors().size());
		// System.out.println(arDao.getMovieDirectors(movie1.getId()).size());
	}

	public void testGetActorByName() {
		ActorEntity actor = arDao.getActorByName("牛津");
		System.out.println("actor : " + actor.getName());
		System.out.println("movies : " + actor.getMovies().size());

		MovieEntity movie = new MovieEntity();
		List<ActorEntity> actors = new ArrayList<ActorEntity>();
		actors.add(actor);
		movie.setActors(actors);
		movie.setName("wow");
		List<MovieEntity> movies = actor.getMovies();
		movies.add(movie);
		actor.setMovies(movies);
		arDao.createMovieEntity(movie);
	}
}