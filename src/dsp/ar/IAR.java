package dsp.ar;

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

public interface IAR {
	/** Movie Management */
	public String listMoiveToJson(int start, int limit);

	public int CreateMovieEntity(MovieEntity movie);

	public List<MovieEntity> getTypeMovies(int typeId);

	public List<MovieEntity> getDirectorMovies(int directorId);

	public List<MovieEntity> getAreaMovies(int areaId);

	/** Ad Management */
	public String recommendAdToJson(int userId, int movieId);

	public int CreateAdEntity(AdEntity ad);

	public List<AdEntity> getTypeAds(int typeId);

	public List<AdEntity> getDirectorAds(int directorId);

	public List<AdEntity> getActorAds(int actorId);

	public List<AdEntity> getAreaAds(int areaId);

	public String listAdToJson(int start, int limit);

	/** Actor Management */
	public ActorEntity getActorByName(String name);

	public List<ActorEntity> getActors();

	/** Director Management */
	public DirectorEntity getDirectorByName(String name);

	public List<DirectorEntity> getDirectors();

	/** Type Management */
	public TypeEntity getTypeByName(String name);

	public List<TypeEntity> getTypes();

	/** Area Management */
	public AreaEntity getAreaByName(String name);

	public List<AreaEntity> getAreas();

	/** User Management */
	public void updateUserByAd(int adId, UserEntity user);

	public void updateUserByMovie(int movieId, UserEntity user);

	public int createUser(UserEntity user);

	public UserEntity getUserByIp(String ip);

	/** Record Management */
	public RecordEntity getLatestRecord(int userId);

	public int createRecord(RecordEntity record);

	/** Similarity Management */
	public int createMovieAdSimilarity(MovieAdSimilarityEntity similarity);

}