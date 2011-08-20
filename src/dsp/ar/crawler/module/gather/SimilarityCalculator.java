package dsp.ar.crawler.module.gather;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import dsp.ar.dao.IARDao;
import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.AdEntity;
import dsp.ar.domain.AreaEntity;
import dsp.ar.domain.MovieAdSimilarityEntity;
import dsp.ar.domain.MovieEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.util.AppContext;

public class SimilarityCalculator {
	private IARDao arDao;

	public IARDao getIARDaoService() {
		if (arDao == null) {
			arDao = (IARDao) AppContext.getBean("arDao");
		}
		return arDao;
	}

	private void process() {
		List<MovieEntity> movieList = this.getIARDaoService().findMovies(null,
				null, null, null, null, 0, 0);
		List<AdEntity> adList = this.getIARDaoService().findAds(null, null,
				null, null, null, 0, 0);

		for (MovieEntity movie : movieList) {
			// gather movie actor id info
			List<ActorEntity> movieActors = movie.getActors();
			List<Integer> movieActorIdList = new ArrayList<Integer>();
			for (ActorEntity actor : movieActors) {
				movieActorIdList.add(actor.getId());
			}

			// gather movie type id info
			List<TypeEntity> movieTypes = this.getIARDaoService()
					.getMovieTypes(movie.getId());
			List<Integer> movieTypeIdList = new ArrayList<Integer>();
			for (TypeEntity type : movieTypes) {
				movieTypeIdList.add(type.getId());
			}

			// gather movie area id info
			List<AreaEntity> movieAreas = this.getIARDaoService()
					.getMovieAreas(movie.getId());
			List<Integer> movieAreaIdList = new ArrayList<Integer>();
			for (AreaEntity area : movieAreas) {
				movieAreaIdList.add(area.getId());
			}

			for (AdEntity ad : adList) {
				// actor similarity
				List<ActorEntity> adActors = this.getIARDaoService()
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
				List<TypeEntity> adTypes = this.getIARDaoService().getAdTypes(
						ad.getId());
				double typeSimilarity = 0.0;
				if (movieTypeIdList
						.contains(new Integer(adTypes.get(0).getId()))) {
					typeSimilarity = 1.0;
				}

				// area similarity
				List<AreaEntity> adAreas = this.getIARDaoService().getAdAreas(
						ad.getId());
				double areaSimilarity = 0.0;
				if (movieAreaIdList
						.contains(new Integer(adAreas.get(0).getId()))) {
					areaSimilarity = 1.0;
				}

				// calculate result
				double result = actorSimilarity * 0.4 + typeSimilarity * 0.3
						+ areaSimilarity * 0.3;
				// System.out.println("result : " + result);

				MovieAdSimilarityEntity similarity = new MovieAdSimilarityEntity();
				similarity.setAdId(ad.getId());
				similarity.setMovieId(movie.getId());
				similarity.setSimilarity(result);
				this.getIARDaoService().createMovieAdSimilarityEntity(
						similarity);
			}
		}
	}

	public static void main(String[] args) {
		SimilarityCalculator calculator = new SimilarityCalculator();
		calculator.process();
	}
}