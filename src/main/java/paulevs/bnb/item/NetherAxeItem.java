package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherAxeItem extends NetherToolItem {
	public NetherAxeItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}

	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return isEffectiveOn(ironAxe, tile);
	}
}
