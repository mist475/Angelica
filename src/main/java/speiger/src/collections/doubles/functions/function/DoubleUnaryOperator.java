package speiger.src.collections.doubles.functions.function;

import java.util.Objects;

/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 */
@FunctionalInterface
public interface DoubleUnaryOperator extends java.util.function.DoubleUnaryOperator
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public double applyAsDouble(double k);
	
	/**
	 * Creates a Default function that returns the input provided.
	 * @return a input returning function
	 */
	public static DoubleUnaryOperator identity() {
		return T -> T;
	}
	
	/**
	 * Returns a composed function that first applies the {@code before}
     * function to its input, and then applies this function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     * 
     * @param before the function that should be used first
     * @return a composed function with a different starting function.
	 */
	public default DoubleUnaryOperator compose(DoubleUnaryOperator before) {
        Objects.requireNonNull(before);
		return T -> applyAsDouble(before.applyAsDouble(T));
	}
	
	/**
     * Returns a composed function that first applies this function to
     * its input, and then applies the {@code after} function to the result.
     * If evaluation of either function throws an exception, it is relayed to
     * the caller of the composed function.
     * 
     * @param after the function that should be used last
     * @return a composed function with a different starting function.
	 */
	public default DoubleUnaryOperator andThen(DoubleUnaryOperator after) {
        Objects.requireNonNull(after);
		return T -> after.applyAsDouble(applyAsDouble(T));
	}
	
}