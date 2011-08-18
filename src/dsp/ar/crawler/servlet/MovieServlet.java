package dsp.ar.crawler.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import dsp.ar.crawler.IAR;
import dsp.ar.crawler.domain.RecordEntity;
import dsp.ar.crawler.domain.UserEntity;
import dsp.ar.crawler.impl.ARImpl;

public class MovieServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(MovieServlet.class);
	IAR ar;
	private String ip = "";
	private String movieId = "";
	private String adId = "";

	public IAR getIAR() {
		if (ar == null) {
			ar = new ARImpl();
		}
		return ar;
	}

	protected void doGet(HttpServletRequest req, HttpServletResponse resp) {
		this.doPost(req, resp);
	}

	protected void doPost(HttpServletRequest req, HttpServletResponse resp) {
		String method = req.getParameter("method");
		if (method.equalsIgnoreCase("list")) {
			doList(req, resp);
			ip = req.getRemoteAddr();
			logger.info("User from : " + ip);
			// create new user if not exist!
			doCheckIp(ip);
		} else if (method.equalsIgnoreCase("viewMovie")) {
			logger.info("<-------------viewMovie------------->");
			ip = req.getRemoteAddr();
			logger.info("User from : " + ip);
			movieId = req.getParameter("id");
			logger.info("Movie ID : " + movieId);
			doRecordUserMovie(ip, Integer.parseInt(movieId));
		} else if (method.equalsIgnoreCase("listAds")) {
			doAdRecommend(req, resp);
		} else if (method.equalsIgnoreCase("viewAd")) {
			logger.info("<---------------viewAd----------------->");
			ip = req.getRemoteAddr();
			logger.info("User from : " + ip);
			adId = req.getParameter("id");
			logger.info("Ad ID : " + adId);
			doRecordUserAd(ip, Integer.parseInt(adId));
		}
	}

	private void doRecordUserAd(String ip, int adId) {
		UserEntity user = ar.getUserByIp(ip);
		RecordEntity record = ar.getLatestRecord(user.getId());
		if (record != null) {
			record.setAdId(adId);
			record.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
					.format(new Date()));
			ar.createRecord(record);
			ar.updateUserByAd(adId, user);
		}
	}

	private void doRecordUserMovie(String ip, int movieId) {
		RecordEntity record = new RecordEntity();
		record.setMovieId(movieId);
		UserEntity user = ar.getUserByIp(ip);
		record.setUserId(user.getId());
		record.setTime(new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSZ")
				.format(new Date()));
		ar.createRecord(record);
		ar.updateUserByMovie(movieId, user);
	}

	private void doCheckIp(String ip) {
		UserEntity user = ar.getUserByIp(ip);
		if (user == null) {
			// if not exist, create it
			user = new UserEntity();
			user.setIp(ip);
			ar.createUser(user);
		}
	}

	private void doAdRecommend(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("<---------------CORE : Ad recomment-------------->");
		String ip = req.getRemoteAddr();
		UserEntity user = ar.getUserByIp(ip);
		int userId = user.getId();
		RecordEntity record = ar.getLatestRecord(userId);
		int movieId = record.getMovieId();
		// use userId and movieId to do recommend
		String adList = this.getIAR().listAdToJson(userId, movieId);
		try {
			resp.setCharacterEncoding("utf-8");
			PrintWriter writer = resp.getWriter();
			writer.write(adList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void doList(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("<-----------------get movie list-------------------->");
		int start = Integer.parseInt(req.getParameter("start"));
		int limit = Integer.parseInt(req.getParameter("limit"));
		String movieList = this.getIAR().listMoiveToJson(start, limit);
		try {
			resp.setCharacterEncoding("utf-8");
			PrintWriter writer = resp.getWriter();
			writer.write(movieList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}