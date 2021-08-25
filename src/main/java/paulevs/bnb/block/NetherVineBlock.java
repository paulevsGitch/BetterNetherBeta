package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.TileView;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.block.types.NetherVines;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;

public class NetherVineBlock extends NetherCeilPlantBlock implements BlockWithLight {
	public NetherVineBlock(String name, int id) {
		super(name, id, NetherVines.class);
		this.disableStat();
		this.disableNotifyOnMetaDataChange();
	}
	
	@Override
	protected boolean isCeil(int id) {
		return id == this.id || super.isCeil(id);
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = variants[clampMeta(damage)].getTexture(0);
				return TextureListener.getBlockTexture(name);
			}
		};
	}

	@Override
	public float getEmissionIntensity() {
		return 2F;
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		int meta = world.getTileMeta(x, y, z);
		String name = variants[clampMeta(meta)].getTexture(side);
		if (world.getTileId(x, y - 1, z) != id) {
			name += "_bottom";
		}
		return TextureListener.getBlockTexture(name);
	}
}
