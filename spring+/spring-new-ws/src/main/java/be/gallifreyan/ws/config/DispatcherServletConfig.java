package be.gallifreyan.ws.config;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.servlet.DispatcherServlet;
/**
 * Java configuration that replaces the web.xml
 */
public class DispatcherServletConfig implements WebApplicationInitializer {
	
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(WSConfig.class);
		
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.setInitParameter("defaultHtmlEscape", "true");
		//servletContext.setInitParameter("transformWsdlLocations", "true");

		ServletRegistration.Dynamic appServlet = servletContext.addServlet("spring", new DispatcherServlet());
		appServlet.setLoadOnStartup(1);
		Set<String> mappingConflicts = appServlet.addMapping("/*");
		
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException("'spring' cannot be mapped to '/' under Tomcat versions <= 7.0.14 or Jetty without webdefault tweak.");
		}		
	}
}
