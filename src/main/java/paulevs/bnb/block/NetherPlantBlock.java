package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.item.tool.Shears;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.item.NetherShearsItem;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.ItemUtil;

import java.util.Random;

public class NetherPlantBlock extends MultiBlock implements BlockModelProvider, BlockWithLight {
	private final boolean useShears;
	
	public <T extends BlockEnum> NetherPlantBlock(String name, int id, Class<T> type, boolean useShears) {
		super(name, id, Material.PLANT, type);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.75F, 0.875F);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
		this.sounds(GRASS_SOUNDS);
		this.useShears = useShears;
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
			if (!useShears) {
				this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			}
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

	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
	}

	@Override
	public float getEmissionIntensity() {
		return 1;
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return null;
	}

	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		return ModelListener.getBlockModel(getVariant(meta).getName());
	}
	
	@Override
	public void afterBreak(Level level, PlayerBase player, int x, int y, int z, int meta) {
		if (useShears) {
			ItemBase item = player.getHeldItem() == null ? null : ItemUtil.itemByID(player.getHeldItem().itemId);
			if (!level.isClient && item != null && (item instanceof Shears || item instanceof NetherShearsItem)) {
				this.drop(level, x, y, z, meta);
			}
		}
		else {
			super.afterBreak(level, player, x, y, z, meta);
		}
	}
}
