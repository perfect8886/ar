package dsp.ar.crawler.impl;

import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import dsp.ar.crawler.IAR;
import dsp.ar.crawler.dao.ICrawlerDao;
import dsp.ar.crawler.domain.ActorEntity;
import dsp.ar.crawler.domain.AdEntity;
import dsp.ar.crawler.domain.DirectorEntity;
import dsp.ar.crawler.domain.MovieAdSimilarityEntity;
import dsp.ar.crawler.domain.MovieEntity;
import dsp.ar.crawler.domain.RecordEntity;
import dsp.ar.crawler.domain.TypeEntity;
import dsp.ar.crawler.domain.UserEntity;
import dsp.ar.crawler.dto.AdDTO;
import dsp.ar.crawler.dto.MovieDTO;
import dsp.ar.crawler.util.AppContext;

public class ARImpl implements IAR {
	private static final Logger logger = Logger.getLogger(ARImpl.class);

	private static final double INTEREST_FACTOR = 0.5;

	private static final String ACTION = "动作";
	private static final String SUSPENSE = "悬疑";
	private static final String FANTASY = "奇幻";
	private static final String FAMILY = "家庭";
	private static final String COMEDY = "喜剧";
	private static final String ANIMATION = "动画";
	private static final String ADVENTURE = "冒险";
	private static final String FICTION = "科幻";
	private static final String CHILD = "儿童";
	private static final String STORY = "剧情";
	private static final String LOVE = "爱情";
	private static final String DOCUMENT = "纪录片";
	private static final String THRILLER = "惊悚";
	private static final String CRIME = "犯罪";
	private static final String VIDEO = "短片";
	private static final String TERROR = "恐怖";
	private static final String WAR = "战争";
	private static final String WEST = "西部";
	private static final String MUSIC = "音乐";
	private static final String SPORT = "运动";
	private static final String HISTORY = "历史";
	private static final String BIOGRAPHY = "传记";
	private static final String MARTIAL_ARTS = "武侠";
	private static final String HOMOSEXUAL = "同性";
	private static final String PHANTOM = "鬼怪";
	private static final String ANCIENT_COSTUME = "古装";
	private static final String DANCE = "歌舞";

	private ICrawlerDao crawlerDao;
	private Gson gson;

	public Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	public ICrawlerDao getICrawlerDaoService() {
		if (crawlerDao == null) {
			crawlerDao = (ICrawlerDao) AppContext.getBean("crawlerDao");
		}
		return crawlerDao;
	}

