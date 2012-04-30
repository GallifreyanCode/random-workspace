package be.gallifreyan.java;

import java.util.concurrent.ForkJoinPool;

import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Test;
import org.perf4j.StopWatch;

public class FibonacciProblemTest {
	private StopWatch stopWatch;
	/* From 40 or more there are clear performance difference */
	private int n = 50;
	private int processors;
	private static long regularTime;
	private static long forkJoinTime;
	
	@Before
	public void startUp() {
		/* Check the number of available processors */
		processors = Runtime.getRuntime().availableProcessors();
		stopWatch = new StopWatch();
	}
	
	@Test
	public void testRegularApproach() {
		System.out.println("Regular Approach");
		FibonacciProblem bigProblem = new FibonacciProblem(n);
		long result = bigProblem.solve();
		stopWatch.stop();

		System.out.println("Result: " + result);
		regularTime = stopWatch.getElapsedTime();
		System.out.println("Elapsed Time: " + stopWatch.getElapsedTime());
	}

	@Test
	public void testForkJoinApproach() {
		System.out.println("Fork Join Approach");
		StopWatch stopWatch = new StopWatch();
		FibonacciProblem bigProblem = new FibonacciProblem(n);

		FibonacciTask task = new FibonacciTask(bigProblem);
		/* Create ForkJoin pool based on number of processors */
		ForkJoinPool pool = new ForkJoinPool(processors);
		pool.invoke(task);
		stopWatch.stop();
		
		System.out.println("Result: " + task.result);
		forkJoinTime = stopWatch.getElapsedTime();
		System.out.println("Elapsed Time: " + stopWatch.getElapsedTime());
	}
	
	@Test
	public void testImprovedRegularApproach() {
		System.out.println("Improved Regular Approach");
		ImprovedFibonacciProblem bigProblem = new ImprovedFibonacciProblem(n);
		long result = bigProblem.solve();
		stopWatch.stop();

		System.out.println("Result: " + result);
		System.out.println("Elapsed Time: " + stopWatch.getElapsedTime());
	}
	
	@AfterClass
	public static void tearDown() {
		System.out.println("Regular: " + regularTime + " Vs. Fork/Join " + forkJoinTime);
	}
}
