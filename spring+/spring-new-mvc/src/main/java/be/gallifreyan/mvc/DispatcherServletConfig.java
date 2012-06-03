package be.gallifreyan.mvc;

import java.util.EnumSet;
import java.util.Set;

import javax.servlet.DispatcherType;
import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRegistration;

import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import be.gallifreyan.config.ContextConfig;
import be.gallifreyan.config.PersistenceJPAConfig;
import be.gallifreyan.config.WebMvcConfig;
/**
 * Java configuration that replaces the web.xml
 */
public class DispatcherServletConfig implements WebApplicationInitializer {
	private static final String PROFILE = "test";
	
	public void onStartup(ServletContext servletContext) throws ServletException {
		AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
		//TODO: FIX THESE REGISTRATIONS
		rootContext.getEnvironment().setActiveProfiles(PROFILE);
		rootContext.scan("be.gallifreyan.config.profile");
		rootContext.register(ContextConfig.class, PersistenceJPAConfig.class);
		
		/* Security */
		FilterRegistration.Dynamic securityFilter = servletContext.addFilter("securityFilter", new DelegatingFilterProxy("springSecurityFilterChain"));
		securityFilter.addMappingForUrlPatterns(EnumSet.allOf(DispatcherType.class), true, "/*");
		
		servletContext.addListener(new ContextLoaderListener(rootContext));
		servletContext.setInitParameter("defaultHtmlEscape", "true");
		
		AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
		mvcContext.getEnvironment().setActiveProfiles(PROFILE);
		mvcContext.scan("be.gallifreyan.config.profile");
		mvcContext.register(WebMvcConfig.class, ContextConfig.class, PersistenceJPAConfig.class);
		
		ServletRegistration.Dynamic appServlet = servletContext.addServlet("spring", new DispatcherServlet(mvcContext));
		appServlet.setLoadOnStartup(1);
		Set<String> mappingConflicts = appServlet.addMapping("/");
		
		/* Spring WS
		ServletRegistration.Dynamic greetinService = servletContext.addServlet("GreetinService", "be.gallifreyan.ws.ExampleEndpoint");
		greetinService.addMapping("/GreetingService"); */
		
		if (!mappingConflicts.isEmpty()) {
			throw new IllegalStateException("'spring' cannot be mapped to '/' under Tomcat versions <= 7.0.14 or Jetty without webdefault tweak.");
		}		
	}
}
