package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.types.NetherSaplingType;
import paulevs.bnb.interfaces.Bonemealable;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.world.structures.NetherStructures;

import java.util.Random;

public class NetherSaplingBlock extends MultiBlock implements Bonemealable {
	public NetherSaplingBlock(String name, int id) {
		super(name, id, Material.PLANT, NetherSaplingType.class);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.75F, 0.875F);
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
				String name = variants[clampMeta(damage)].getTexture(0);
				return TextureListener.getBlockTexture(name);
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + getVariant(item.getDamage()).getTranslationKey();
			}
		};
	}
	
	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, int meta) {
		if (NetherStructures.EMBER_TREE.generate(level, level.rand, x, y, z)) {
			level.setTile(x, y, z, 0);
			BlockUtil.updateArea(level, x - 15, y - 15, z - 15, x + 15, y + 15, z + 15);
		}
		return true;
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
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
}
