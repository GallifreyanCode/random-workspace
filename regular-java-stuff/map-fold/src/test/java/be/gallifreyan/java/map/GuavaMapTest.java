package be.gallifreyan.java.map;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.google.common.base.Function;
import com.google.common.collect.Iterables;

public class GuavaMapTest {

	/*
	 * Iterables.transform(Iterable, Function)
	 * Lists.transform(List, Function)
	 * Maps.transformValues(Map, Function)
	 * == Map operator
	 */
	@Test
	public void MapExample() {
		List<Double> exVat = Arrays.asList(new Double[] { 99., 127., 35. });
		 Iterable<Double> incVat = Iterables.transform(exVat, new Function<Double, Double>() {
		   public Double apply(Double exVat) {
		     return exVat * (1.196);
		   }
		 });
		 assertEquals(incVat.toString(), "[118.404, 151.892, 41.86]");
	}
}
