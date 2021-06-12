package paulevs.bnb.block;

import java.util.Random;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import paulevs.bnb.interfaces.Bonemealable;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.world.structures.NetherStructures;

public class SoulSpirePlantBlock extends NetherBlock implements BlockItemProvider, Bonemealable {
	private final String texture;
	
	public SoulSpirePlantBlock(String name, int id) {
		super(name, id, Material.PLANT);
		this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
		this.setLightEmittance(1F);
		this.texture = name + "_";
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
		this.sounds(GRASS_SOUNDS);
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntity(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				return TextureListener.getBlockTexture(texture + "0");
			}
		};
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		int above = count(world, x, y, z, BlockDirection.POS_Y);
		int below = count(world, x, y, z, BlockDirection.NEG_Y);
		int index = 0;
		if (above == 0) {
			index = below > 1 ? 3 : below == 1 ? 1 : 0;
		}
		else if (above == 1) {
			index = below > 1 ? 4 : below == 1 ? 5 : 2;
		}
		else if (above > 1) {
			index = below > 0 ? 5 : 6;
		}
		return TextureListener.getBlockTexture(texture + index);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return TextureListener.getBlockTexture(texture + "0");
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		int ground = level.getTileId(x, y - 1, z);
		if (BlockUtil.isSoulTerrain(ground)) {
			return super.canPlaceAt(level, x, y, z);
		}
		if (ground == this.id) {
			return count(level, x, y, z, BlockDirection.NEG_Y) < 4 && super.canPlaceAt(level, x, y, z);
		}
		return false;
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
	
	private int count(TileView world, int x, int y, int z, BlockDirection dir) {
		int count = 0;
		for (int i = 0; i < 4; i++) {
			y += dir.getY();
			int id = world.getTileId(x, y, z);
			if (id == this.id) {
				count++;
			}
		}
		return count;
	}

	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, int meta) {
		if (level.getTileId(x, y + 1, z) == 0 && level.getTileId(x, y - 1, z) != id) {
			level.setTile(x, y, z, 0);
			NetherStructures.SOUL_SPIRE.generate(level, level.rand, x, y, z);
			return true;
		}
		return false;
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
		int id = level.getTileId(x, y - 1, z);
		if (id != this.id && !BlockUtil.isSoulTerrain(id)) {
			this.drop(level, x, y, z, 0);
			level.setTile(x, y, z, 0);
		}
	}
}
