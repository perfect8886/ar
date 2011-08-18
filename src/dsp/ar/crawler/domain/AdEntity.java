package dsp.ar.crawler.domain;

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
@Table(name = "CRAWLER_AD")
@TableGenerator(name = "crawler_ad_id", table = "CRAWLER_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "CRAWLER_AD_PK", allocationSize = 1)
public class AdEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "crawler_ad_id")
	@Column(name = "IID")
	private int id;

	@Column(name = "IIMAGE_URL")
	private String imageUrl;

	@Column(name = "NNAME")
	private String name;

	@Column(name = "AAREA")
	private String area;

	@Column(name = "PPUBLISH_TIME")
	private String publishTime;

	@Column(name = "DDESC")
	private String desc;

	@ManyToMany(targetEntity = dsp.ar.crawler.domain.ActorEntity.class, mappedBy = "ads", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<ActorEntity> actors;

	@ManyToMany(targetEntity = dsp.ar.crawler.domain.TypeEntity.class, mappedBy = "ads", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<TypeEntity> types;

	@ManyToMany(targetEntity = dsp.ar.crawler.domain.DirectorEntity.class, mappedBy = "ads", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private List<DirectorEntity> directors;

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

	public String getArea() {
		return area;
	}

	public void setArea(String area) {
		this.area = area;
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
}
