package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.BlockEnum;

import java.util.Random;

public class NetherCeilPlantBlock extends MultiBlock implements BlockItemProvider {
	public <T extends BlockEnum> NetherCeilPlantBlock(String name, int id, Class<T> type) {
		super(name, id, Material.PLANT, type);
		this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 1.0F, 0.875F);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
		this.sounds(GRASS_SOUNDS);
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				return getTextureForSide(0, damage);
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + getVariant(item.getDamage()).getTranslationKey();
			}
		};
	}

	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return this.isCeil(level.getTileId(x, y + 1, z));
	}

	protected boolean isCeil(int id) {
		BlockBase block = BlockBase.BY_ID[id];
		return block != null && ((block.isFullCube() && block.isFullOpaque()) || block instanceof NetherLeavesBlock);
	}

	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		this.tick(level, x, y, z);
	}

	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		this.tick(level, x, y, z);
	}

	protected void tick(Level level, int x, int y, int z) {
		if (!this.canPlaceAt(level, x, y, z)) {
			this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			level.setTile(x, y, z, 0);
		}
	}

	@Override
	public boolean canGrow(Level level, int x, int y, int z) {
		return canPlaceAt(level, x, y, z);
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

	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
	}
}
