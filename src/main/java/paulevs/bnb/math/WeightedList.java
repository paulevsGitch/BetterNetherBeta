package paulevs.bnb.math;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeightedList<T> {
	private final List<Float> weights = new ArrayList<>();
	private final List<T> values = new ArrayList<>();
	private float maxWeight;
	
	public void add(T value, float weight) {
		maxWeight += weight;
		weights.add(maxWeight);
		values.add(value);
	}
	
	public T get(Random random) {
		if (maxWeight < 1) {
			return null;
		}
		float weight = random.nextFloat() * maxWeight;
		for (int i = 0; i < weights.size(); i++) {
			if (weight <= weights.get(i)) {
				return values.get(i);
			}
		}
		return null;
	}
}
