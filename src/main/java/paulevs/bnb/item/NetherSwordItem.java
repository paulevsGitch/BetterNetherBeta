package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherSwordItem extends NetherToolItem {
	public NetherSwordItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}

	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return ItemBase.ironSword.isEffectiveOn(tile);
	}
	
	@Override
	public boolean postHit(ItemInstance arg, Living damageSource, Living damageTarget) {
		arg.applyDamage(1, damageTarget);
		return true;
	}

	@Override
	public boolean postMine(ItemInstance item, int i, int j, int k, int i1, Living damageTarget) {
		item.applyDamage(2, damageTarget);
		return true;
	}
	
	@Override
	public float getStrengthOnBlock(ItemInstance item, BlockBase tile) {
		return ItemBase.ironSword.getStrengthOnBlock(item, tile);
	}
	
	@Override
	public int method_447(EntityBase entity) {
		return material.getAttackDamage() * 2 + 4;
	}
}
