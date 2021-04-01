package paulevs.bnb.interfaces;

import net.minecraft.block.BlockBase;

@FunctionalInterface
public interface BlockInit<N, I, T extends BlockBase> {
	T apply(N name, I id);
}
