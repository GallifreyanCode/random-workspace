package be.gallifreyan.ws.config;

import java.util.Set;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.ws.transport.http.MessageDispatcherServlet;
/**
 * Java configuration that replaces the web.xml
 */
public class DispatcherServletConfig implements WebApplicationInitializer {
	
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		rootContext.register(WSConfig.class);
		
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.setInitParameter("defaultHtmlEscape", "true");
		
		ServletRegistration.Dynamic appServlet = servletContext.addServlet("spring", new MessageDispatcherServlet());
		appServlet.setLoadOnStartup(1);
		Set<String> mappingConflicts = appServlet.addMapping("/*");
		
		/* Spring WS
		ServletRegistration.Dynamic greetinService = servletContext.addServlet("GreetinService", "be.gallifreyan.ws.ExampleEndpoint");
		greetinService.addMapping("/GreetingService"); */
		
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException("'spring' cannot be mapped to '/' under Tomcat versions <= 7.0.14 or Jetty without webdefault tweak.");
		}		
	}
}
