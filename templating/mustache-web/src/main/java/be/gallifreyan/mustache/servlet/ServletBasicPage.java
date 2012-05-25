package be.gallifreyan.mustache.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import be.gallifreyan.mustache.page.BasicPage;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

public class ServletBasicPage extends HttpServlet {
	private static final long serialVersionUID = -5654592392140368901L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		createBasicPage(request, response, "GET");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		createBasicPage(request, response, "POST");
	}

	public void createBasicPage(HttpServletRequest request,
			HttpServletResponse response, String methodGetPost)
			throws ServletException, IOException {
		try {
			/**
			 * MUSTACHE
			 */
			MustacheFactory mf = new DefaultMustacheFactory();
			Mustache mustache = mf.compile("basicPage.mustache");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			mustache.execute(new PrintWriter(outputStream), new BasicPage())
						.flush();
			
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentType("text/html");
			response.setContentLength(outputStream.size());
			ServletOutputStream out = response.getOutputStream();
			outputStream.writeTo(out);
			out.flush();
			out.close();
		} catch (Exception e2) {
			System.out.println("Error in " + getClass().getName() + "\n" + e2);
		}
	}

}
