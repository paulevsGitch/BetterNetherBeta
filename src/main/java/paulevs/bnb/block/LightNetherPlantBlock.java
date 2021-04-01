package paulevs.bnb.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

public class LightNetherPlantBlock extends BlockBase implements BlockItemProvider, BlockWithLight {
	private final String name;
	
	public LightNetherPlantBlock(String name, int id) {
		super(id, Material.PLANT);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.75F, 0.875F);
		this.setName(BetterNetherBeta.getID(name));
		this.sounds(GRASS_SOUNDS);
		//this.setLightEmittance(0.4F);
		this.name = name;
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntity(i) {
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				return TextureListener.getBlockTexture(name + "_inventory");
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
		return BlockUtil.isLightPass() ? TextureListener.getBlockTexture(name + "_light") : TextureListener.getBlockTexture(name);
	}
	
	@Environment(EnvType.CLIENT)
	public float method_1604(TileView arg, int i, int j, int k) {
		return BlockUtil.isLightPass() ? 1F : super.method_1604(arg, i, j, k);
	}
}
