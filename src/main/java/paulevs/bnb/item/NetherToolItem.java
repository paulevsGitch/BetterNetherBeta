package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.modificationstation.stationloader.api.common.item.tool.ToolLevel;
import paulevs.bnb.item.material.NetherToolMaterial;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.mixin.common.ToolBaseAccessor;

public abstract class NetherToolItem extends NetherItem implements ToolLevel {
	protected final NetherToolMaterial material;
	private final String texture;
	
	public NetherToolItem(String name, int id, NetherToolMaterial material) {
		super(name, id);
		this.texture = name;
		this.material = material;
		this.maxStackSize = 1;
		this.setDurability(material.getDurability());
	}

	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture(texture);
	}
	
	public abstract boolean isEffectiveOn(BlockBase tile);
	
	protected boolean isEffectiveOn(ItemBase item, BlockBase tile) {
		for (BlockBase block: ((ToolBaseAccessor) item).bnb_getEffective()) {
			if (block == tile) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public float getStrengthOnBlock(ItemInstance item, BlockBase tile) {
		if (isEffectiveOn(tile)) {
			return material.getMiningSpeed();
		}
		return 1.0F;
	}
	
	@Override
	public boolean postHit(ItemInstance item, Living damageSource, Living damageTarget) {
		item.applyDamage(2, damageTarget);
		return true;
	}

	@Override
	public boolean postMine(ItemInstance item, int i, int j, int k, int i1, Living damageTarget) {
		item.applyDamage(1, damageTarget);
		return true;
	}

	@Override
	public int method_447(EntityBase entity) {
		return material.getAttackDamage() + 1;
	}

	@Override
	@Environment(EnvType.CLIENT)
	public boolean isRendered3d() {
		return true;
	}
	
	@Override
	public int getToolLevel() {
		return material.getMiningLevel();
	}
}
