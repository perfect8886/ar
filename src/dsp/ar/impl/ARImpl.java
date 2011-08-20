package dsp.ar.impl;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.google.gson.Gson;

import dsp.ar.Constants;
import dsp.ar.IAR;
import dsp.ar.dao.IARDao;
import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.AdEntity;
import dsp.ar.domain.AreaEntity;
import dsp.ar.domain.DirectorEntity;
import dsp.ar.domain.InterestModelEntity;
import dsp.ar.domain.MovieAdSimilarityEntity;
import dsp.ar.domain.MovieEntity;
import dsp.ar.domain.RecordEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.domain.UserEntity;
import dsp.ar.dto.AdDTO;
import dsp.ar.dto.AdManageDTO;
import dsp.ar.dto.MovieDTO;
import dsp.ar.util.AppContext;

public class ARImpl implements IAR {
	private static final Logger logger = Logger.getLogger(ARImpl.class);

	private IARDao arDao;
	private Gson gson;

	public ARImpl() {
		gson = new Gson();
		arDao = (IARDao) AppContext.getBean("arDao");
		Constants.mapIntialize();
	}

	public Gson getGson() {
		if (gson == null) {
			gson = new Gson();
		}
		return gson;
	}

	public IARDao getIARDaoService() {
		if (arDao == null) {
			arDao = (IARDao) AppContext.getBean("arDao");
		}
		return arDao;
	}

	/** Movie Management */
	public String listMoiveToJson(int start, int limit) {
		List<MovieEntity> movieEntityList = this.getIARDaoService().findMovies(
				null, null, null, null, null, start, limit);
		logger.info("movie list size : " + movieEntityList.size());

		List<MovieDTO> movieDTOList = new ArrayList<MovieDTO>();
		for (MovieEntity movieEntity : movieEntityList) {
			MovieDTO movieDTO = new MovieDTO();
			// desc, id, imageUrl, name, publish time
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
			List<DirectorEntity> directorsEntity = this.getIARDaoService()
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
			List<TypeEntity> typersEntity = this.getIARDaoService()
					.getMovieTypes(movieEntity.getId());
			if (typersEntity != null && typersEntity.size() > 0) {
				for (TypeEntity typeEntity : typersEntity) {
					String typeStr = typeEntity.getName();
					typesStr.add(typeStr);
				}
			}
			movieDTO.setTypes(typesStr);

			// areas
			List<String> areasStr = new ArrayList<String>();
			List<AreaEntity> areasEntity = this.getIARDaoService()
					.getMovieAreas(movieEntity.getId());
			if (areasEntity != null && areasEntity.size() > 0) {
				for (AreaEntity areaEntity : areasEntity) {
					String areaStr = areaEntity.getName();
					areasStr.add(areaStr);
				}
			}
			movieDTO.setAreas(areasStr);
			movieDTOList.add(movieDTO);
		}

		long total = this.getIARDaoService().getMovieCount();

		logger.info("-------------------> Total Movie : " + total);
		StringBuilder jsonBuilder = new StringBuilder("{success:true,total:")
				.append(total).append(",movies:");
		jsonBuilder.append(this.getGson().toJson(movieDTOList)).append("}");
		logger.info("---->Response : " + jsonBuilder.toString());
		return jsonBuilder.toString();
	}

	public int CreateMovieEntity(MovieEntity movie) {
		return this.getIARDaoService().createMovieEntity(movie);
	}

	public List<MovieEntity> getDirectorMovies(int directorId) {
		return this.getIARDaoService().getDirectorMovies(directorId);
	}

	public List<MovieEntity> getTypeMovies(int typeId) {
		return this.getIARDaoService().getTypeMovies(typeId);
	}

	public List<MovieEntity> getAreaMovies(int areaId) {
		return this.getIARDaoService().getAreaMovies(areaId);
	}

