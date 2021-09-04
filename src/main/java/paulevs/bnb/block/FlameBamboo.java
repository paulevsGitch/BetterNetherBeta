package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableBlock;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.Vec3i;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.types.FlameBambooType;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.interfaces.Climmable;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class FlameBamboo extends MultiBlock implements BlockModelProvider, BlockWithLight, Climmable {
	public <T extends BlockEnum> FlameBamboo(String name, int id) {
		super(name, id, Material.WOOD, FlameBambooType.class);
		this.disableNotifyOnMetaDataChange();
		this.setHardness(WOOD.getHardness() * 0.3F);
		this.setTicksRandomly(true);
		this.sounds(WOOD_SOUNDS);
		this.disableStat();
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = damage == FlameBambooType.SAPLING.getMeta() ? "flame_bamboo_sapling" : "flame_bamboo_stem_item";
				return TextureListener.getBlockTexture(name);
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + getVariant(item.getDamage()).getTranslationKey();
			}
			
			@Override
			public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
				if (item.count > 0 && item.getDamage() == FlameBambooType.UNNATURAL_STEM.getMeta() || item.getDamage() == FlameBambooType.LADDER.getMeta()) {
					BlockDirection dir = BlockDirection.fromFacing(facing);
					Vec3i pos = dir.offset(new Vec3i(x, y, z));
					System.out.println(pos);
					if (BlockBase.STONE.canPlaceAt(level, pos.x, pos.y, pos.z)) {
						FlameBamboo block = FlameBamboo.this;
						block.onBlockPlaced(level, pos.x, pos.y, pos.z, facing);
						level.setTile(pos.x, pos.y, pos.z, block.id);
						level.setTileMeta(pos.x, pos.y, pos.z, item.getDamage());
						block.afterPlaced(level, pos.x, pos.y, pos.z, player);
						level.playSound(pos.x + 0.5, pos.y + 0.5, pos.z + 0.5, block.sounds.getWalkSound(), (block.sounds.getVolume() + 1.0F) / 2.0F, block.sounds.getPitch() * 0.8F);
						item.count--;
					}
					return true;
				}
				return super.useOnTile(item, player, level, x, y, z, facing);
			}
		};
	}
	
	@Environment(EnvType.CLIENT)
	public void method_1605() {
		this.setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
	}
	
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		int meta = world.getTileMeta(x, y, z);
		if (meta == FlameBambooType.SAPLING.getMeta() || meta == FlameBambooType.TOP.getMeta() || meta == FlameBambooType.TOP_INACTIVE.getMeta()) {
			this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
		}
		else {
			this.setBoundingBox(0.375F, 0.0F, 0.375F, 0.625F, 1.0F, 0.625F);
		}
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		int meta = level.getTileMeta(x, y, z);
		if (meta == FlameBambooType.SAPLING.getMeta() || meta == FlameBambooType.TOP.getMeta() || meta == FlameBambooType.TOP_INACTIVE.getMeta()) {
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
		int meta = level.getTileMeta(x, y, z);
		if (meta != FlameBambooType.SAPLING.getMeta() && meta != FlameBambooType.LADDER.getMeta()) {
			level.setTileMeta(x, y, z, FlameBambooType.UNNATURAL_STEM.getMeta());
			BlockUtil.updateTile(level, x, y, z);
		}
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		if (meta == FlameBambooType.STEM.getMeta() && level.getTileId(x, y - 1, z) == this.id) {
			int type = MHelper.getRandomHash(y, x, z) % 12;
			if (type < 4) {
				return ModelListener.getBlockModel("flame_bamboo_stem_2_" + type);
			}
			else if (type < 8) {
				return ModelListener.getBlockModel("flame_bamboo_stem_1");
			}
			else {
				return ModelListener.getBlockModel("flame_bamboo_stem");
			}
		}
		String name = this.getVariant(meta).getName();
		return ModelListener.getBlockModel(name);
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int meta) {
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
			level.playSound(x + 0.5, y + 0.5, z + 0.5, this.sounds.getBreakSound(), (this.sounds.getVolume() + 1.0F) / 2.0F, this.sounds.getPitch() * 0.8F);
			level.setTile(x, y, z, 0);
			drop(level, x, y, z, meta);
		}
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		int meta = level.getTileMeta(x, y, z);
		if (meta == FlameBambooType.LADDER.getMeta() || meta == FlameBambooType.UNNATURAL_STEM.getMeta()) {
			return true;
		}
		int id = level.getTileId(x, y - 1, z);
		if (meta == FlameBambooType.SAPLING.getMeta()) {
			return BlockUtil.isTerrain(id);
		}
		return id == this.id || BlockUtil.isTerrain(id);
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
	}
	
	private void tick(Level level, int x, int y, int z, Random rand) {
		int meta = level.getTileMeta(x, y, z);
		if (meta != FlameBambooType.TOP.getMeta() && meta != FlameBambooType.SAPLING.getMeta()) {
			return;
		}
		if (level.getTileId(x, y + 1, z) != 0) {
			return;
		}
		if (meta == FlameBambooType.SAPLING.getMeta()) {
			level.setTile(x, y + 1, z, this.id);
			level.setTileMeta(x, y + 1, z, FlameBambooType.TOP.getMeta());
			level.setTileMeta(x, y, z, FlameBambooType.MIDDLE.getMeta());
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
			level.setTileMeta(x, y, z, FlameBambooType.TOP_INACTIVE.getMeta());
			BlockUtil.updateTile(level, x, y, z);
			return;
		}
		level.setTile(x, y + 1, z, this.id);
		level.setTileMeta(x, y + 1, z, FlameBambooType.TOP.getMeta());
		level.setTileMeta(x, y, z, FlameBambooType.MIDDLE.getMeta());
		level.setTileMeta(x, y - 1, z, FlameBambooType.STEM.getMeta());
		BlockUtil.updateArea(level, x, y - 1, z, x, y + 1, z);
	}
	
	@Override
	public boolean canClimb(int meta) {
		return meta == FlameBambooType.LADDER.getMeta();
	}
}
