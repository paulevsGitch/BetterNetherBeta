package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherStump;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

public class NetherStumpBlock extends MultiBlock implements BlockWithLight, BlockModelProvider {
	public NetherStumpBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherStump.class);
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

	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		int state = MHelper.getRandomHash(y, x, z) & 3;
		String model = this.getVariant(meta).getName();
		return ModelListener.getBlockModel(model + "_" + state);
	}

	@Override
	public CustomModel getCustomInventoryModel(int meta) {
		return ModelListener.getBlockModel(getVariant(meta).getName() + "_0");
	}
}