	public String listAdToJson(int userId, int movieId) {
		// 影片拟合度
		// 兴趣相似度
		// 协同推荐度 TODO

		// step 1: 获取最相似广告及其相似度 Top20
		List<MovieAdSimilarityEntity> similarityList = this
				.getICrawlerDaoService().getTopNAdByMovieId(movieId, 100);
		// step 2:
		// 在相似广告中，计算用户兴趣程度interst,并计算最终result：0.4*similarity+0.6*interest
		List<AdDTO> adTop5 = new ArrayList<AdDTO>();
		for (MovieAdSimilarityEntity similarity : similarityList) {
			AdEntity ad = this.getICrawlerDaoService().getAdById(
					similarity.getAdId());
			AdDTO adDTO = new AdDTO();
			adDTO.setId(ad.getId());
			adDTO.setMovieSimilarity(similarity.getSimilarity());
			List<TypeEntity> types = this.getICrawlerDaoService().getAdTypes(
					ad.getId());
			// 计算用户兴趣程度
			UserEntity user = this.getICrawlerDaoService().getUserById(userId);
			double interest = 0.0;
			for (TypeEntity type : types) {
				String typeName = type.getName();
				if (typeName.equalsIgnoreCase(ACTION)) {
					interest += user.getActionFactor();
				} else if (typeName.equalsIgnoreCase(SUSPENSE)) {
					interest += user.getSuspenseFactor();
				} else if (typeName.equalsIgnoreCase(FANTASY)) {
					interest += user.getFantasyFactor();
				} else if (typeName.equalsIgnoreCase(FAMILY)) {
					interest += user.getFamilyFactor();
				} else if (typeName.equalsIgnoreCase(COMEDY)) {
					interest += user.getComedyFactor();
				} else if (typeName.equalsIgnoreCase(ANIMATION)) {
					interest += user.getAnimationFactor();
				} else if (typeName.equalsIgnoreCase(ADVENTURE)) {
					interest += user.getAdventureFactor();
				} else if (typeName.equalsIgnoreCase(FICTION)) {
					interest += user.getFictionFactor();
				} else if (typeName.equalsIgnoreCase(CHILD)) {
					interest += user.getChildFactor();
				} else if (typeName.equalsIgnoreCase(STORY)) {
					interest += user.getStoryFactor();
				} else if (typeName.equalsIgnoreCase(LOVE)) {
					interest += user.getLoveFactor();
				} else if (typeName.equalsIgnoreCase(DOCUMENT)) {
					interest += user.getDocumentoryFactor();
				} else if (typeName.equalsIgnoreCase(THRILLER)) {
					interest += user.getThrillerFactor();
				} else if (typeName.equalsIgnoreCase(CRIME)) {
					interest += user.getCrimeFactor();
				} else if (typeName.equalsIgnoreCase(VIDEO)) {
					interest += user.getVideoFactor();
				} else if (typeName.equalsIgnoreCase(TERROR)) {
					interest += user.getTerrorFactor();
				} else if (typeName.equalsIgnoreCase(WAR)) {
					interest += user.getWarFactor();
				} else if (typeName.equalsIgnoreCase(WEST)) {
					interest += user.getWestFactor();
				} else if (typeName.equalsIgnoreCase(MUSIC)) {
					interest += user.getMusicFactor();
				} else if (typeName.equalsIgnoreCase(SPORT)) {
					interest += user.getSportFactor();
				} else if (typeName.equalsIgnoreCase(HISTORY)) {
					interest += user.getHistoryFactor();
				} else if (typeName.equalsIgnoreCase(BIOGRAPHY)) {
					interest += user.getBiographyFactor();
				} else if (typeName.equalsIgnoreCase(MARTIAL_ARTS)) {
					interest += user.getMartialArtsFactor();
				} else if (typeName.equalsIgnoreCase(HOMOSEXUAL)) {
					interest += user.getHomosexualFactor();
				} else if (typeName.equalsIgnoreCase(PHANTOM)) {
					interest += user.getPhantomFactor();
				} else if (typeName.equalsIgnoreCase(ANCIENT_COSTUME)) {
					interest += user.getAncientCostumeFactor();
				} else if (typeName.equalsIgnoreCase(DANCE)) {
					interest += user.getDanceFactor();
				}
			}
			interest = (double) (interest * 2);
			adDTO.setInterest(interest);

			// 计算兴趣权重，随观看次数增多而增加
			double interestFactor = 0.0;
			long userRecordCount = this.getICrawlerDaoService()
					.getUserRecordCount(userId);
			if (userRecordCount < 1) {
				interestFactor = 0.0;
			} else {
				interestFactor = (double) (INTEREST_FACTOR - 1.0 / userRecordCount);
			}
			double similarityFactor = 1 - interestFactor;

			adDTO.setResult((double) (interest * interestFactor + similarity
					.getSimilarity()
					* similarityFactor));
			adTop5.add(adDTO);
		}

		// 选择Top3 ad
		List<AdDTO> adTop3 = new ArrayList<AdDTO>();
		for (AdDTO adDTO : adTop5) {
			if (adTop3.size() < 3) {
				// 如果adTop3长度小于3，直接加入adDTO
				adTop3.add(adDTO);
			} else {
				// 遍历比较，去除最小
				double result = adDTO.getResult();
				int adId = adDTO.getId();
				int index = -1;
				for (AdDTO innerAdDTO : adTop3) {
					double temp = innerAdDTO.getResult();
					if (temp < result) {
						result = temp;
						adId = innerAdDTO.getId();
						index = adTop3.indexOf(innerAdDTO);
					}
				}
				if (adId == adDTO.getId()) {
					// 待加入的最小，不用移动adTop3
				} else {
					// 移除最小，加入最新
					adTop3.remove(index);
					adTop3.add(adDTO);
				}
			}
		}

		// 补全 AdDTO信息
		for (AdDTO adDTO : adTop3) {
			AdEntity ad = this.getICrawlerDaoService().getAdById(adDTO.getId());
			// area, desc, id, imageUrl, name
			adDTO.setArea(ad.getArea());
			adDTO.setDesc(ad.getDesc());
			adDTO.setId(ad.getId());
			adDTO.setImageUrl(ad.getImageUrl());
			adDTO.setName(ad.getName());
			adDTO.setPublishTime(ad.getPublishTime());

			// actors
			List<String> actorsStr = new ArrayList<String>();
			List<ActorEntity> actorsEntity = this.getICrawlerDaoService()
					.getAdActors(ad.getId());
			for (ActorEntity actorEntity : actorsEntity) {
				String actorStr = actorEntity.getName();
				actorsStr.add(actorStr);
			}
			adDTO.setActors(actorsStr);
			// directors
			List<String> directorsStr = new ArrayList<String>();
			List<DirectorEntity> directorsEntity = this.getICrawlerDaoService()
					.getAdDirectors(ad.getId());
			if (directorsEntity != null && directorsEntity.size() > 0) {
				for (DirectorEntity directorEntity : directorsEntity) {
					String directorStr = directorEntity.getName();
					directorsStr.add(directorStr);
				}
			}
			adDTO.setDirectors(directorsStr);

			// types
			List<String> typesStr = new ArrayList<String>();
			List<TypeEntity> typesEntity = this.getICrawlerDaoService()
					.getAdTypes(ad.getId());
			if (typesEntity != null && typesEntity.size() > 0) {
				for (TypeEntity typeEntity : typesEntity) {
					String typeStr = typeEntity.getName();
					typesStr.add(typeStr);
				}
			}
			adDTO.setTypes(typesStr);
		}
		StringBuilder jsonBuilder = new StringBuilder(this.getGson().toJson(
				adTop3));
		logger.info("---->Response : " + jsonBuilder.toString());
		return jsonBuilder.toString();
	}

