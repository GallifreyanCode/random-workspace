package be.gallifreyan.java.fold;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

public class CustomFoldTest {

	@Test
	public void FoldTest() {
		SummingClosure closure = new SummingClosure();
		List<Double> exVat = Arrays.asList(new Double[] { 99., 127., 35. });
		Double result = foreach(exVat, closure);
		Double expected = 261.0;
		assertEquals(expected, result);
	}

	public final static <T> T foreach(Iterable<T> list, Closure<T> closure) {
		T result = null;
		for (T t : list) {
			result = closure.execute(t);
		}
		return result;
	}
}
