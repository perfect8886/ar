package dsp.ar.crawler.module.gather;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dsp.ar.crawler.dao.ICrawlerDao;
import dsp.ar.crawler.domain.ActorEntity;
import dsp.ar.crawler.domain.AdEntity;
import dsp.ar.crawler.domain.MovieAdSimilarityEntity;
import dsp.ar.crawler.domain.MovieEntity;
import dsp.ar.crawler.domain.TypeEntity;
import dsp.ar.crawler.util.AppContext;

public class SimilarityCalculator {
	private ICrawlerDao crawlerDao;

	public ICrawlerDao getICrawlerDaoService() {
		if (crawlerDao == null) {
			crawlerDao = (ICrawlerDao) AppContext.getBean("crawlerDao");
		}
		return crawlerDao;
	}

	/**
	 * 后台计算movieId最近似Ad TopN; 新建MovieAdSimilarityEntity 启动时，一个thread计算movie and
	 * ad 相似度; 计算方法：actor，type，area三个维度 actor，type匹配方法: 权重：a=0.6、b=0.3、c=0.1
	 * 出现相同演员：x = 1->0.9;>=2->1; 出现相同类型 y = 影片类型重复比例; 地域 z = 相同1，不同0; result=
	 * a*x+b*y+c*z
	 */
	private void process() {
		List<MovieEntity> movieList = this.getICrawlerDaoService().findMovies(
				null, null, null, null, 0, 0);
		List<AdEntity> adList = this.getICrawlerDaoService().findAds(null,
				null, null, null);

		for (MovieEntity movie : movieList) {
			// gather movie actor id info
			List<ActorEntity> movieActors = movie.getActors();
			List<Integer> movieActorIdList = new ArrayList<Integer>();
			for (ActorEntity actor : movieActors) {
				movieActorIdList.add(actor.getId());
			}

			// gather movie type id info
			List<TypeEntity> movieTypes = this.getICrawlerDaoService()
					.getMovieTypes(movie.getId());
			List<Integer> movieTypeIdList = new ArrayList<Integer>();
			for (TypeEntity type : movieTypes) {
				movieTypeIdList.add(type.getId());
			}

			// area
			String movieArea = movie.getArea();
			for (AdEntity ad : adList) {
				// actor similarity
				List<ActorEntity> adActors = this.getICrawlerDaoService()
						.getAdActors(ad.getId());
				List<Integer> adActorIdList = new ArrayList<Integer>();
				for (ActorEntity actor : adActors) {
					adActorIdList.add(actor.getId());
				}
				Set actorSet = new HashSet<Integer>();
				actorSet.addAll(movieActorIdList);
				actorSet.addAll(adActorIdList);
				int actorCount = movieActorIdList.size() + adActorIdList.size()
						- actorSet.size();
				double actorSimilarity = 0.0;
				if (actorCount == 0) {
					actorSimilarity = 0.0;
				} else if (actorCount == 1) {
					actorSimilarity = 0.9;
				} else if (actorCount >= 2) {
					actorSimilarity = 1.0;
				}

				// type similarity
				List<TypeEntity> adTypes = this.getICrawlerDaoService()
						.getAdTypes(ad.getId());
				List<Integer> adTypeIdList = new ArrayList<Integer>();
				for (TypeEntity type : adTypes) {
					adTypeIdList.add(type.getId());
				}
				Set typeSet = new HashSet<Integer>();
				typeSet.addAll(movieTypeIdList);
				typeSet.addAll(adTypeIdList);
				double typeCount = movieTypeIdList.size() + adTypeIdList.size()
						- typeSet.size();
				double typeSimilarity = 0.0;
				if (movieTypeIdList.size() > 0) {
					typeSimilarity = typeCount * 3 / movieTypeIdList.size();
				} else {
					typeSimilarity = 0.0;
				}

				// area similarity
				String adArea = ad.getArea();
				double areaSimilarity = 0.0;
				if (adArea != null && movieArea != null) {
					if (adArea.contains(movieArea)
							|| movieArea.contains(adArea)) {
						areaSimilarity = 1.0;
					}
				}

				// calculate result
				double result = actorSimilarity * 0.6 + typeSimilarity * 0.3
						+ areaSimilarity * 0.1;
				// System.out.println("result : " + result);

				MovieAdSimilarityEntity similarity = new MovieAdSimilarityEntity();
				similarity.setAdId(ad.getId());
				similarity.setMovieId(movie.getId());
				similarity.setSimilarity(result);
				this.getICrawlerDaoService().createMovieAdSimilarityEntity(
						similarity);
			}
		}
	}

	public static void main(String[] args) {
		SimilarityCalculator calculator = new SimilarityCalculator();
		calculator.process();
	}
}
