package paulevs.bnb.interfaces;

import net.minecraft.item.ItemBase;

@FunctionalInterface
public interface ItemInit<N, I, T extends ItemBase> {
	T apply(N name, I id);
}
