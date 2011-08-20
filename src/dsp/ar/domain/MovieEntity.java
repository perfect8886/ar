package dsp.ar.domain;

import java.io.Serializable;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "AR_MOVIE")
@TableGenerator(name = "ar_movie_id", table = "AR_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "AR_MOVIE_PK", allocationSize = 1)
public class MovieEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ar_movie_id")
	@Column(name = "IID")
	private int id;

	@Column(name = "IIMAGE_URL")
	private String imageUrl;

	@Column(name = "NNAME")
	private String name;

	@Column(name = "PPUBLISH_TIME")
	private String publishTime;

	@Column(name = "UURL")
	private String url;

	@Column(name = "DDESC")
	private String desc;

	@ManyToMany(targetEntity = dsp.ar.domain.ActorEntity.class, mappedBy = "movies", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private List<ActorEntity> actors;

	@ManyToMany(targetEntity = dsp.ar.domain.TypeEntity.class, mappedBy = "movies", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TypeEntity> types;

	@ManyToMany(targetEntity = dsp.ar.domain.DirectorEntity.class, mappedBy = "movies", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DirectorEntity> directors;

	@ManyToMany(targetEntity = dsp.ar.domain.AreaEntity.class, mappedBy = "movies", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<AreaEntity> areas;

	public String getImageUrl() {
		return imageUrl;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPublishTime() {
		return publishTime;
	}

	public void setPublishTime(String publishTime) {
		this.publishTime = publishTime;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public List<ActorEntity> getActors() {
		return actors;
	}

	public void setActors(List<ActorEntity> actors) {
		this.actors = actors;
	}

	public List<TypeEntity> getTypes() {
		return types;
	}

	public void setTypes(List<TypeEntity> types) {
		this.types = types;
	}

	public List<DirectorEntity> getDirectors() {
		return directors;
	}

	public void setDirectors(List<DirectorEntity> directors) {
		this.directors = directors;
	}

	public List<AreaEntity> getAreas() {
		return areas;
	}

	public void setAreas(List<AreaEntity> areas) {
		this.areas = areas;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}
}