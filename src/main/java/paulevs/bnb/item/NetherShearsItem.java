package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherShearsItem extends NetherToolItem {
	public NetherShearsItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}

	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return ItemBase.shears.isEffectiveOn(tile);
	}
	
	@Override
	public boolean postMine(ItemInstance arg, int i, int j, int k, int i1, Living damageTarget) {
		return ItemBase.shears.postMine(arg, i, j, k, i1, damageTarget);
	}

	@Override
	public float getStrengthOnBlock(ItemInstance item, BlockBase tile) {
		return ItemBase.shears.getStrengthOnBlock(item, tile);
	}
}
