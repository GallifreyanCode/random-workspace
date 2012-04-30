package be.gallifreyan.java;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ImprovedFibonacciProblem {
	public int n;
	private static ConcurrentMap<Integer, Long> result = new ConcurrentHashMap<Integer, Long>();

	public ImprovedFibonacciProblem(int n) {
		this.n = n;
	}

	public long solve() {
		return fibonacci(n);
	}

	private long fibonacci(int n) {
		if (n <= 1) {
			return n;
		} else {
			Long fibonacci1 = getFib(n - 2);
			Long fibonacci = getFib(n - 1);
			return fibonacci + fibonacci1;
		}
	}

	private Long getFib(int n) {
		Long fibonacci = (Long) result.get(n);
		if (fibonacci == null) {
			fibonacci = fibonacci(n);
			result.put(n, fibonacci);
		}
		return fibonacci;
	}
}
