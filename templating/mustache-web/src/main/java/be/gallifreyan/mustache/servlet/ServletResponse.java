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

import be.gallifreyan.mustache.Example;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;
import com.itextpdf.text.Document;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfWriter;

public class ServletResponse extends HttpServlet {
	private static final long serialVersionUID = -5654592392140368901L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
	}

	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		makePdf(request, response, "GET");
	}

	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		makePdf(request, response, "POST");
	}

	public void makePdf(HttpServletRequest request,
			HttpServletResponse response, String methodGetPost)
			throws ServletException, IOException {
		try {

			Document document = new Document();
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			PdfWriter.getInstance(document, baos);
			document.open();
			
			/**
			 * MUSTACHE
			 */
			MustacheFactory mf = new DefaultMustacheFactory();
			Mustache mustache = mf.compile("developer.mustache");
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			mustache.execute(new PrintWriter(outputStream), new Example())
						.flush();
			String string = outputStream.toString();
			document.add(new Paragraph(string));
			document.close();
			response.setHeader("Expires", "0");
			response.setHeader("Cache-Control",
					"must-revalidate, post-check=0, pre-check=0");
			response.setHeader("Pragma", "public");
			response.setContentType("application/pdf");
			response.setContentLength(baos.size());
			ServletOutputStream out = response.getOutputStream();
			baos.writeTo(out);
			out.flush();
			out.close();
		} catch (Exception e2) {
			System.out.println("Error in " + getClass().getName() + "\n" + e2);
		}
	}
}