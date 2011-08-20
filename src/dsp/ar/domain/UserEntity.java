package dsp.ar.domain;

import java.io.Serializable;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.TableGenerator;

@SuppressWarnings("serial")
@Entity
@Table(name = "AR_USER")
@TableGenerator(name = "ar_user_id", table = "AR_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "AR_USER_PK", allocationSize = 1)
public class UserEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "ar_user_id")
	@Column(name = "IID")
	private int id;

	@Column(name = "IIP")
	private String ip;

	@OneToOne(cascade = CascadeType.ALL, optional = true)
	@JoinColumn(name = "IINTEREST_MODEL_ID", referencedColumnName = "IID", unique = true)
	private InterestModelEntity interestModel;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public InterestModelEntity getInterestModel() {
		return interestModel;
	}

	public void setInterestModel(InterestModelEntity interestModel) {
		this.interestModel = interestModel;
	}
}