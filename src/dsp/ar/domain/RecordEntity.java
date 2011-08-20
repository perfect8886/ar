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
@Table(name = "AR_RECORD")
@TableGenerator(name = "ar_record_id", table = "AR_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "AR_RECORD_PK", allocationSize = 1)
public class RecordEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ar_record_id")
	@Column(name = "IID")
	private int id;

	@Column(name = "UUSER_ID")
	private int userId;

	@Column(name = "MMOVIE_ID")
	private int movieId;

	@Column(name = "AAD_ID")
	private int adId;

	@Column(name = "TTIME")
	private String time;

	@Column(name = "AAD_RATE")
	private int adRate;

	@Column(name = "MMOVIE_RATE")
	private int movieRate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
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

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getAdRate() {
		return adRate;
	}

	public void setAdRate(int adRate) {
		this.adRate = adRate;
	}

	public int getMovieRate() {
		return movieRate;
	}

	public void setMovieRate(int movieRate) {
		this.movieRate = movieRate;
	}
}