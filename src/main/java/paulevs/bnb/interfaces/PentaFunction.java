package paulevs.bnb.interfaces;

@FunctionalInterface
public interface PentaFunction<A, B, C, D, E, T> {
	T apply(A var1, B var2, C var3, D var4, E var5);
}