	public String listMoiveToJson(int start, int limit) {
		List<MovieEntity> movieEntityList = this.getICrawlerDaoService()
				.findMovies(null, null, null, null, start, limit);
		logger.info("movie list size : " + movieEntityList.size());

		List<MovieDTO> movieDTOList = new ArrayList<MovieDTO>();
		for (MovieEntity movieEntity : movieEntityList) {
			MovieDTO movieDTO = new MovieDTO();
			// area, desc, id, imageUrl, name
			movieDTO.setArea(movieEntity.getArea());
			movieDTO.setDesc(movieEntity.getDesc());
			movieDTO.setId(movieEntity.getId());
			movieDTO.setImageUrl(movieEntity.getImageUrl());
			movieDTO.setName(movieEntity.getName());
			movieDTO.setPublishTime(movieEntity.getPublishTime());
			// actors
			List<String> actorsStr = new ArrayList<String>();
			List<ActorEntity> actorsEntity = movieEntity.getActors();
			for (ActorEntity actorEntity : actorsEntity) {
				String actorStr = actorEntity.getName();
				actorsStr.add(actorStr);
			}
			movieDTO.setActors(actorsStr);
			// directors
			List<String> directorsStr = new ArrayList<String>();
			List<DirectorEntity> directorsEntity = this.getICrawlerDaoService()
					.getMovieDirectors(movieEntity.getId());
			if (directorsEntity != null && directorsEntity.size() > 0) {
				for (DirectorEntity directorEntity : directorsEntity) {
					String directorStr = directorEntity.getName();
					directorsStr.add(directorStr);
				}
			}
			movieDTO.setDirectors(directorsStr);
			// types
			List<String> typesStr = new ArrayList<String>();
			List<TypeEntity> typersEntity = this.getICrawlerDaoService()
					.getMovieTypes(movieEntity.getId());
			if (typersEntity != null && typersEntity.size() > 0) {
				for (TypeEntity typeEntity : typersEntity) {
					String typeStr = typeEntity.getName();
					typesStr.add(typeStr);
				}
			}
			movieDTO.setTypes(typesStr);
			movieDTOList.add(movieDTO);
		}

		long total = this.getICrawlerDaoService().getMovieCount();

		logger.info("-------------------> Total Movie : " + total);
		StringBuilder jsonBuilder = new StringBuilder("{success:true,total:")
				.append(total).append(",movies:");
		jsonBuilder.append(this.getGson().toJson(movieDTOList)).append("}");
		logger.info("---->Response : " + jsonBuilder.toString());
		return jsonBuilder.toString();
	}

	public ActorEntity getActorByName(String name) {
		return this.getICrawlerDaoService().getActorByName(name);
	}

	public DirectorEntity getDirectorByName(String name) {
		return this.getICrawlerDaoService().getDirectorByName(name);
	}

	public TypeEntity getTypeByName(String name) {
		return this.getICrawlerDaoService().getTypeByName(name);
	}

