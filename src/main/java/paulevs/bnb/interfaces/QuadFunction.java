package paulevs.bnb.interfaces;

@FunctionalInterface
public interface QuadFunction<A, B, C, D, T> {
	T apply(A var1, B var2, C var3, D var4);
}
