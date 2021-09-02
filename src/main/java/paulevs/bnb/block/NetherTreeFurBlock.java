package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.types.NetherTreeFurType;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.listeners.TextureListener;

public class NetherTreeFurBlock extends NetherCeilPlantBlock implements BlockModelProvider, BlockWithLight {
	public NetherTreeFurBlock(String name, int id) {
		super(name, id, NetherTreeFurType.class);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = variants[clampMeta(damage)].getTexture(0) + "_bottom";
				return TextureListener.getBlockTexture(name);
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + getVariant(item.getDamage()).getTranslationKey();
			}
		};
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		String texture = getVariant(world.getTileMeta(x, y, z)).getName();
		if (world.getTileId(x, y - 1, z) != this.id) {
			texture += "_bottom";
		}
		return TextureListener.getBlockTexture(texture);
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		int id = level.getTileId(x, y + 1, z);
		return this.isCeil(id) || id == this.id;
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		String model = getVariant(meta).getName();
		if (level.getTileId(x, y - 1, z) != this.id) {
			model += "_bottom";
		}
		return ModelListener.getBlockModel(model);
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int meta) {
		return null;
	}

	@Override
	public float getEmissionIntensity() {
		return 2F;
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 6;
	}
}
