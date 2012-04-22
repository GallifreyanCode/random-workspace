package be.gallifreyan.javaee.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This filter is used to redirect authenticated users to the landing page for
 * authenticated users. This will ensure that users will not be confused when
 * accessing the Login page amongst others, when already logged in.
 * 
 * @author Vineet Reynolds
 * 
 */
public class UserRedirectFilter implements Filter
{

	private String contextPath;

	/**
	 * Default constructor.
	 */
	public UserRedirectFilter()
	{
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
	}

	/**
	 * Redirects the user to the landing page of the application for
	 * authenticated users, if the request is associated with a
	 * {@link Principal}.
	 * 
	 * If no {@link Principal} is found, then the request is chained to the next
	 * filter or the web-resource.
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest.getUserPrincipal() != null)
		{
			httpResponse.sendRedirect(contextPath + "/private/HomePage.xhtml");
		}

		// pass the request along the filter chain
		chain.doFilter(request, response);
	}

	/**
	 * @see Filter#init(FilterConfig)
	 */
	public void init(FilterConfig fConfig) throws ServletException
	{
		contextPath = fConfig.getServletContext().getContextPath();
	}

}
