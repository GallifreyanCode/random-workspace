package be.gallifreyan.java.fold;

import java.util.List;

public class FoldExample {
	
	/*
	 * Using += we fold all items of the list into one.
	 * This is similar to the Map operator,
	 *  except the result is one item instead of a list, a scalar.
	 * Again in functional programming this is called the Fold or Reduce operator.
	 * 
	 * Like with the Map operator you have someMethod(T) and is applied repeatedly to each element from the input list<T>,
	 *  the result is one single element T. We can find this kind of operation in String concatenation, List.addAll(),..
	 */
	public double totalAmount(List<Double> amounts) {
		double sum = 0;
		for (double amount : amounts) {
			sum += amount;
		}
		return sum;
	}
}
