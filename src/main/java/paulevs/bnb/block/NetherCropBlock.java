package paulevs.bnb.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.Bonemealable;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

public class NetherCropBlock extends NetherBlock implements BlockItemProvider, Bonemealable {
	private final String name;
	private final int maxAge;
	
	public <T extends BlockEnum> NetherCropBlock(String name, int id, int maxAge) {
		super(name, id, Material.PLANT);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.75F, 0.875F);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
		this.setTicksRandomly(true);
		this.sounds(GRASS_SOUNDS);
		this.maxAge = maxAge;
		this.name = name;
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				return getTextureForSide(0, maxAge);
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + name;
			}
		};
	}

	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return super.canPlaceAt(level, x, y, z) && this.canPlantOnTopOf(level.getTileId(x, y - 1, z));
	}

	protected boolean canPlantOnTopOf(int id) {
		return BlockUtil.isTerrain(id);
	}

	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		this.tick(level, x, y, z);
	}

	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		if (this.tick(level, x, y, z) && rand.nextInt(8) == 0) {
			int age = level.getTileMeta(x, y, z);
			if (age >= maxAge) {
				return;
			}
			level.setTileMeta(x, y, z, age + 1);
		}
	}

	protected boolean tick(Level level, int x, int y, int z) {
		if (!this.canGrow(level, x, y, z)) {
			this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			level.setTile(x, y, z, 0);
			return false;
		}
		return true;
	}

	@Override
	public boolean canGrow(Level level, int x, int y, int z) {
		return this.canPlantOnTopOf(level.getTileId(x, y - 1, z));
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
		return 6;
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return TextureListener.getBlockTexture(name + "_" + (meta % (maxAge + 1)));
	}

	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, int meta) {
		if (meta < maxAge) {
			int age = meta + level.rand.nextInt(2) + 1;
			if (age > maxAge) {
				age = maxAge;
			}
			level.setTileMeta(x, y, z, age);
			return true;
		}
		return false;
	}
}
