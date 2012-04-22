package be.gallifreyan.javaee.filter;

import java.io.IOException;
import java.security.Principal;

import javax.servlet.*;
import javax.servlet.http.*;

/**
 * This filter is used to protect the private section of the application from
 * unauthenticated users.
 * 
 * @author Vineet Reynolds
 * 
 */
public class AuthenticationFilter implements Filter
{

	private String contextPath;

	/**
	 * Default constructor.
	 */
	public AuthenticationFilter()
	{
	}

	/**
	 * @see Filter#destroy()
	 */
	public void destroy()
	{
	}

	/**
	 * Verifies if the request is associated with a {@link Principal}. If no
	 * {@link Principal} is found, then the user is redirected to the Login page
	 * of the application. Otherwise, the request is chained to next filter, or
	 * the web-resource.
	 * 
	 * @see Filter#doFilter(ServletRequest, ServletResponse, FilterChain)
	 */
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException,
			ServletException
	{
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		if (httpRequest.getUserPrincipal() == null)
		{
			httpResponse.sendRedirect(contextPath + "/Login.xhtml");
			return;
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
