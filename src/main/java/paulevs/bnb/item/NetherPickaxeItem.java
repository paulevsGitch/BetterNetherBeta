package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemBase;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherPickaxeItem extends NetherToolItem {
	public NetherPickaxeItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}

	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return ItemBase.ironPickaxe.isEffectiveOn(tile);
	}
}
