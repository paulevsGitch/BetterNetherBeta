package paulevs.bnb.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

public class NetherPlantBlock extends MultiBlock implements BlockWithLight {
	public <T extends BlockEnum> NetherPlantBlock(String name, int id, Class<T> type) {
		super(name, id, Material.PLANT, type);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.75F, 0.875F);
		this.sounds(GRASS_SOUNDS);
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = variants[clampMeta(damage)].getTexture(0);
				return TextureListener.getBlockTexture(name + "_inventory", name);//getTextureForSide(0, damage);
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
		this.tick(level, x, y, z);
	}

	protected void tick(Level level, int x, int y, int z) {
		if (!this.canGrow(level, x, y, z)) {
			this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			level.setTile(x, y, z, 0);
		}
	}

	@Override
	public boolean canGrow(Level arg, int x, int y, int z) {
		return (arg.isAboveGroundCached(x, y, z)) && this.canPlantOnTopOf(arg.getTileId(x, y - 1, z));
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
	
	@Override
	public int getTextureForSide(int side, int meta) {
		String name = variants[clampMeta(meta)].getTexture(side);
		if (BlockUtil.isLightPass()) {
			return TextureListener.getBlockTexture(name + "_light", name);
		}
		else {
			return TextureListener.getBlockTexture(name);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public float method_1604(TileView arg, int i, int j, int k) {
		return BlockUtil.isLightPass() ? 1F : super.method_1604(arg, i, j, k);
	}
}
