package paulevs.bnb.util;

import it.unimi.dsi.fastutil.longs.LongArrayFIFOQueue;

public class ConcurrentLongQueue {
	private final LongArrayFIFOQueue queue = new LongArrayFIFOQueue();
	
	public synchronized long get() {
		return queue.dequeueLong();
	}
	
	public synchronized void add(long value) {
		queue.enqueue(value);
	}
	
	public synchronized boolean isEmpty() {
		return queue.isEmpty();
	}
}
