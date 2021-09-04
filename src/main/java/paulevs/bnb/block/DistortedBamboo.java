package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.block.types.DistortedBambooType;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class DistortedBamboo extends MultiBlock implements BlockModelProvider, BlockWithLight {
	public <T extends BlockEnum> DistortedBamboo(String name, int id) {
		super(name, id, Material.WOOD, DistortedBambooType.class);
		this.disableNotifyOnMetaDataChange();
		this.setHardness(WOOD.getHardness() * 0.3F);
		this.setTicksRandomly(true);
		this.sounds(WOOD_SOUNDS);
		this.disableStat();
	}
	
	@Environment(EnvType.CLIENT)
	public void method_1605() {
		this.setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
	}
	
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		int meta = world.getTileMeta(x, y, z);
		if (meta == DistortedBambooType.SAPLING.getMeta() || meta == DistortedBambooType.TOP.getMeta() || meta == DistortedBambooType.TOP_INACTIVE.getMeta()) {
			this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
		}
		else {
			this.setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
		}
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		int meta = level.getTileMeta(x, y, z);
		if (meta == DistortedBambooType.SAPLING.getMeta() || meta == DistortedBambooType.TOP.getMeta() || meta == DistortedBambooType.TOP_INACTIVE.getMeta()) {
			return null;
		}
		this.method_1616(level, x, y, z);
		return super.getCollisionShape(level, x, y, z);
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
	public void onBlockPlaced(Level level, int x, int y, int z) {
		if (level.getTileMeta(x, y, z) != DistortedBambooType.SAPLING.getMeta()) {
			level.setTileMeta(x, y, z, DistortedBambooType.UNNATURAL_STEM.getMeta());
			BlockUtil.updateTile(level, x, y, z);
		}
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		if (meta == DistortedBambooType.STEM.getMeta() && level.getTileId(x, y - 1, z) == this.id) {
			int type = MHelper.getRandomHash(y, x, z) % 12;
			if (type < 4) {
				return ModelListener.getBlockModel("distorted_bamboo_stem_2_" + type);
			}
			else if (type < 8) {
				return ModelListener.getBlockModel("distorted_bamboo_stem_1");
			}
			else {
				return ModelListener.getBlockModel("distorted_bamboo_stem");
			}
		}
		String name = this.getVariant(meta).getName();
		return ModelListener.getBlockModel(name);
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return null;
	}
	
	@Override
	public float getEmissionIntensity() {
		return 3;
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		tick(level, x, y, z, rand);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int meta) {
		super.onAdjacentBlockUpdate(level, x, y, z, meta);
		if (!canPlaceAt(level, x, y, z)) {
			drop(level, x, y, z, meta);
			level.setTile(x, y, z, 0);
		}
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		int id = level.getTileId(x, y - 1, z);
		if (level.getTileMeta(x, y, z) == DistortedBambooType.SAPLING.getMeta()) {
			return BlockUtil.isTerrain(id);
		}
		return id != this.id && !BlockUtil.isTerrain(id);
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 2;
	}
	
	private void tick(Level level, int x, int y, int z, Random rand) {
		int meta = level.getTileMeta(x, y, z);
		if (meta != DistortedBambooType.TOP.getMeta() && meta != DistortedBambooType.SAPLING.getMeta()) {
			return;
		}
		if (level.getTileId(x, y + 1, z) != 0) {
			return;
		}
		if (meta == DistortedBambooType.SAPLING.getMeta()) {
			level.setTile(x, y + 1, z, this.id);
			level.setTileMeta(x, y + 1, z, DistortedBambooType.TOP.getMeta());
			level.setTileMeta(x, y, z, DistortedBambooType.MIDDLE.getMeta());
			BlockUtil.updateArea(level, x, y, z, x, y + 1, z);
			return;
		}
		int count = 0;
		for (int i = 1; i < 12; i++) {
			count++;
			if (level.getTileId(x, y - i, z) != this.id) {
				break;
			}
		}
		if (count > 12) {
			return;
		}
		if (count == 12 || count > 7 && rand.nextBoolean()) {
			level.setTileMeta(x, y, z, DistortedBambooType.TOP_INACTIVE.getMeta());
			BlockUtil.updateTile(level, x, y, z);
			return;
		}
		level.setTile(x, y + 1, z, this.id);
		level.setTileMeta(x, y + 1, z, DistortedBambooType.TOP.getMeta());
		level.setTileMeta(x, y, z, DistortedBambooType.MIDDLE.getMeta());
		level.setTileMeta(x, y - 1, z, DistortedBambooType.STEM.getMeta());
		BlockUtil.updateArea(level, x, y - 1, z, x, y + 1, z);
	}
}
