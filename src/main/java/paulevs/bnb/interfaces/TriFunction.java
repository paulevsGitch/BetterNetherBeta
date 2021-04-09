package paulevs.bnb.interfaces;

@FunctionalInterface
public interface TriFunction<A, B, C, T> {
	T apply(A var1, B var2, C var3);
}