	/** Ad Management */
	public String recommendAdToJson(int userId, int movieId) {
		// 影片拟合度
		// 兴趣相似度
		// 协同推荐度 TODO

		// step 1: 获取最相似广告及其相似度 Top20
		List<MovieAdSimilarityEntity> similarityList = this.getIARDaoService()
				.getTopNAdByMovieId(movieId, 100);
		// step 2:
		// 在相似广告中，计算用户兴趣程度interst,并计算最终result：0.4*similarity+0.6*interest
		List<AdDTO> adTop5 = new ArrayList<AdDTO>();
		for (MovieAdSimilarityEntity similarity : similarityList) {
			AdEntity ad = this.getIARDaoService().getAdById(
					similarity.getAdId());
			AdDTO adDTO = new AdDTO();
			adDTO.setId(ad.getId());
			adDTO.setMovieSimilarity(similarity.getSimilarity());
			List<TypeEntity> types = this.getIARDaoService().getAdTypes(
					ad.getId());
			// 计算用户兴趣程度
			UserEntity user = this.getIARDaoService().getUserById(userId);
			InterestModelEntity interestModel = user.getInterestModel();
			double interest = 0.0;
			for (TypeEntity type : types) {
				String typeCNName = type.getName();
				String typeENName = Constants.TYPE_MAP.get(typeCNName);
				String getMethodName = "get" + typeENName + "Factor";
				try {
					Class interestModelClass = interestModel.getClass();
					Method getMethod = interestModelClass
							.getDeclaredMethod(getMethodName);

					double factor = (Double) getMethod.invoke(interestModel,
							null);
					interest += factor;
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// for (TypeEntity type : types) {
			// String typeName = type.getName();
			// if (typeName.equalsIgnoreCase(Constants.Type.ACTION)) {
			// interest += interestModel.getActionFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.SUSPENSE)) {
			// interest += interestModel.getSuspenseFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.FANTASY)) {
			// interest += interestModel.getFantasyFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.FAMILY)) {
			// interest += interestModel.getFamilyFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.COMEDY)) {
			// interest += interestModel.getComedyFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.ANIMATION)) {
			// interest += interestModel.getAnimationFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.ADVENTURE)) {
			// interest += interestModel.getAdventureFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.FICTION)) {
			// interest += interestModel.getFictionFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.CHILD)) {
			// interest += interestModel.getChildFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.STORY)) {
			// interest += interestModel.getStoryFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.LOVE)) {
			// interest += interestModel.getLoveFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.DOCUMENT)) {
			// interest += interestModel.getDocumentoryFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.THRILLER)) {
			// interest += interestModel.getThrillerFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.CRIME)) {
			// interest += interestModel.getCrimeFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.VIDEO)) {
			// interest += interestModel.getVideoFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.TERROR)) {
			// interest += interestModel.getTerrorFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.WAR)) {
			// interest += interestModel.getWarFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.WEST)) {
			// interest += interestModel.getWestFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.MUSIC)) {
			// interest += interestModel.getMusicFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.SPORT)) {
			// interest += interestModel.getSportFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.HISTORY)) {
			// interest += interestModel.getHistoryFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.BIOGRAPHY)) {
			// interest += interestModel.getBiographyFactor();
			// } else if (typeName
			// .equalsIgnoreCase(Constants.Type.MARTIAL_ARTS)) {
			// interest += interestModel.getMartialArtsFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.HOMOSEXUAL))
			// {
			// interest += interestModel.getHomosexualFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.PHANTOM)) {
			// interest += interestModel.getPhantomFactor();
			// } else if (typeName
			// .equalsIgnoreCase(Constants.Type.ANCIENT_COSTUME)) {
			// interest += interestModel.getAncientCostumeFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.DANCE)) {
			// interest += interestModel.getDanceFactor();
			// } else if (typeName.equalsIgnoreCase(Constants.Type.SEX)) {
			// interest += interestModel.getSexFactor();
			// }
			// }
			interest = (double) (interest * 2);
			adDTO.setInterest(interest);

			// 计算兴趣权重，随观看次数增多而增加
			double interestFactor = 0.0;
			long userRecordCount = this.getIARDaoService().getUserRecordCount(
					userId);
			if (userRecordCount < 1) {
				interestFactor = 0.0;
			} else {
				interestFactor = (double) (Constants.Factor.INTEREST - 1.0 / userRecordCount);
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
			AdEntity ad = this.getIARDaoService().getAdById(adDTO.getId());
			// desc, id, imageUrl, name
			adDTO.setDesc(ad.getDesc());
			adDTO.setId(ad.getId());
			adDTO.setImageUrl(ad.getImageUrl());
			adDTO.setName(ad.getName());

			// actors
			List<String> actorsStr = new ArrayList<String>();
			List<ActorEntity> actorsEntity = this.getIARDaoService()
					.getAdActors(ad.getId());
			for (ActorEntity actorEntity : actorsEntity) {
				String actorStr = actorEntity.getName();
				actorsStr.add(actorStr);
			}
			adDTO.setActors(actorsStr);
			// directors
			List<String> directorsStr = new ArrayList<String>();
			List<DirectorEntity> directorsEntity = this.getIARDaoService()
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
			List<TypeEntity> typesEntity = this.getIARDaoService().getAdTypes(
					ad.getId());
			if (typesEntity != null && typesEntity.size() > 0) {
				for (TypeEntity typeEntity : typesEntity) {
					String typeStr = typeEntity.getName();
					typesStr.add(typeStr);
				}
			}
			adDTO.setTypes(typesStr);

			// areas
			List<String> areasStr = new ArrayList<String>();
			List<AreaEntity> areasEntity = this.getIARDaoService().getAdAreas(
					ad.getId());
			if (areasEntity != null && areasEntity.size() > 0) {
				for (AreaEntity areaEntity : areasEntity) {
					String areaStr = areaEntity.getName();
					areasStr.add(areaStr);
				}
			}
			adDTO.setAreas(areasStr);
		}
		StringBuilder jsonBuilder = new StringBuilder(this.getGson().toJson(
				adTop3));
		logger.info("---->Response : " + jsonBuilder.toString());
		return jsonBuilder.toString();
	}

	public int CreateAdEntity(AdEntity ad) {
		return this.getIARDaoService().createAdEntity(ad);
	}

	public List<AdEntity> getTypeAds(int typeId) {
		return this.getIARDaoService().getTypeAds(typeId);
	}

	public List<AdEntity> getDirectorAds(int directorId) {
		return this.getIARDaoService().getDirectorAds(directorId);
	}

	public List<AdEntity> getActorAds(int actorId) {
		return this.getIARDaoService().getActorAds(actorId);
	}

	public List<AdEntity> getAreaAds(int areaId) {
		return this.getIARDaoService().getAreaAds(areaId);
	}

	public String listAdToJson(int start, int limit) {
		List<AdEntity> adEntityList = this.getIARDaoService().findAds(null,
				null, null, null, null, start, limit);
		logger.info("ad list size : " + adEntityList.size());

		List<AdManageDTO> adDTOList = new ArrayList<AdManageDTO>();
		for (AdEntity adEntity : adEntityList) {
			AdManageDTO adDTO = new AdManageDTO();
			// desc, id, imageUrl, name, publish time
			adDTO.setDesc(adEntity.getDesc());
			adDTO.setId(adEntity.getId());
			adDTO.setImageUrl(adEntity.getImageUrl());
			adDTO.setName(adEntity.getName());
			adDTO.setPublishTime(adEntity.getPublishTime());
			// actors
			List<String> actorsStr = new ArrayList<String>();
			List<ActorEntity> actorsEntity = this.getIARDaoService()
					.getAdActors(adEntity.getId());
			for (ActorEntity actorEntity : actorsEntity) {
				String actorStr = actorEntity.getName();
				actorsStr.add(actorStr);
			}
			adDTO.setActors(actorsStr);
			// directors
			List<String> directorsStr = new ArrayList<String>();
			List<DirectorEntity> directorsEntity = this.getIARDaoService()
					.getAdDirectors(adDTO.getId());
			if (directorsEntity != null && directorsEntity.size() > 0) {
				for (DirectorEntity directorEntity : directorsEntity) {
					String directorStr = directorEntity.getName();
					directorsStr.add(directorStr);
				}
			}
			adDTO.setDirectors(directorsStr);
			// types
			List<String> typesStr = new ArrayList<String>();
			List<TypeEntity> typersEntity = this.getIARDaoService().getAdTypes(
					adEntity.getId());
			if (typersEntity != null && typersEntity.size() > 0) {
				for (TypeEntity typeEntity : typersEntity) {
					String typeStr = typeEntity.getName();
					typesStr.add(typeStr);
				}
			}
			adDTO.setTypes(typesStr);

			// areas
			List<String> areasStr = new ArrayList<String>();
			List<AreaEntity> areasEntity = this.getIARDaoService().getAdAreas(
					adEntity.getId());
			if (areasEntity != null && areasEntity.size() > 0) {
				for (AreaEntity areaEntity : areasEntity) {
					String areaStr = areaEntity.getName();
					areasStr.add(areaStr);
				}
			}
			adDTO.setAreas(areasStr);
			adDTOList.add(adDTO);
		}

		long total = this.getIARDaoService().getAdCount();

		logger.info("-------------------> Total Ad : " + total);
		StringBuilder jsonBuilder = new StringBuilder("{success:true,total:")
				.append(total).append(",ads:");
		jsonBuilder.append(this.getGson().toJson(adDTOList)).append("}");
		logger.info("---->Response : " + jsonBuilder.toString());
		return jsonBuilder.toString();
	}

	/** Actor Management */
	public ActorEntity getActorByName(String name) {
		return this.getIARDaoService().getActorByName(name);
	}

	public List<ActorEntity> getActors() {
		return this.getIARDaoService().getActors();
	}

	public ActorEntity getRandomActor() {
		List<ActorEntity> actors = this.getIARDaoService().getActors();
		int size = actors.size();
		int random = (int) (Math.random() * size);
		return actors.get(random);
	}

	/** Director Management */
	public DirectorEntity getDirectorByName(String name) {
		return this.getIARDaoService().getDirectorByName(name);
	}

	public List<DirectorEntity> getDirectors() {
		return this.getIARDaoService().getDirectors();
	}

	/** Type Management */
	public TypeEntity getTypeByName(String name) {
		return this.getIARDaoService().getTypeByName(name);
	}

	public List<TypeEntity> getTypes() {
		return this.getIARDaoService().getTypes();
	}

	/** Area Management */
	public AreaEntity getAreaByName(String name) {
		return this.getIARDaoService().getAreaByName(name);
	}

	public List<AreaEntity> getAreas() {
		return this.getIARDaoService().getAreas();
	}

	/** User Management */
	public int createUser(UserEntity user) {
		return this.getIARDaoService().createUserEntity(user);
	}

	public UserEntity getUserByIp(String ip) {
		return this.getIARDaoService().getUserByIp(ip);
	}

	public void updateUserByAd(int adId, UserEntity user) {
		List<TypeEntity> types = this.getIARDaoService().getAdTypes(adId);
		this.updateUser(types, user);
	}

	public void updateUserByMovie(int movieId, UserEntity user) {
		List<TypeEntity> types = this.getIARDaoService().getMovieTypes(movieId);
		this.updateUser(types, user);
	}

	private void updateUser(List<TypeEntity> types, UserEntity user) {
		if (types != null && types.size() > 0) {
			InterestModelEntity interestModel = user.getInterestModel();
			int total = interestModel.getTotal();
			int totalAfter = total + types.size();

			// set all factors
			List<TypeEntity> allTypes = this.getIARDaoService().getTypes();
			for (TypeEntity type : allTypes) {
				String typeCNName = type.getName();
				String typeENName = Constants.TYPE_MAP.get(typeCNName);
				String getMethodName = "get" + typeENName + "Factor";
				String setMethodName = "set" + typeENName + "Factor";
				try {
					Class interestModelClass = interestModel.getClass();
					Method getMethod = interestModelClass
							.getDeclaredMethod(getMethodName);
					Method setMethod = interestModelClass.getDeclaredMethod(
							setMethodName, double.class);
					double factor = (Double) getMethod.invoke(interestModel,
							null);
					factor = factor * total / totalAfter;
					setMethod.invoke(interestModel, factor);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// set added factors
			for (TypeEntity type : types) {
				String typeCNName = type.getName();
				String typeENName = Constants.TYPE_MAP.get(typeCNName);
				String getMethodName = "get" + typeENName + "Factor";
				String setMethodName = "set" + typeENName + "Factor";

				try {
					Class interestModelClass = interestModel.getClass();
					Method getMethod = interestModelClass
							.getDeclaredMethod(getMethodName);
					Method setMethod = interestModelClass.getDeclaredMethod(
							setMethodName, double.class);
					double factor = (Double) getMethod.invoke(interestModel,
							null);
					factor = (factor * totalAfter + 1.0) / totalAfter;
					setMethod.invoke(interestModel, factor);
				} catch (SecurityException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (NoSuchMethodException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalArgumentException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IllegalAccessException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (InvocationTargetException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

			// // set all factors
			// double actionFactor = interestModel.getActionFactor() * total
			// / totalAfter;
			// interestModel.setActionFactor(actionFactor);
			// double suspenseFactor = interestModel.getSuspenseFactor() * total
			// / totalAfter;
			// interestModel.setSuspenseFactor(suspenseFactor);
			// double fantasyFactor = interestModel.getFantasyFactor() * total
			// / totalAfter;
			// interestModel.setFantasyFactor(fantasyFactor);
			// double familyFactor = interestModel.getFamilyFactor() * total
			// / totalAfter;
			// interestModel.setFamilyFactor(familyFactor);
			// double comedyFactor = interestModel.getComedyFactor() * total
			// / totalAfter;
			// interestModel.setComedyFactor(comedyFactor);
			// double animationFactor = interestModel.getAnimationFactor() *
			// total
			// / totalAfter;
			// interestModel.setAnimationFactor(animationFactor);
			// double adventureFactor = interestModel.getAdventureFactor() *
			// total
			// / totalAfter;
			// interestModel.setAdventureFactor(adventureFactor);
			// double fictionFactor = interestModel.getFictionFactor() * total
			// / totalAfter;
			// interestModel.setFictionFactor(fictionFactor);
			// double childFactor = interestModel.getChildFactor() * total
			// / totalAfter;
			// interestModel.setChildFactor(childFactor);
			// double storyFactor = interestModel.getStoryFactor() * total
			// / totalAfter;
			// interestModel.setStoryFactor(storyFactor);
			// double loveFactor = interestModel.getLoveFactor() * total
			// / totalAfter;
			// interestModel.setLoveFactor(loveFactor);
			// double documentoryFactor = interestModel.getDocumentoryFactor()
			// * total / totalAfter;
			// interestModel.setDocumentoryFactor(documentoryFactor);
			// double thrillerFactor = interestModel.getThrillerFactor() * total
			// / totalAfter;
			// interestModel.setThrillerFactor(thrillerFactor);
			// double crimeFactor = interestModel.getCrimeFactor() * total
			// / totalAfter;
			// interestModel.setCrimeFactor(crimeFactor);
			// double videoFactor = interestModel.getVideoFactor() * total
			// / totalAfter;
			// interestModel.setVideoFactor(videoFactor);
			// double terrorFactor = interestModel.getTerrorFactor() * total
			// / totalAfter;
			// interestModel.setTerrorFactor(terrorFactor);
			// double warFactor = interestModel.getWarFactor() * total
			// / totalAfter;
			// interestModel.setWarFactor(warFactor);
			// double westFactor = interestModel.getWestFactor() * total
			// / totalAfter;
			// interestModel.setWestFactor(westFactor);
			// double musicFactor = interestModel.getMusicFactor() * total
			// / totalAfter;
			// interestModel.setMusicFactor(musicFactor);
			// double sportFactor = interestModel.getSportFactor() * total
			// / totalAfter;
			// interestModel.setSportFactor(sportFactor);
			// double historyFactor = interestModel.getHistoryFactor() * total
			// / totalAfter;
			// interestModel.setHistoryFactor(historyFactor);
			// double biographyFactor = interestModel.getBiographyFactor() *
			// total
			// / totalAfter;
			// interestModel.setBiographyFactor(biographyFactor);
			// double martialArtsFactor = interestModel.getMartialArtsFactor()
			// * total / totalAfter;
			// interestModel.setMartialArtsFactor(martialArtsFactor);
			// double homosexualFactor = interestModel.getHomosexualFactor()
			// * total / totalAfter;
			// interestModel.setHomosexualFactor(homosexualFactor);
			// double phantomFactor = interestModel.getPhantomFactor() * total
			// / totalAfter;
			// interestModel.setPhantomFactor(phantomFactor);
			// double ancientCostumeFactor = interestModel
			// .getAncientCostumeFactor()
			// * total / totalAfter;
			// interestModel.setAncientCostumeFactor(ancientCostumeFactor);
			// double danceFactor = interestModel.getDanceFactor() * total
			// / totalAfter;
			// interestModel.setDanceFactor(danceFactor);
			// double sexFactor = interestModel.getSexFactor() * total
			// / totalAfter;
			// interestModel.setSexFactor(sexFactor);
			//
			// // set added factors
			// for (TypeEntity type : types) {
			// String typeName = type.getName();
			// if (typeName.equalsIgnoreCase(Constants.Type.ACTION)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getActionFactor() + 1.0) / totalAfter);
			// interestModel.setActionFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.SUSPENSE)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getSuspenseFactor() + 1.0) / totalAfter);
			// interestModel.setSuspenseFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.FANTASY)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getFantasyFactor() + 1.0) / totalAfter);
			// interestModel.setFantasyFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.FAMILY)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getFamilyFactor() + 1.0) / totalAfter);
			// interestModel.setFamilyFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.COMEDY)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getComedyFactor() + 1.0) / totalAfter);
			// interestModel.setComedyFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.ANIMATION)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getAnimationFactor() + 1.0) / totalAfter);
			// interestModel.setAnimationFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.ADVENTURE)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getAdventureFactor() + 1.0) / totalAfter);
			// interestModel.setAdventureFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.FICTION)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getFictionFactor() + 1.0) / totalAfter);
			// interestModel.setFictionFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.CHILD)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getChildFactor() + 1.0) / totalAfter);
			// interestModel.setChildFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.STORY)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getStoryFactor() + 1.0) / totalAfter);
			// interestModel.setStoryFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.LOVE)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getLoveFactor() + 1.0) / totalAfter);
			// interestModel.setLoveFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.DOCUMENT)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getDocumentoryFactor() + 1.0) / totalAfter);
			// interestModel.setDocumentoryFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.THRILLER)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getThrillerFactor() + 1.0) / totalAfter);
			// interestModel.setThrillerFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.CRIME)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getCrimeFactor() + 1.0) / totalAfter);
			// interestModel.setCrimeFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.VIDEO)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getVideoFactor() + 1.0) / totalAfter);
			// interestModel.setVideoFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.TERROR)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getTerrorFactor() + 1.0) / totalAfter);
			// interestModel.setTerrorFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.WAR)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getWarFactor() + 1.0) / totalAfter);
			// interestModel.setWarFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.WEST)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getWestFactor() + 1.0) / totalAfter);
			// interestModel.setWestFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.MUSIC)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getMusicFactor() + 1.0) / totalAfter);
			// interestModel.setMusicFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.SPORT)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getSportFactor() + 1.0) / totalAfter);
			// interestModel.setSportFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.HISTORY)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getHistoryFactor() + 1.0) / totalAfter);
			// interestModel.setHistoryFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.BIOGRAPHY)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getBiographyFactor() + 1.0) / totalAfter);
			// interestModel.setBiographyFactor(factor);
			// } else if (typeName
			// .equalsIgnoreCase(Constants.Type.MARTIAL_ARTS)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getMartialArtsFactor() + 1.0) / totalAfter);
			// interestModel.setMartialArtsFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.HOMOSEXUAL))
			// {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getHomosexualFactor() + 1.0) / totalAfter);
			// interestModel.setHomosexualFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.PHANTOM)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getPhantomFactor() + 1.0) / totalAfter);
			// interestModel.setPhantomFactor(factor);
			// } else if (typeName
			// .equalsIgnoreCase(Constants.Type.ANCIENT_COSTUME)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getAncientCostumeFactor() + 1.0) / totalAfter);
			// interestModel.setAncientCostumeFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.DANCE)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getDanceFactor() + 1.0) / totalAfter);
			// interestModel.setDanceFactor(factor);
			// } else if (typeName.equalsIgnoreCase(Constants.Type.SEX)) {
			// double factor = (double) ((((double) totalAfter)
			// * interestModel.getSexFactor() + 1.0) / totalAfter);
			// interestModel.setSexFactor(factor);
			// }
			// }
			interestModel.setTotal(totalAfter);
			user.setInterestModel(interestModel);
			this.getIARDaoService().createUserEntity(user);
		}
	}

	/** Record Management */
	public int createRecord(RecordEntity record) {
		return this.getIARDaoService().createRecordEntity(record);
	}

	public RecordEntity getLatestRecord(int userId) {
		List<RecordEntity> recordList = this.getIARDaoService().findRecords(
				userId, 0, 0);
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

	/** Similarity Management */
	public int createMovieAdSimilarity(MovieAdSimilarityEntity similarity) {
		return this.getIARDaoService()
				.createMovieAdSimilarityEntity(similarity);
	}

	public static void main(String[] args) {
		ARImpl service = new ARImpl();
		// String list = service.listMoiveToJson(0, 10);
		// System.out.println(list);
		ActorEntity actor = service.getRandomActor();
		System.out.println(actor.getName());
	}
}
