package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.tool.ShearsItem;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.vbe.utils.CreativeUtil;

import java.util.Random;

public abstract class BNBPlantBlock extends TemplateBlock {
	public BNBPlantBlock(Identifier id, Material material) {
		super(id, material);
		NO_AMBIENT_OCCLUSION[this.id] = true;
		disableNotifyOnMetaDataChange();
		setSounds(GRASS_SOUNDS);
		disableStat();
		setLightOpacity(0);
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.getMaterial() == Material.LAVA || !state.getMaterial().isReplaceable()) return false;
		return canStay(level, x, y, z);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		tick(level, x, y, z);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		tick(level, x, y, z);
	}
	
	@Override
	public boolean canGrow(Level level, int x, int y, int z) {
		return canStay(level, x, y, z);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) {
			super.afterBreak(level, player, x, y, z, meta);
			return;
		}
		ItemStack heldItem = player.getHeldItem();
		if (heldItem == null || !(heldItem.getType() instanceof ShearsItem)) return;
		drop(level, x, y, z, new ItemStack(this));
		if (!CreativeUtil.isCreative(player)) heldItem.applyDamage(1, player);
	}
	
	protected abstract boolean canStay(Level level, int x, int y, int z);
	
	protected void tick(Level level, int x, int y, int z) {
		if (!this.canStay(level, x, y, z)) {
			this.drop(level, x, y, z, 0);
			level.setBlockState(x, y, z, States.AIR.get());
		}
	}
}
