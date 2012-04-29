package be.gallifreyan.java.map;

import java.util.ArrayList;
import java.util.List;

/**
 * Based on
 * http://www.javacodegeeks.com/2012/03/functional-programming-with-map-and.html
 */
public class MapExample {

	/*
	 * Map like operation in Java #1.
	 * 
	 * For each element of the list do something and return the "updated" list.
	 */
	public List<Double> addVAT(List<Double> amounts, double rate) {
		final List<Double> amountsWithVAT = new ArrayList<Double>();
		for (double amount : amounts) {
			amountsWithVAT.add(addVAT(amount, rate));
		}
		return amountsWithVAT;
	}
	
	public double addVAT(double amount, double rate) {
		return amount * (1 + rate);
	}
	
	/*
	 * Map like operation in Java #2.
	 * 
	 * In Java we create an output list,
	 *  call method on each element of input list and put result in output list,
	 *  return output list.
	 *  
	 * That's what Map is,
	 *  apply someMethod(T):T to each element of a list<T>,
	 *   which returns list<T> with same size.
	 */
	public List<Double> convertCurrency(List<Double> amounts,
			double currencyRate) {
		final List<Double> amountsInCurrency = new ArrayList<Double>();
		for (double amount : amounts) {
			amountsInCurrency.add(convertCurrency(amount, currencyRate));
		}
		return amountsInCurrency;
	}
	
	public double convertCurrency(double amount, double currencyRate) {
		return amount / currencyRate;
	}
}
