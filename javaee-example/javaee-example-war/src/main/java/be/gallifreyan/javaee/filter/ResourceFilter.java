package be.gallifreyan.javaee.filter;

import java.io.IOException;

import javax.servlet.*;
import javax.servlet.http.HttpServletResponse;

/**
 * This filter is meant to protect facelet resources that are never meant to be
 * accessed directly by end-users. Facelet resources include stylesheets,
 * templates and other files that may be present in the document root, and
 * browseable by end-users.
 * 
 * @author Vineet Reynolds
 * 
 */
public class ResourceFilter implements Filter
{

	private String contextPath;

	/**
	 * Default constructor.
	 */
	public ResourceFilter()
	{
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
	}

	/**
	 * Redirects all requests arriving at the URLs protected from the user, to
	 * the login page. The JSF runtime will continue to access these URLs
	 * normally using it's resource resolver.
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpResponse.sendRedirect(contextPath + "/Login.xhtml");
		return;
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		contextPath = fConfig.getServletContext().getContextPath();
	}

}
