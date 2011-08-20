package dsp.ar.dao.hibernate;

import java.util.ArrayList;
import java.util.List;

import org.hibernate.Query;
import org.hibernate.Session;
import org.springframework.orm.hibernate3.HibernateCallback;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;

import dsp.ar.dao.IARDao;
import dsp.ar.domain.ActorEntity;
import dsp.ar.domain.AdEntity;
import dsp.ar.domain.AreaEntity;
import dsp.ar.domain.DirectorEntity;
import dsp.ar.domain.MovieAdSimilarityEntity;
import dsp.ar.domain.MovieEntity;
import dsp.ar.domain.RecordEntity;
import dsp.ar.domain.TypeEntity;
import dsp.ar.domain.UserEntity;

public class ARDaoHibernate extends HibernateDaoSupport implements IARDao {
	/** Movie Manage */
	public int createMovieEntity(MovieEntity movie) {
		logger.info("Create MovieEntity.");
		this.getHibernateTemplate().merge(movie);
		return movie.getId();
	}

	public List<MovieEntity> findMovies(String name, List<String> actorName,
			List<String> typeName, List<String> directorName,
			List<String> areaName, final int start, final int limit) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct m from MovieEntity as m");
		if (actorName != null && actorName.size() > 0) {
			queryStr.append(" inner join m.actors a");
		}
		if (typeName != null && typeName.size() > 0) {
			queryStr.append(" inner join m.types t");
		}
		if (directorName != null && directorName.size() > 0) {
			queryStr.append(" inner join m.directors d");
		}
		if (areaName != null && areaName.size() > 0) {
			queryStr.append("inner join m.areas e");
		}
		queryStr.append(" where 1=1");

