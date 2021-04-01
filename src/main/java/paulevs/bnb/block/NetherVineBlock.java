package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.block.types.NetherVines;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

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
	protected void tick(Level level, int x, int y, int z) {
		super.tick(level, x, y, z);
		int id = level.getTileId(x, y, z);
		int meta = level.getTileMeta(x, y, z);
		if (id == this.id) {
			boolean isNotSameDown = level.getTileId(x, y - 1, z) != this.id;
			boolean bottom = isNotSameDown || (level.getTileId(x, y + 1, z) != this.id && isNotSameDown);
			boolean zeroMeta = (meta & 1) == 0;
			if (!zeroMeta && bottom) {
				level.setTileMeta(x, y, z, meta & 0b11111110);
			}
			else if (zeroMeta && !bottom) {
				level.setTileMeta(x, y, z, meta | 1);
			}
		}
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				String name = variants[clampMeta(damage >> 1)].getTexture(0, damage);
				return TextureListener.getBlockTexture(name + "_inventory", name);
			}
		};
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		String name = variants[clampMeta(meta >> 1)].getTexture(side, meta);
		if (BlockUtil.isLightPass()) {
			return TextureListener.getBlockTexture(name + "_light", name);
		}
		else {
			return TextureListener.getBlockTexture(name);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public float method_1604(TileView arg, int i, int j, int k) {
		return BlockUtil.isLightPass() ? 1F : super.method_1604(arg, i, j, k);
	}
	
	@Override
	protected int droppedMeta(int meta) {
		return variants[clampMeta(meta >> 1)].getDropMeta();
	}
}
