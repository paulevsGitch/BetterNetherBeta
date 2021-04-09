package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherShovelItem extends NetherToolItem {
	public NetherShovelItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}

	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return ItemBase.ironShovel.isEffectiveOn(tile);
	}
}
