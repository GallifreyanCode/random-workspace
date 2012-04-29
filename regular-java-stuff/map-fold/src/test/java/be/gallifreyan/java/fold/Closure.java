package be.gallifreyan.java.fold;
/**
 * Closure interface, similar as in commons-collections.
 * Command pattern style. *
 */
public interface Closure<T> {
	T execute(T value);
}
