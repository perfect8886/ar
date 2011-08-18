package dsp.ar.crawler.dao;

import java.util.List;

import dsp.ar.crawler.domain.ActorEntity;
import dsp.ar.crawler.domain.AdEntity;
import dsp.ar.crawler.domain.DirectorEntity;
import dsp.ar.crawler.domain.MovieAdSimilarityEntity;
import dsp.ar.crawler.domain.MovieEntity;
import dsp.ar.crawler.domain.RecordEntity;
import dsp.ar.crawler.domain.TypeEntity;
import dsp.ar.crawler.domain.UserEntity;

public interface ICrawlerDao {
	/** Movie Manage */
	public int createMovieEntity(MovieEntity movie);

	public List<MovieEntity> findMovies(String name, List<String> actorName,
			List<String> typeName, List<String> directorName, int start,
			int limit);

	public List<DirectorEntity> getMovieDirectors(int movieId);

	public List<TypeEntity> getMovieTypes(int movieId);

	public List<MovieEntity> getTypeMovies(int typeId);

	public List<MovieEntity> getDirectorMovies(int directorId);

	public long getMovieCount();

	public MovieEntity getMovieById(int id);

	/** Ad Manage */
	public int createAdEntity(AdEntity ad);

	public List<AdEntity> findAds(String name, List<String> actorName,
			List<String> typeName, List<String> directorName);

	public List<ActorEntity> getAdActors(int adId);

	public List<DirectorEntity> getAdDirectors(int adId);

	public List<TypeEntity> getAdTypes(int adId);

	public List<AdEntity> getDirectorAds(int directorId);

	public List<AdEntity> getTypeAds(int typeId);

	public List<AdEntity> getActorAds(int actorId);

	public AdEntity getAdById(int id);

	/** Actor Manage */
	public int createActorEntity(ActorEntity actor);

	public ActorEntity getActorByName(String name);

	public List<ActorEntity> getActors();

	/** Director Manage */
	public int createDirectorEntity(DirectorEntity director);

	public DirectorEntity getDirectorByName(String director);

	public List<DirectorEntity> getDirectors();

	/** Type Manage */
	public int createTypeEntity(TypeEntity actor);

	public TypeEntity getTypeByName(String type);

	public List<TypeEntity> getTypes();

	/** User Manage */
	public UserEntity getUserByIp(String ip);

	public UserEntity getUserById(int id);

	public int createUserEntity(UserEntity user);

	/** Record Manage */
	public int createRecordEntity(RecordEntity record);

	public List<RecordEntity> findRecords(int userId, int movieId, int adId);

	public long getUserRecordCount(int userId);

	/** Similarity Manage */
	public int createMovieAdSimilarityEntity(MovieAdSimilarityEntity similarity);

	public List<MovieAdSimilarityEntity> getTopNAdByMovieId(int movieId, int n);
}