	public int CreateMovieEntity(MovieEntity movie) {
		return this.getICrawlerDaoService().createMovieEntity(movie);
	}

	public int CreateAdEntity(AdEntity ad) {
		return this.getICrawlerDaoService().createAdEntity(ad);
	}

	public List<ActorEntity> getActors() {
		return this.getICrawlerDaoService().getActors();
	}

	public List<DirectorEntity> getDirectors() {
		return this.getICrawlerDaoService().getDirectors();
	}

	public List<TypeEntity> getTypes() {
		return this.getICrawlerDaoService().getTypes();
	}

	public ActorEntity getRandomActor() {
		List<ActorEntity> actors = this.getICrawlerDaoService().getActors();
		int size = actors.size();
		int random = (int) (Math.random() * size);
		return actors.get(random);
	}

	public List<MovieEntity> getDirectorMovies(int directorId) {
		return this.getICrawlerDaoService().getDirectorMovies(directorId);
	}

	public List<MovieEntity> getTypeMovies(int typeId) {
		return this.getICrawlerDaoService().getTypeMovies(typeId);
	}

	public List<AdEntity> getTypeAds(int typeId) {
		return this.getICrawlerDaoService().getTypeAds(typeId);
	}

	public List<AdEntity> getDirectorAds(int directorId) {
		return this.getICrawlerDaoService().getDirectorAds(directorId);
	}

	public List<AdEntity> getActorAds(int actorId) {
		return this.getICrawlerDaoService().getActorAds(actorId);
	}

	public int createUser(UserEntity user) {
		return this.getICrawlerDaoService().createUserEntity(user);
	}

	public UserEntity getUserByIp(String ip) {
		return this.getICrawlerDaoService().getUserByIp(ip);
	}

	public int createRecord(RecordEntity record) {
		return this.getICrawlerDaoService().createRecordEntity(record);
	}

	public RecordEntity getLatestRecord(int userId) {
		List<RecordEntity> recordList = this.getICrawlerDaoService()
				.findRecords(userId, 0, 0);
		if (recordList.size() > 0 && recordList != null) {
			RecordEntity result = recordList.get(0);
			String time = result.getTime();
			for (RecordEntity record : recordList) {
				if (time.compareTo(record.getTime()) < 0) {
					result = record;
					time = result.getTime();
				}
			}
			return result;
		}
		return null;
	}

	public int createMovieAdSimilarity(MovieAdSimilarityEntity similarity) {
		return this.getICrawlerDaoService().createMovieAdSimilarityEntity(
				similarity);
	}

	public void updateUserByAd(int adId, UserEntity user) {
		List<TypeEntity> types = this.getICrawlerDaoService().getAdTypes(adId);
		this.updateUser(types, user);
	}

	public void updateUserByMovie(int movieId, UserEntity user) {
		List<TypeEntity> types = this.getICrawlerDaoService().getMovieTypes(
				movieId);
		this.updateUser(types, user);
	}

