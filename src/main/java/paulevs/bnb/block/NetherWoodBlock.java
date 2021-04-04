package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherWood;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

public class NetherWoodBlock extends MultiBlock implements BlockWithLight {
	public NetherWoodBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherWood.class);
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
		if (side > 1) {
			int offset = MathHelper.floor(MHelper.getNoiseValue(x * 0.5, z * 0.5) * 3);
			int state = MHelper.abs((offset + y) % 5 - 2);
			String name = getVariant(world.getTileMeta(x, y, z)).getTexture(side);
			return TextureListener.getBlockTexture(name + "_" + state, name);
		}
		return super.method_1626(world, x, y, z, side);
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