		if (name != null && name.trim().length() > 0) {
			queryStr.append(" and m.name like '%").append(name).append("%'");
		}
		if (actorName != null && actorName.size() > 0) {
			queryStr.append(" and (");
			for (String actor : actorName) {
				queryStr.append("a.name like '%").append(actor)
						.append("%' or ");
			}
			queryStr.append("1=0)");
		}
		if (typeName != null && typeName.size() > 0) {
			queryStr.append(" and (");
			for (String type : typeName) {
				queryStr.append("t.name like '%").append(type).append("%' or ");
			}
			queryStr.append("1=0)");
		}
		if (directorName != null && directorName.size() > 0) {
			queryStr.append(" and (");
			for (String director : directorName) {
				queryStr.append("d.name like '%").append(director).append(
						"%' or ");
			}
			queryStr.append("1=0)");
		}
		if (areaName != null && areaName.size() > 0) {
			queryStr.append(" and (");
			for (String area : areaName) {
				queryStr.append("e.name like '%").append(area).append("%' or ");
			}
			queryStr.append("1=0)");
		}
		List<MovieEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						if (limit != 0) {
							q.setFirstResult(start);
							q.setMaxResults(limit);
						}
						return q.list();
					}
				});

		if (list.size() == 0 || list == null) {
			logger.error("find MovieEntity error!");
			return new ArrayList<MovieEntity>();
		}
		return list;
	}

	public List<DirectorEntity> getMovieDirectors(int movieId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct d from DirectorEntity as d inner join d.movies m where m.id = '")
				.append(movieId).append("'");
		List<DirectorEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find DirectorEntity error!");
			return new ArrayList<DirectorEntity>();
		}
		return list;
	}

	public List<TypeEntity> getMovieTypes(int movieId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct t from TypeEntity as t inner join t.movies m where m.id = '")
				.append(movieId).append("'");
		List<TypeEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find TypeEntity error!");
			return new ArrayList<TypeEntity>();
		}
		return list;
	}

	public List<AreaEntity> getMovieAreas(int movieId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct e from AreaEntity as e inner join e.movies m where m.id = '")
				.append(movieId).append("'");
		List<AreaEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AreaEntity error!");
			return new ArrayList<AreaEntity>();
		}
		return list;
	}

	public List<MovieEntity> getTypeMovies(int typeId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct m from MovieEntity as m inner join m.types t where t.id = '")
				.append(typeId).append("'");
		List<MovieEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find MovieEntity error!");
			return new ArrayList<MovieEntity>();
		}
		return list;
	}

	public List<MovieEntity> getDirectorMovies(int directorId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct m from MovieEntity as m inner join m.directors d where d.id = '")
				.append(directorId).append("'");
		List<MovieEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find MovieEntity error!");
			return new ArrayList<MovieEntity>();
		}
		return list;
	}

	public List<MovieEntity> getAreaMovies(int areaId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct m from MovieEntity as m inner join m.areas e where e.id = '")
				.append(areaId).append("'");
		List<MovieEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find MovieEntity error!");
			return new ArrayList<MovieEntity>();
		}
		return list;
	}

	public long getMovieCount() {
		final StringBuilder queryStr = new StringBuilder(
				"select count(*) from MovieEntity");
		long count = (Long) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Long doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return (Long) q.uniqueResult();
					}
				});
		logger.info("------------------------> Movie count : " + count);
		return count;
	}

	public MovieEntity getMovieById(int id) {
		logger.info("getMovieById.");
		final StringBuilder queryStr = new StringBuilder(
				"from MovieEntity as m where m.id = '").append(id).append("'");
		List<MovieEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.info("MovieEntity not exist!");
			return null;
		}
		return list.get(0);
	}

	/** Ad Manage */
	public int createAdEntity(AdEntity ad) {
		logger.info("Create AdEntity.");
		this.getHibernateTemplate().merge(ad);
		return ad.getId();
	}

	public List<AdEntity> findAds(String name, List<String> actorName,
			List<String> typeName, List<String> directorName,
			List<String> areaName, final int start, final int limit) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct ad from AdEntity as ad");
		if (actorName != null && actorName.size() > 0) {
			queryStr.append(" inner join ad.actors a");
		}
		if (typeName != null && typeName.size() > 0) {
			queryStr.append(" inner join ad.types t");
		}
		if (directorName != null && directorName.size() > 0) {
			queryStr.append(" inner join ad.directors d");
		}
		if (areaName != null && areaName.size() > 0) {
			queryStr.append(" inner join ad.areas e");
		}
		queryStr.append(" where 1=1");

		if (name != null && name.trim().length() > 0) {
			queryStr.append(" and ad.name like '%").append(name).append("%'");
		}
		if (actorName != null && actorName.size() > 0) {
			queryStr.append(" and (");
			for (String actor : actorName) {
				queryStr.append("a.name like '%").append(actor)
						.append("%' or ");
			}
			queryStr.append("1=0)");
		}
		if (typeName != null && typeName.size() > 0) {
			queryStr.append(" and (");
			for (String type : typeName) {
				queryStr.append("t.name like '%").append(type).append("%' or ");
			}
			queryStr.append("1=0)");
		}
		if (directorName != null && directorName.size() > 0) {
			queryStr.append(" and (");
			for (String director : directorName) {
				queryStr.append("d.name like '%").append(director).append(
						"%' or ");
			}
			queryStr.append("1=0)");
		}
		if (areaName != null && areaName.size() > 0) {
			queryStr.append(" and (");
			for (String area : areaName) {
				queryStr.append("e.name like '%").append(area).append("%' or ");
			}
			queryStr.append("1=0)");
		}
		List<AdEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						if (limit != 0) {
							q.setFirstResult(start);
							q.setMaxResults(limit);
						}
						return q.list();
					}
				});

		if (list.size() == 0 || list == null) {
			logger.error("find AdEntity error!");
			return new ArrayList<AdEntity>();
		}
		return list;
	}

	public List<ActorEntity> getAdActors(int adId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct a from ActorEntity as a inner join a.ads ad where ad.id = '")
				.append(adId).append("'");
		List<ActorEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find ActorEntity error!");
			return new ArrayList<ActorEntity>();
		}
		return list;
	}

	public List<DirectorEntity> getAdDirectors(int adId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct d from DirectorEntity as d inner join d.ads ad where ad.id = '")
				.append(adId).append("'");
		List<DirectorEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find DirectorEntity error!");
			return new ArrayList<DirectorEntity>();
		}
		return list;
	}

	public List<TypeEntity> getAdTypes(int adId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct t from TypeEntity as t inner join t.ads ad where ad.id = '")
				.append(adId).append("'");
		List<TypeEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find TypeEntity error!");
			return new ArrayList<TypeEntity>();
		}
		return list;
	}

	public List<AreaEntity> getAdAreas(int adId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct e from AreaEntity as e inner join e.ads ad where ad.id = '")
				.append(adId).append("'");
		List<AreaEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AreaEntity error!");
			return new ArrayList<AreaEntity>();
		}
		return list;
	}

	public List<AdEntity> getDirectorAds(int directorId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct ad from AdEntity as ad inner join ad.directors d where d.id = '")
				.append(directorId).append("'");
		List<AdEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AdEntity error!");
			return new ArrayList<AdEntity>();
		}
		return list;
	}

	public List<AdEntity> getTypeAds(int typeId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct ad from AdEntity as ad inner join ad.types t where t.id = '")
				.append(typeId).append("'");
		List<AdEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AdEntity error!");
			return new ArrayList<AdEntity>();
		}
		return list;
	}

	public List<AdEntity> getActorAds(int actorId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct ad from AdEntity as ad inner join ad.actors a where a.id = '")
				.append(actorId).append("'");
		List<AdEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AdEntity error!");
			return new ArrayList<AdEntity>();
		}
		return list;
	}

	public List<AdEntity> getAreaAds(int areaId) {
		final StringBuilder queryStr = new StringBuilder(
				"select distinct ad from AdEntity as ad inner join ad.areas e where e.id = '")
				.append(areaId).append("'");
		List<AdEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AdEntity error!");
			return new ArrayList<AdEntity>();
		}
		return list;
	}

	public long getAdCount() {
		final StringBuilder queryStr = new StringBuilder(
				"select count(*) from AdEntity");
		long count = (Long) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Long doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return (Long) q.uniqueResult();
					}
				});
		logger.info("------------------------> Ad count : " + count);
		return count;
	}

	public AdEntity getAdById(int id) {
		logger.info("getAdById.");
		final StringBuilder queryStr = new StringBuilder(
				"from AdEntity as ad where ad.id = '").append(id).append("'");
		List<AdEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.info("AdEntity not exist!");
			return null;
		}
		return list.get(0);
	}

	/** Actor Manage */
	public int createActorEntity(ActorEntity actor) {
		logger.info("Create ActorEntity.");
		this.getHibernateTemplate().saveOrUpdate(actor);
		return actor.getId();
	}

	public ActorEntity getActorByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		} else {
			final StringBuilder queryStr = new StringBuilder(
					"select distinct a from ActorEntity as a where a.name like '%")
					.append(name).append("%'");
			List<ActorEntity> list = (List) this.getHibernateTemplate()
					.execute(new HibernateCallback() {
						public List doInHibernate(Session session) {
							Query q = session.createQuery(queryStr.toString());
							return q.list();
						}
					});

			if (list.size() == 0 || list == null) {
				logger.error("ActorEntity not found");
				return null;
			}
			return list.get(0);
		}
	}

	public List<ActorEntity> getActors() {
		final StringBuilder queryStr = new StringBuilder(
				"from ActorEntity a where 1=1");
		List<ActorEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find ActorEntity error!");
			return new ArrayList<ActorEntity>();
		}
		return list;
	}

	/** Director Manage */
	public int createDirectorEntity(DirectorEntity director) {
		logger.info("Create DirectorEntity.");
		this.getHibernateTemplate().saveOrUpdate(director);
		return director.getId();
	}

	public DirectorEntity getDirectorByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		} else {
			final StringBuilder queryStr = new StringBuilder(
					"select distinct d from DirectorEntity as d where d.name like '%")
					.append(name).append("%'");
			List<DirectorEntity> list = (List) this.getHibernateTemplate()
					.execute(new HibernateCallback() {
						public List doInHibernate(Session session) {
							Query q = session.createQuery(queryStr.toString());
							return q.list();
						}
					});

			if (list.size() == 0 || list == null) {
				logger.error("DirectorEntity not found");
				return null;
			}
			return list.get(0);
		}
	}

	public List<DirectorEntity> getDirectors() {
		final StringBuilder queryStr = new StringBuilder(
				"from DirectorEntity d where 1=1");
		List<DirectorEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find DirectorEntity error!");
			return new ArrayList<DirectorEntity>();
		}
		return list;
	}

	/** Type Manage */
	public int createTypeEntity(TypeEntity actor) {
		logger.info("Create TypeEntity.");
		this.getHibernateTemplate().saveOrUpdate(actor);
		return actor.getId();
	}

	public TypeEntity getTypeByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		} else {
			final StringBuilder queryStr = new StringBuilder(
					"select distinct t from TypeEntity as t where t.name like '%")
					.append(name).append("%'");
			List<TypeEntity> list = (List) this.getHibernateTemplate().execute(
					new HibernateCallback() {
						public List doInHibernate(Session session) {
							Query q = session.createQuery(queryStr.toString());
							return q.list();
						}
					});

			if (list.size() == 0 || list == null) {
				logger.error("TypeEntity not found");
				return null;
			}
			return list.get(0);
		}
	}

	public List<TypeEntity> getTypes() {
		final StringBuilder queryStr = new StringBuilder(
				"from TypeEntity t where 1=1");
		List<TypeEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find TypeEntity error!");
			return new ArrayList<TypeEntity>();
		}
		return list;
	}

	/** Area Manage */
	public int createAreaEntity(AreaEntity area) {
		logger.info("Create AreaEntity.");
		this.getHibernateTemplate().saveOrUpdate(area);
		return area.getId();
	}

	public AreaEntity getAreaByName(String name) {
		if (name == null || name.equals("")) {
			return null;
		} else {
			final StringBuilder queryStr = new StringBuilder(
					"select distinct e from AreaEntity as e where e.name like '%")
					.append(name).append("%'");
			List<AreaEntity> list = (List) this.getHibernateTemplate().execute(
					new HibernateCallback() {
						public List doInHibernate(Session session) {
							Query q = session.createQuery(queryStr.toString());
							return q.list();
						}
					});

			if (list.size() == 0 || list == null) {
				logger.error("AreaEntity not found");
				return null;
			}
			return list.get(0);
		}
	}

	public List<AreaEntity> getAreas() {
		final StringBuilder queryStr = new StringBuilder(
				"from AreaEntity e where 1=1");
		List<AreaEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.error("find AreaEntity error!");
			return new ArrayList<AreaEntity>();
		}
		return list;
	}

	/** User Manage */
	public int createUserEntity(UserEntity user) {
		logger.info("Create UserEntity.");
		this.getHibernateTemplate().merge(user);
		return user.getId();
	}

	public UserEntity getUserByIp(String ip) {
		if (ip.trim().length() == 0 || ip == null) {
			logger.error("ip invalid!");
			return null;
		}
		final StringBuilder queryStr = new StringBuilder(
				"from UserEntity as u where u.ip = '").append(ip).append("'");
		List<UserEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.info("User not exist!");
			return null;
		}
		return list.get(0);
	}

	public UserEntity getUserById(int id) {
		logger.info("getUserById.");
		final StringBuilder queryStr = new StringBuilder(
				"from UserEntity as u where u.id = '").append(id).append("'");
		List<UserEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.info("UserEntity not exist!");
			return null;
		}
		return list.get(0);
	}

	/** Record Manage */
	public int createRecordEntity(RecordEntity record) {
		logger.info("Create RecordEntity.");
		this.getHibernateTemplate().saveOrUpdate(record);
		return record.getId();
	}

	public List<RecordEntity> findRecords(int userId, int movieId, int adId) {
		logger.info("find RecordEntity.");
		final StringBuilder queryStr = new StringBuilder(
				"from RecordEntity as r where 1=1");
		if (userId != 0) {
			queryStr.append(" and r.userId = '").append(userId).append("'");
		}
		if (movieId != 0) {
			queryStr.append(" and r.movieId = '").append(movieId).append("'");
		}
		if (adId != 0) {
			queryStr.append(" and r.adId = '").append(adId).append("'");
		}
		List<RecordEntity> list = (List) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.info("User not exist!");
			return new ArrayList<RecordEntity>();
		}
		return list;
	}

	public long getUserRecordCount(int userId) {
		final StringBuilder queryStr = new StringBuilder(
				"select count(*) from RecordEntity as r where r.userId = '")
				.append(userId).append("'");
		long count = (Long) this.getHibernateTemplate().execute(
				new HibernateCallback() {
					public Long doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						return (Long) q.uniqueResult();
					}
				});
		logger.info("------------------------> Movie count : " + count);
		return count;
	}

	/** Similarity Manage */
	public int createMovieAdSimilarityEntity(MovieAdSimilarityEntity similarity) {
		logger.info("Create MovieAdSimilarityEntity.");
		this.getHibernateTemplate().merge(similarity);
		return similarity.getId();
	}

	public List<MovieAdSimilarityEntity> getTopNAdByMovieId(int movieId,
			final int n) {
		logger.info("find MovieAdSimilarityEntity.");
		final StringBuilder queryStr = new StringBuilder(
				"from MovieAdSimilarityEntity as s where s.movieId = '")
				.append(movieId).append("' order by s.similarity DESC");
		List<MovieAdSimilarityEntity> list = (List) this.getHibernateTemplate()
				.execute(new HibernateCallback() {
					public List doInHibernate(Session session) {
						Query q = session.createQuery(queryStr.toString());
						q.setFirstResult(0);
						q.setMaxResults(n);
						return q.list();
					}
				});
		if (list.size() == 0 || list == null) {
			logger.info("MovieAdSimilarityEntity not exist!");
			return new ArrayList<MovieAdSimilarityEntity>();
		}
		return list;
	}
}