package dsp.ar.dao;

import java.util.List;

import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.AdEntity;
import dsp.ar.domain.AreaEntity;
import dsp.ar.domain.DirectorEntity;
import dsp.ar.domain.MovieAdSimilarityEntity;
import dsp.ar.domain.MovieEntity;
import dsp.ar.domain.RecordEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.domain.UserEntity;

public interface IARDao {
	/** Movie Manage */
	public int createMovieEntity(MovieEntity movie);

	public List<MovieEntity> findMovies(String name, List<String> actorName,
			List<String> typeName, List<String> directorName,
			List<String> areaName, int start, int limit);

	public List<DirectorEntity> getMovieDirectors(int movieId);

	public List<TypeEntity> getMovieTypes(int movieId);

	public List<AreaEntity> getMovieAreas(int movieId);

	public List<MovieEntity> getTypeMovies(int typeId);

	public List<MovieEntity> getDirectorMovies(int directorId);

	public List<MovieEntity> getAreaMovies(int areaId);

	public long getMovieCount();

	public MovieEntity getMovieById(int id);

	/** Ad Manage */
	public int createAdEntity(AdEntity ad);

	public List<AdEntity> findAds(String name, List<String> actorName,
			List<String> typeName, List<String> directorName,
			List<String> areaName, int start, int limit);

	public List<ActorEntity> getAdActors(int adId);

	public List<DirectorEntity> getAdDirectors(int adId);

	public List<TypeEntity> getAdTypes(int adId);

	public List<AreaEntity> getAdAreas(int adId);

	public List<AdEntity> getDirectorAds(int directorId);

	public List<AdEntity> getTypeAds(int typeId);

	public List<AdEntity> getActorAds(int actorId);

	public List<AdEntity> getAreaAds(int areaId);

	public long getAdCount();

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
	public int createTypeEntity(TypeEntity type);

	public TypeEntity getTypeByName(String type);

	public List<TypeEntity> getTypes();

	/** Area Manage */
	public int createAreaEntity(AreaEntity area);

	public AreaEntity getAreaByName(String area);

	public List<AreaEntity> getAreas();

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