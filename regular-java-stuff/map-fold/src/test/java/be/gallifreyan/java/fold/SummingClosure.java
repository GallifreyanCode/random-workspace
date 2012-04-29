package be.gallifreyan.java.fold;

public class SummingClosure implements Closure<Double> {
	private double sum = 0;

	public Double execute(Double value) {
		sum += value;
		return sum;
	}
}
