package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherShovelItem extends NetherToolItem {
	public NetherShovelItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}
	
	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return isEffectiveOn(ironShovel, tile);
	}
}
