package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.types.NetherDoublePlantType;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

public class NetherDoublePlant extends NetherPlantBlock implements BlockWithLight {
	public <T extends BlockEnum> NetherDoublePlant(String name, int id) {
		super(name, id, NetherDoublePlantType.class, false);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 1.0F, 0.875F);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = variants[clampMeta(damage)].getTexture(0);
				return TextureListener.getBlockTexture(name + "_top");
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + getVariant(item.getDamage()).getTranslationKey();
			}
		};
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return id == this.id || BlockUtil.isTerrain(id);
	}

	@Override
	public float getEmissionIntensity() {
		return 2F;
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		if (level.getTileId(x, y - 1, z) == this.id) {
			return null;
		}
		return super.getCustomWorldModel(level, x, y, z, meta);
	}
	
	@Override
	public void beforeDestroyedByExplosion(Level level, int x, int y, int z, int meta, float dropChance) {
		if (level.getTileId(x, y - 1, z) != this.id) {
			super.beforeDestroyedByExplosion(level, x, y, z, meta, dropChance);
		}
	}
}
