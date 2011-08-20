package dsp.ar.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "AR_STRATEGY")
@TableGenerator(name = "ar_strategy_id", table = "AR_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "AR_STRATEGY_PK", allocationSize = 1)
public class StrategyEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ar_strategy_id")
	@Column(name = "IID")
	private int id;

	@OneToOne(optional = true, cascade = CascadeType.ALL, mappedBy = "strategy")
	private AdEntity ad;

	@Column(name = "PPUBLISH_START_DATE")
	private String publishStartDate;

	@Column(name = "PPUBLISH_END_DATE")
	private String publishEndDate;

	@Column(name = "SSTART_DAY_TIME")
	private String startDayTime;

	@Column(name = "EEND_DAY_TIME")
	private String endDayTime;

	@Column(name = "IIP_PREFIX")
	private String ipPrefix;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public AdEntity getAd() {
		return ad;
	}

	public void setAd(AdEntity ad) {
		this.ad = ad;
	}

	public String getPublishStartDate() {
		return publishStartDate;
	}

	public void setPublishStartDate(String publishStartDate) {
		this.publishStartDate = publishStartDate;
	}

	public String getPublishEndDate() {
		return publishEndDate;
	}

	public void setPublishEndDate(String publishEndDate) {
		this.publishEndDate = publishEndDate;
	}

	public String getStartDayTime() {
		return startDayTime;
	}

	public void setStartDayTime(String startDayTime) {
		this.startDayTime = startDayTime;
	}

	public String getEndDayTime() {
		return endDayTime;
	}

	public void setEndDayTime(String endDayTime) {
		this.endDayTime = endDayTime;
	}

	public String getIpPrefix() {
		return ipPrefix;
	}

	public void setIpPrefix(String ipPrefix) {
		this.ipPrefix = ipPrefix;
	}
}
