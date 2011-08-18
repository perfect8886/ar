package dsp.ar.crawler;

import java.util.List;

import dsp.ar.crawler.domain.ActorEntity;
import dsp.ar.crawler.domain.AdEntity;
import dsp.ar.crawler.domain.DirectorEntity;
import dsp.ar.crawler.domain.MovieAdSimilarityEntity;
import dsp.ar.crawler.domain.MovieEntity;
import dsp.ar.crawler.domain.RecordEntity;
import dsp.ar.crawler.domain.TypeEntity;
import dsp.ar.crawler.domain.UserEntity;

public interface IAR {
	public String listMoiveToJson(int start, int limit);

	public String listAdToJson(int userId, int movieId);

	public ActorEntity getActorByName(String name);

	public DirectorEntity getDirectorByName(String name);

	public TypeEntity getTypeByName(String name);

	public int CreateMovieEntity(MovieEntity movie);

	public int CreateAdEntity(AdEntity ad);

	public List<ActorEntity> getActors();

	public List<DirectorEntity> getDirectors();

	public List<TypeEntity> getTypes();

	public List<MovieEntity> getTypeMovies(int typeId);

	public List<MovieEntity> getDirectorMovies(int directorId);

	public List<AdEntity> getTypeAds(int typeId);

	public List<AdEntity> getDirectorAds(int directorId);

	public List<AdEntity> getActorAds(int actorId);

	public int createUser(UserEntity user);

	public UserEntity getUserByIp(String ip);

	public RecordEntity getLatestRecord(int userId);

	public int createRecord(RecordEntity record);

	public int createMovieAdSimilarity(MovieAdSimilarityEntity similarity);

	public void updateUserByAd(int adId, UserEntity user);

	public void updateUserByMovie(int movieId, UserEntity user);
}