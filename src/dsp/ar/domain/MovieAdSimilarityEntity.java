package dsp.ar.domain;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "AR_MOVIE_AD_SIMILARITY")
@TableGenerator(name = "ar_movie_ad_similiarity_id", table = "AR_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "AR_MOVIE_AD_SIMILARITY_PK", allocationSize = 1)
public class MovieAdSimilarityEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ar_movie_ad_similiarity_id")
	@Column(name = "IID")
	private int id;

	@Column(name = "MMOVIE_ID")
	private int movieId;

	@Column(name = "AAD_ID")
	private int adId;

	@Column(name = "SSIMILARITY")
	private double similarity;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getMovieId() {
		return movieId;
	}

	public void setMovieId(int movieId) {
		this.movieId = movieId;
	}

	public int getAdId() {
		return adId;
	}

	public void setAdId(int adId) {
		this.adId = adId;
	}

	public double getSimilarity() {
		return similarity;
	}

	public void setSimilarity(double similarity) {
		this.similarity = similarity;
	}
}