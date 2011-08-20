package dsp.ar.servlet;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;

import dsp.ar.IAR;
import dsp.ar.impl.ARImpl;

public class AdServlet extends HttpServlet {
	private static final Logger logger = Logger.getLogger(ARServlet.class);
	IAR ar;

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
		}
	}

	private void doList(HttpServletRequest req, HttpServletResponse resp) {
		logger.info("<-----------------get ad list-------------------->");
		int start = Integer.parseInt(req.getParameter("start"));
		int limit = Integer.parseInt(req.getParameter("limit"));
		String adList = this.getIAR().listAdToJson(start, limit);
		try {
			resp.setCharacterEncoding("utf-8");
			PrintWriter writer = resp.getWriter();
			writer.write(adList);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
