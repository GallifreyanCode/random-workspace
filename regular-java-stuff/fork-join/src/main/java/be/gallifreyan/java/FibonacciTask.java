package be.gallifreyan.java;

import java.util.concurrent.RecursiveTask;

public class FibonacciTask extends RecursiveTask<Long> {
	private static final long serialVersionUID = -6282172956600346242L;

	private static final int THRESHOLD = 5;
	private FibonacciProblem problem;
	public long result;

	public FibonacciTask(FibonacciProblem problem) {
		this.problem = problem;
	}

	public Long compute() {
		/* Compare to threshold,
		 * it determines whether or not to use parallelism.
		 */
		if (problem.n < THRESHOLD) {
			result = problem.solve();
		} else {
			/* If task is large enough,
			 *  create different threads and combine result with fork and join.
			 */
			FibonacciTask worker1 = new FibonacciTask(
					new FibonacciProblem(problem.n - 1));
			
			FibonacciTask worker2 = new FibonacciTask(
					new FibonacciProblem(problem.n - 2));
			
			worker1.fork();
			result = worker2.compute() + worker1.join();
		}
		return result;
	}
	
	
}
