package speiger.src.collections.floats.functions.function;

import java.util.Objects;

/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 */
@FunctionalInterface
public interface FloatPredicate
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public boolean test(float k);
	
	/**
	 * Creates a Always true function that may be useful if you don't need to process information or just want a default.
	 * @return a default returning function
	 */
	public static FloatPredicate alwaysTrue() {
		return T -> true;
	}
	
	/**
	 * Creates a Always false function that may be useful if you don't need to process information or just want a default.
	 * @return a default returning function
	 */
	public static FloatPredicate alwaysFalse() {
		return T -> false;
	}
	
	/**
	 * A Type specific and-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a and comparason
	 */
	public default FloatPredicate andType(FloatPredicate other) {
		Objects.requireNonNull(other);
		return T -> test(T) && other.test(T);
	}
	
	/**
	 * A type specific inverter function
	 * @return the same function but inverts the result
	 */
	public default FloatPredicate negate() {
		return T -> !test(T);
	}
	
	/**
	 * A Type specific or-function helper function that reduces boxing/unboxing
	 * @param other the other function that should be merged with.
	 * @return a function that compares values in a or comparason
	 */
	public default FloatPredicate orType(FloatPredicate other) {
		Objects.requireNonNull(other);
		return T -> test(T) || other.test(T);
	}
	
}