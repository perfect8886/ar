package dsp.ar.crawler.server;

import java.io.File;

import org.apache.log4j.Logger;
import org.mortbay.jetty.Connector;
import org.mortbay.jetty.Server;
import org.mortbay.jetty.nio.SelectChannelConnector;
import org.mortbay.jetty.webapp.Configuration;
import org.mortbay.jetty.webapp.WebAppContext;
import org.mortbay.jetty.webapp.WebXmlConfiguration;
import org.mortbay.util.URIUtil;

public class WebServer {

	public WebServer(String contextDir) {
		this.contextDir = contextDir;
	}

	private static final Logger logger = Logger.getLogger(WebServer.class);
	private String contextDir = "context";

	public void start() {
		try {
			Server server = new Server();
			Connector con = new SelectChannelConnector();
			con.setPort(8090);
			server.addConnector(con);

			WebAppContext webapp = new WebAppContext();
			{
				File file = new File(contextDir);
				System.out.println(file.getAbsolutePath());
				if (file.exists()) {
					webapp.setResourceBase(contextDir);
				}
			}
			webapp.setContextPath(URIUtil.SLASH);
			webapp.setDefaultsDescriptor(contextDir);
			webapp.setDefaultsDescriptor(this.getClass().getResource(
					"webdefault.xml").toString());

			WebXmlConfiguration configuration = new WebXmlConfiguration();
			configuration.setWebAppContext(webapp);

			webapp.setConfigurations(new Configuration[] { configuration });
			server.addHandler(webapp);
			server.start();
		} catch (Exception ex) {
			System.err.println(ex);
		}
	}

	public void stop() {

	}

	/**
	 * @param args
	 * @throws InterruptedException
	 */
	public static void main(String[] args) throws InterruptedException {
		org.apache.log4j.PropertyConfigurator.configure(WebServer.class
				.getResource("log4j.properties"));
		WebServer webServer = new WebServer("context");
		webServer.start();
		Thread.sleep(600000);
	}
}