package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.block.types.NetherFungus;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

public class NetherFungusBlock extends NetherPlantBlock implements BlockModelProvider {
	public NetherFungusBlock(String registryName, int id) {
		super(registryName, id, NetherFungus.class);
	}

	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return null;
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = variants[clampMeta(damage)].getTexture(0) + "_item";
				return TextureListener.getBlockTexture(name);
			}
		};
	}

	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		int state = MHelper.getRandomHash(y, x, z) & 3;
		String model = this.getVariant(meta).getName();
		return ModelListener.getBlockModel(model + "_" + state);
	}

	@Override
	public float getEmissionIntensity() {
		return 3F;
	}
}