	private void updateUser(List<TypeEntity> types, UserEntity user) {
		if (types != null && types.size() > 0) {
			int total = user.getTotal();
			int totalAfter = total + types.size();
			// set all factors
			double actionFactor = user.getActionFactor() * total / totalAfter;
			user.setActionFactor(actionFactor);
			double suspenseFactor = user.getSuspenseFactor() * total
					/ totalAfter;
			user.setSuspenseFactor(suspenseFactor);
			double fantasyFactor = user.getFantasyFactor() * total / totalAfter;
			user.setFantasyFactor(fantasyFactor);
			double familyFactor = user.getFamilyFactor() * total / totalAfter;
			user.setFamilyFactor(familyFactor);
			double comedyFactor = user.getComedyFactor() * total / totalAfter;
			user.setComedyFactor(comedyFactor);
			double animationFactor = user.getAnimationFactor() * total
					/ totalAfter;
			user.setAnimationFactor(animationFactor);
			double adventureFactor = user.getAdventureFactor() * total
					/ totalAfter;
			user.setAdventureFactor(adventureFactor);
			double fictionFactor = user.getFictionFactor() * total / totalAfter;
			user.setFictionFactor(fictionFactor);
			double childFactor = user.getChildFactor() * total / totalAfter;
			user.setChildFactor(childFactor);
			double storyFactor = user.getStoryFactor() * total / totalAfter;
			user.setStoryFactor(storyFactor);
			double loveFactor = user.getLoveFactor() * total / totalAfter;
			user.setLoveFactor(loveFactor);
			double documentoryFactor = user.getDocumentoryFactor() * total
					/ totalAfter;
			user.setDocumentoryFactor(documentoryFactor);
			double thrillerFactor = user.getThrillerFactor() * total
					/ totalAfter;
			user.setThrillerFactor(thrillerFactor);
			double crimeFactor = user.getCrimeFactor() * total / totalAfter;
			user.setCrimeFactor(crimeFactor);
			double videoFactor = user.getVideoFactor() * total / totalAfter;
			user.setVideoFactor(videoFactor);
			double terrorFactor = user.getTerrorFactor() * total / totalAfter;
			user.setTerrorFactor(terrorFactor);
			double warFactor = user.getWarFactor() * total / totalAfter;
			user.setWarFactor(warFactor);
			double westFactor = user.getWestFactor() * total / totalAfter;
			user.setWestFactor(westFactor);
			double musicFactor = user.getMusicFactor() * total / totalAfter;
			user.setMusicFactor(musicFactor);
			double sportFactor = user.getSportFactor() * total / totalAfter;
			user.setSportFactor(sportFactor);
			double historyFactor = user.getHistoryFactor() * total / totalAfter;
			user.setHistoryFactor(historyFactor);
			double biographyFactor = user.getBiographyFactor() * total
					/ totalAfter;
			user.setBiographyFactor(biographyFactor);
			double martialArtsFactor = user.getMartialArtsFactor() * total
					/ totalAfter;
			user.setMartialArtsFactor(martialArtsFactor);
			double homosexualFactor = user.getHomosexualFactor() * total
					/ totalAfter;
			user.setHomosexualFactor(homosexualFactor);
			double phantomFactor = user.getPhantomFactor() * total / totalAfter;
			user.setPhantomFactor(phantomFactor);
			double ancientCostumeFactor = user.getAncientCostumeFactor()
					* total / totalAfter;
			user.setAncientCostumeFactor(ancientCostumeFactor);
			double danceFactor = user.getDanceFactor() * total / totalAfter;
			user.setDanceFactor(danceFactor);

			// set added factors
			for (TypeEntity type : types) {
				String typeName = type.getName();
				if (typeName.equalsIgnoreCase(ACTION)) {
					double factor = (double) ((((double) totalAfter)
							* user.getActionFactor() + 1.0) / totalAfter);
					user.setActionFactor(factor);
				} else if (typeName.equalsIgnoreCase(SUSPENSE)) {
					double factor = (double) ((((double) totalAfter)
							* user.getSuspenseFactor() + 1.0) / totalAfter);
					user.setSuspenseFactor(factor);
				} else if (typeName.equalsIgnoreCase(FANTASY)) {
					double factor = (double) ((((double) totalAfter)
							* user.getFantasyFactor() + 1.0) / totalAfter);
					user.setFantasyFactor(factor);
				} else if (typeName.equalsIgnoreCase(FAMILY)) {
					double factor = (double) ((((double) totalAfter)
							* user.getFamilyFactor() + 1.0) / totalAfter);
					user.setFamilyFactor(factor);
				} else if (typeName.equalsIgnoreCase(COMEDY)) {
					double factor = (double) ((((double) totalAfter)
							* user.getComedyFactor() + 1.0) / totalAfter);
					user.setComedyFactor(factor);
				} else if (typeName.equalsIgnoreCase(ANIMATION)) {
					double factor = (double) ((((double) totalAfter)
							* user.getAnimationFactor() + 1.0) / totalAfter);
					user.setAnimationFactor(factor);
				} else if (typeName.equalsIgnoreCase(ADVENTURE)) {
					double factor = (double) ((((double) totalAfter)
							* user.getAdventureFactor() + 1.0) / totalAfter);
					user.setAdventureFactor(factor);
				} else if (typeName.equalsIgnoreCase(FICTION)) {
					double factor = (double) ((((double) totalAfter)
							* user.getFictionFactor() + 1.0) / totalAfter);
					user.setFictionFactor(factor);
				} else if (typeName.equalsIgnoreCase(CHILD)) {
					double factor = (double) ((((double) totalAfter)
							* user.getChildFactor() + 1.0) / totalAfter);
					user.setChildFactor(factor);
				} else if (typeName.equalsIgnoreCase(STORY)) {
					double factor = (double) ((((double) totalAfter)
							* user.getStoryFactor() + 1.0) / totalAfter);
					user.setStoryFactor(factor);
				} else if (typeName.equalsIgnoreCase(LOVE)) {
					double factor = (double) ((((double) totalAfter)
							* user.getLoveFactor() + 1.0) / totalAfter);
					user.setLoveFactor(factor);
				} else if (typeName.equalsIgnoreCase(DOCUMENT)) {
					double factor = (double) ((((double) totalAfter)
							* user.getDocumentoryFactor() + 1.0) / totalAfter);
					user.setDocumentoryFactor(factor);
				} else if (typeName.equalsIgnoreCase(THRILLER)) {
					double factor = (double) ((((double) totalAfter)
							* user.getThrillerFactor() + 1.0) / totalAfter);
					user.setThrillerFactor(factor);
				} else if (typeName.equalsIgnoreCase(CRIME)) {
					double factor = (double) ((((double) totalAfter)
							* user.getCrimeFactor() + 1.0) / totalAfter);
					user.setCrimeFactor(factor);
				} else if (typeName.equalsIgnoreCase(VIDEO)) {
					double factor = (double) ((((double) totalAfter)
							* user.getVideoFactor() + 1.0) / totalAfter);
					user.setVideoFactor(factor);
				} else if (typeName.equalsIgnoreCase(TERROR)) {
					double factor = (double) ((((double) totalAfter)
							* user.getTerrorFactor() + 1.0) / totalAfter);
					user.setTerrorFactor(factor);
				} else if (typeName.equalsIgnoreCase(WAR)) {
					double factor = (double) ((((double) totalAfter)
							* user.getWarFactor() + 1.0) / totalAfter);
					user.setWarFactor(factor);
				} else if (typeName.equalsIgnoreCase(WEST)) {
					double factor = (double) ((((double) totalAfter)
							* user.getWestFactor() + 1.0) / totalAfter);
					user.setWestFactor(factor);
				} else if (typeName.equalsIgnoreCase(MUSIC)) {
					double factor = (double) ((((double) totalAfter)
							* user.getMusicFactor() + 1.0) / totalAfter);
					user.setMusicFactor(factor);
				} else if (typeName.equalsIgnoreCase(SPORT)) {
					double factor = (double) ((((double) totalAfter)
							* user.getSportFactor() + 1.0) / totalAfter);
					user.setSportFactor(factor);
				} else if (typeName.equalsIgnoreCase(HISTORY)) {
					double factor = (double) ((((double) totalAfter)
							* user.getHistoryFactor() + 1.0) / totalAfter);
					user.setHistoryFactor(factor);
				} else if (typeName.equalsIgnoreCase(BIOGRAPHY)) {
					double factor = (double) ((((double) totalAfter)
							* user.getBiographyFactor() + 1.0) / totalAfter);
					user.setBiographyFactor(factor);
				} else if (typeName.equalsIgnoreCase(MARTIAL_ARTS)) {
					double factor = (double) ((((double) totalAfter)
							* user.getMartialArtsFactor() + 1.0) / totalAfter);
					user.setMartialArtsFactor(factor);
				} else if (typeName.equalsIgnoreCase(HOMOSEXUAL)) {
					double factor = (double) ((((double) totalAfter)
							* user.getHomosexualFactor() + 1.0) / totalAfter);
					user.setHomosexualFactor(factor);
				} else if (typeName.equalsIgnoreCase(PHANTOM)) {
					double factor = (double) ((((double) totalAfter)
							* user.getPhantomFactor() + 1.0) / totalAfter);
					user.setPhantomFactor(factor);
				} else if (typeName.equalsIgnoreCase(ANCIENT_COSTUME)) {
					double factor = (double) ((((double) totalAfter)
							* user.getAncientCostumeFactor() + 1.0) / totalAfter);
					user.setAncientCostumeFactor(factor);
				} else if (typeName.equalsIgnoreCase(DANCE)) {
					double factor = (double) ((((double) totalAfter)
							* user.getDanceFactor() + 1.0) / totalAfter);
					user.setDanceFactor(factor);
				}
			}
			user.setTotal(totalAfter);
			this.getICrawlerDaoService().createUserEntity(user);
		}
	}

	public static void main(String[] args) {
		ARImpl service = new ARImpl();
		// String list = service.listMoiveToJson(0, 10);
		// System.out.println(list);
		ActorEntity actor = service.getRandomActor();
		System.out.println(actor.getName());
	}
}
