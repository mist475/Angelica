package speiger.src.collections.objects.functions.function;


/**
 * A Type Specific Function interface that reduces boxing/unboxing and fills the gaps of interfaces that are missing.
 * @param <T> the keyType of elements maintained by this Collection
 */
@FunctionalInterface
public interface ToDoubleFunction<T> extends java.util.function.ToDoubleFunction<T>
{
	/**
	 * Type Specific get function to reduce boxing/unboxing
	 * @param k the value that should be processed
	 * @return the result of the function
	 */
	public double applyAsDouble(T k);
	
}