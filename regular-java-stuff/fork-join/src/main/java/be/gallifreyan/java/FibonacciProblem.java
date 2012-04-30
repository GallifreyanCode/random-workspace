package be.gallifreyan.java;

public class FibonacciProblem {
	public int n;

	 public FibonacciProblem(int n) {
	  this.n = n;
	 }
	 
	 public long solve() {
	  return fibonacci(n);
	 }
	 
	 private long fibonacci(int n) {
	  /* System.out.println("Thread: " + Thread.currentThread().getName() + " calculates " + n); */
	  if (n <= 1)
	   return n;
	  else 
	   return fibonacci(n-1) + fibonacci(n-2);
	 }
}
