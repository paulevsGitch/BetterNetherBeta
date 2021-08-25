package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.TileView;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.types.NetherVines;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

import java.util.Random;

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
		else if (meta == NetherVines.VIRID_VINE.getMeta()) {
			int seed = MHelper.getRandomHash(y, x, z);
			Random random = MHelper.getRandom();
			random.setSeed(seed);
			int type = random.nextInt(3);
			if (type == 0) {
				name += "_2";
			}
			else if (type == 1) {
				name += "_3";
			}
		}
		return TextureListener.getBlockTexture(name);
	}
}
