package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherWoodType;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

public class NetherWoodBlock extends MultiBlock implements BlockWithLight {
	public NetherWoodBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherWoodType.class);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
	}

	@Override
	public float getEmissionIntensity() {
		return 2F;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		int meta = world.getTileMeta(x, y, z);
		if (meta == NetherWoodType.EMBER_WOOD.getMeta()) {
			if (BlockUtil.isHorizontalSide(side)) {
				String name = getVariant(meta).getTexture(side);
				int type = MHelper.getRandomHash(y, x, z) % 3;
				String texture = type > 0 ? name + "_" + type : name;
				if (hasMagma(world, x, y, z)) {
					texture += "_magma";
				}
				else if (world.getTileId(x, y - 1, z) == id) {
					if (hasMagma(world, x, y - 1, z)) {
						texture += "_magma";
					}
					else if (world.getTileId(x, y - 2, z) == id && hasMagma(world, x, y - 2, z)) {
						texture += "_magma";
					}
				}
				return TextureListener.getBlockTexture(texture);
			}
		}
		if (BlockUtil.isHorizontalSide(side)) {
			int offset = MathHelper.floor(MHelper.getNoiseValue(x * 0.5, z * 0.5) * 3);
			int state = MHelper.abs((offset + y) % 5 - 2);
			String name = getVariant(meta).getTexture(side);
			return TextureListener.getBlockTexture(name + "_" + state, name);
		}
		return super.method_1626(world, x, y, z, side);
	}
	
	private boolean hasMagma(TileView world, int x, int y, int z) {
		for (BlockDirection dir: BlockDirection.VALUES) {
			int id = world.getTileId(x + dir.getX(), y + dir.getY(), z + dir.getZ());
			if (id == STILL_LAVA.id || id == STILL_LAVA.id) {
				return true;
			}
		}
		return false;
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		String name = variants[clampMeta(meta)].getTexture(side);
		String getName = name;
		if (side > 1) {
			getName += "_1";
		}
		return TextureListener.getBlockTexture(getName, name);
	}
}
