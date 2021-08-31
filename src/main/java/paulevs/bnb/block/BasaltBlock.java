package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Vec3i;
import org.lwjgl.util.Point;
import paulevs.bnb.block.types.BasaltBlockType;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.interfaces.Bonemealable;
import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

public class BasaltBlock extends NetherStoneBlock implements BlockWithLight, Bonemealable {
	public <T extends StoneBlockEnum> BasaltBlock(String name, int id) {
		super(name, id, BasaltBlockType.class);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		if (world.getTileMeta(x, y, z) != BasaltBlockType.BASALT_BRICKS.getMeta()) {
			return super.method_1626(world, x, y, z, side);
		}
		int texture = MHelper.getRandomHash(y, x, z) & 1;
		return texture == 0 ? super.method_1626(world, x, y, z, side) : TextureListener.getBlockTexture("basalt_bricks_" + texture);
	}
	
	@Override
	public float getEmissionIntensity() {
		return 2;
	}
	
	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, int meta) {
		if (level.getTileMeta(x, y, z) == BasaltBlockType.BASALT.getMeta()) {
			Vec3i[] offsets = MHelper.getOffsets(level.rand);
			for (Vec3i offset: offsets) {
				int px = x + offset.x;
				int py = y + offset.y;
				int pz = z + offset.z;
				if (level.getTileId(px, py, pz) == this.id && level.getTileMeta(px, py, pz) == BasaltBlockType.FLAMING_BASALT.getMeta()) {
					level.setTileMeta(x, y, z, BasaltBlockType.FLAMING_BASALT.getMeta());
					BlockUtil.updateTile(level, x, y, z);
					return true;
				}
			}
		}
		return false;
	}
}
