package dsp.ar.crawler.util;

import java.io.File;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.context.support.FileSystemXmlApplicationContext;

public class AppContext {

	private static ApplicationContext ctx;

	public static ApplicationContext getCtx() {
		if (ctx == null) {
			File file = new File("conf/db/mysql/spring-beans.xml");
			if (file.exists()) {
				ctx = new FileSystemXmlApplicationContext(
						"conf/db/mysql/spring-beans.xml");
			} else {
				ctx = new ClassPathXmlApplicationContext(
						"META-INF/spring-beans.xml");
			}
		}
		return ctx;
	}

	public static Object getBean(String beanName) {
		ApplicationContext ctx = AppContext.getCtx();
		return ctx.getBean(beanName);
	}
}