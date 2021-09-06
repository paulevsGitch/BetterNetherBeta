package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.item.WallPlantPlaceableTileEntity;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.util.BlockDirection;

import java.util.Random;

public class WallNetherPlantBlock extends SimpleNetherBlock implements BlockItemProvider, BlockModelProvider, BlockWithLight {
	public <T extends BlockEnum> WallNetherPlantBlock(String name, int id) {
		super(name, id, Material.PLANT);
		this.disableNotifyOnMetaDataChange();
		this.sounds(GRASS_SOUNDS);
		this.disableStat();
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		BlockDirection dir = BlockDirection.fromFacing(level.getTileMeta(x, y, z));
		return this.isWall(level.getTileId(x + dir.getX(), y, z + dir.getZ()));
	}
	
	public boolean isWall(int id) {
		return BlockBase.FULL_OPAQUE[id];
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
		BlockDirection dir = BlockDirection.fromFacing(level.getTileMeta(x, y, z));
		return this.isWall(level.getTileId(x + dir.getX(), y, z + dir.getZ()));
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
	public float getEmissionIntensity() {
		return 2;
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return null;
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		return ModelListener.getBlockModel(getName() + "_" + (level.getTileMeta(x, y, z) - 1));
	}
	
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		int meta = world.getTileMeta(x, y, z);
		BlockDirection dir = BlockDirection.fromFacing(meta);
		float x1 = dir.getX() < 1 ? 0.0F : 0.25F;
		float z1 = dir.getZ() < 1 ? 0.0F : 0.25F;
		float x2 = dir.getX() > -1 ? 1.0F : 0.75F;
		float z2 = dir.getZ() > -1 ? 1.0F : 0.75F;
		this.setBoundingBox(x1, 0.125F, z1, x2, 0.875F, z2);
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int id) {
		return new WallPlantPlaceableTileEntity(id, this);
	}
}
