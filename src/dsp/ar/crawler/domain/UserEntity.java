package dsp.ar.crawler.domain;

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
@Table(name = "CRAWLER_USER")
@TableGenerator(name = "crawler_user_id", table = "CRAWLER_TABLE_GENERATOR", pkColumnName = "GGEN_NAME", valueColumnName = "GGEN_VALUE", pkColumnValue = "CRAWLER_USER_PK", allocationSize = 1)
public class UserEntity implements Serializable {
	@Id
	@GeneratedValue(strategy = GenerationType.TABLE, generator = "crawler_user_id")
	@Column(name = "IID")
	private int id;

	@Column(name = "IIP")
	private String ip;

	@Column(name = "TTOTAL")
	private int total;

	@Column(name = "AACTION_FACTOR")
	private double actionFactor;

	@Column(name = "SSUSPENSE_FACTOR")
	private double suspenseFactor;

	@Column(name = "FFANTASY_FACTOR")
	private double fantasyFactor;

	@Column(name = "FFAMILY_FACTOR")
	private double familyFactor;

	@Column(name = "CCOMEDY_FACTOR")
	private double comedyFactor;

	@Column(name = "AANIMATION_FACTOR")
	private double animationFactor;

	@Column(name = "AADVENTURE_FACTOR")
	private double adventureFactor;

	@Column(name = "FFICTION_FACTOR")
	private double fictionFactor;

	@Column(name = "CCHILD_FACTOR")
	private double childFactor;

	@Column(name = "SSTORY_FACTOR")
	private double storyFactor;

	@Column(name = "LLOVE_FACTOR")
	private double loveFactor;

	@Column(name = "DDOCUMENT_FACTOR")
	private double documentoryFactor;

	@Column(name = "TTHRILLER_FACTOR")
	private double thrillerFactor;

	@Column(name = "CCRIME_FACTOR")
	private double crimeFactor;

	@Column(name = "VVIDEO_FACTOR")
	private double videoFactor;

	@Column(name = "TTERROR_FACTOR")
	private double terrorFactor;

	@Column(name = "WWAR_FACTOR")
	private double warFactor;

	@Column(name = "WWEST_FACTOR")
	private double westFactor;

	@Column(name = "MMUSIC_FACTOR")
	private double musicFactor;

	@Column(name = "SSPORT_FACTOR")
	private double sportFactor;

	@Column(name = "HHISTORY_FACTOR")
	private double historyFactor;

	@Column(name = "BBIOGRAPHY_FACTOR")
	private double biographyFactor;

	@Column(name = "MMARTIAL_ARTS_FACTOR")
	private double martialArtsFactor;

	@Column(name = "HHOMOSEXUAL_FACTOR")
	private double homosexualFactor;

	@Column(name = "PPHANTOM_FACTOR")
	private double phantomFactor;

	@Column(name = "AANCIENT_COSTUME_FACTOR")
	private double ancientCostumeFactor;

	@Column(name = "DDANCE_FACTOR")
	private double danceFactor;

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

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public double getActionFactor() {
		return actionFactor;
	}

	public void setActionFactor(double actionFactor) {
		this.actionFactor = actionFactor;
	}

	public double getSuspenseFactor() {
		return suspenseFactor;
	}

	public void setSuspenseFactor(double suspenseFactor) {
		this.suspenseFactor = suspenseFactor;
	}

	public double getFantasyFactor() {
		return fantasyFactor;
	}

	public void setFantasyFactor(double fantasyFactor) {
		this.fantasyFactor = fantasyFactor;
	}

	public double getFamilyFactor() {
		return familyFactor;
	}

	public void setFamilyFactor(double familyFactor) {
		this.familyFactor = familyFactor;
	}

	public double getComedyFactor() {
		return comedyFactor;
	}

	public void setComedyFactor(double comedyFactor) {
		this.comedyFactor = comedyFactor;
	}

	public double getAnimationFactor() {
		return animationFactor;
	}

	public void setAnimationFactor(double animationFactor) {
		this.animationFactor = animationFactor;
	}

	public double getAdventureFactor() {
		return adventureFactor;
	}

	public void setAdventureFactor(double adventureFactor) {
		this.adventureFactor = adventureFactor;
	}

	public double getFictionFactor() {
		return fictionFactor;
	}

	public void setFictionFactor(double fictionFactor) {
		this.fictionFactor = fictionFactor;
	}

	public double getChildFactor() {
		return childFactor;
	}

	public void setChildFactor(double childFactor) {
		this.childFactor = childFactor;
	}

	public double getStoryFactor() {
		return storyFactor;
	}

	public void setStoryFactor(double storyFactor) {
		this.storyFactor = storyFactor;
	}

	public double getLoveFactor() {
		return loveFactor;
	}

	public void setLoveFactor(double loveFactor) {
		this.loveFactor = loveFactor;
	}

	public double getDocumentoryFactor() {
		return documentoryFactor;
	}

	public void setDocumentoryFactor(double documentoryFactor) {
		this.documentoryFactor = documentoryFactor;
	}

	public double getThrillerFactor() {
		return thrillerFactor;
	}

	public void setThrillerFactor(double thrillerFactor) {
		this.thrillerFactor = thrillerFactor;
	}

	public double getCrimeFactor() {
		return crimeFactor;
	}

	public void setCrimeFactor(double crimeFactor) {
		this.crimeFactor = crimeFactor;
	}

	public double getVideoFactor() {
		return videoFactor;
	}

	public void setVideoFactor(double videoFactor) {
		this.videoFactor = videoFactor;
	}

	public double getTerrorFactor() {
		return terrorFactor;
	}

	public void setTerrorFactor(double terrorFactor) {
		this.terrorFactor = terrorFactor;
	}

	public double getWarFactor() {
		return warFactor;
	}

	public void setWarFactor(double warFactor) {
		this.warFactor = warFactor;
	}

	public double getWestFactor() {
		return westFactor;
	}

	public void setWestFactor(double westFactor) {
		this.westFactor = westFactor;
	}

	public double getMusicFactor() {
		return musicFactor;
	}

	public void setMusicFactor(double musicFactor) {
		this.musicFactor = musicFactor;
	}

	public double getSportFactor() {
		return sportFactor;
	}

	public void setSportFactor(double sportFactor) {
		this.sportFactor = sportFactor;
	}

	public double getHistoryFactor() {
		return historyFactor;
	}

	public void setHistoryFactor(double historyFactor) {
		this.historyFactor = historyFactor;
	}

	public double getBiographyFactor() {
		return biographyFactor;
	}

	public void setBiographyFactor(double biographyFactor) {
		this.biographyFactor = biographyFactor;
	}

	public double getMartialArtsFactor() {
		return martialArtsFactor;
	}

	public void setMartialArtsFactor(double martialArtsFactor) {
		this.martialArtsFactor = martialArtsFactor;
	}

	public double getHomosexualFactor() {
		return homosexualFactor;
	}

	public void setHomosexualFactor(double homosexualFactor) {
		this.homosexualFactor = homosexualFactor;
	}

	public double getPhantomFactor() {
		return phantomFactor;
	}

	public void setPhantomFactor(double phantomFactor) {
		this.phantomFactor = phantomFactor;
	}

	public double getAncientCostumeFactor() {
		return ancientCostumeFactor;
	}

	public void setAncientCostumeFactor(double ancientCostumeFactor) {
		this.ancientCostumeFactor = ancientCostumeFactor;
	}

	public double getDanceFactor() {
		return danceFactor;
	}

	public void setDanceFactor(double danceFactor) {
		this.danceFactor = danceFactor;
	}
}