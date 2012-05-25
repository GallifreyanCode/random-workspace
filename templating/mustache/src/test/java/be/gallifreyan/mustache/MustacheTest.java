package be.gallifreyan.mustache;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;

import org.junit.Before;
import org.junit.Test;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.github.mustachejava.MustacheFactory;

/**
 * Mustache Manual: http://mustache.github.com/mustache.5.html
 */
public class MustacheTest {
	private static final String TEMPLATE = "developer.mustache";
	ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
	private Mustache mustache;

	@Before
	public void setUp() {
		MustacheFactory mf = new DefaultMustacheFactory();
		assertNotNull(mf);
		mustache = mf.compile(TEMPLATE);
		assertNotNull(mustache);
	}

	@Test
	public void testExample() {
		drawTitle("Example");
		try {
			mustache.execute(new PrintWriter(outputStream), new Example())
					.flush();
			assertNotNull(outputStream);
			String string = outputStream.toString();
			System.out.println(string);
			assertTrue(string.contains("First Name: Name1"));
			assertTrue(string.contains("First Name: Name2"));
			assertTrue(string.contains("Last Name: LastName1"));
			assertTrue(string.contains("Last Name: LastName2"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Test
	public void testEmptyExample() {
		drawTitle("EmptyExample");
		try {
			mustache.execute(new PrintWriter(outputStream), new EmptyExample())
					.flush();
			assertNotNull(outputStream);
			String string = outputStream.toString();
			System.out.println(string);
			assertTrue(string.contains("No developers"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	private void drawLine() {
		System.out
				.println("**************************************************");
	}

	private void drawTitle(String title) {
		drawLine();
		System.out.println(title + ":");
		drawLine();
	}
}
