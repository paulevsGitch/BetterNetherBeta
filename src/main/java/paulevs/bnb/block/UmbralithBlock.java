package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.TileView;
import paulevs.bnb.block.types.UmbralithType;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

public class UmbralithBlock extends NetherStoneBlock implements BlockWithLight {
	public <T extends StoneBlockEnum> UmbralithBlock(String name, int id) {
		super(name, id, UmbralithType.class);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		if (world.getTileMeta(x, y, z) != UmbralithType.UMBRALITH.getMeta()) {
			return super.method_1626(world, x, y, z, side);
		}
		int texture = MHelper.getRandomHash(y, x, z) % 91;
		if (texture < 88) {
			texture &= 3;
			return texture == 0 ? super.method_1626(world, x, y, z, side) : TextureListener.getBlockTexture("umbralith_" + texture);
		}
		texture = texture - 84;
		return TextureListener.getBlockTexture("umbralith_" + texture);
	}
	
	@Override
	public float getEmissionIntensity() {
		return 0.5F;
	}
}
