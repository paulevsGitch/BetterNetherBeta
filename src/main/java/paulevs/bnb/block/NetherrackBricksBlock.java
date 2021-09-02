package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.level.TileView;
import paulevs.bnb.block.types.NetherrackBricksType;
import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.MHelper;

public class NetherrackBricksBlock extends NetherStoneBlock {
	public <T extends StoneBlockEnum> NetherrackBricksBlock(String name, int id) {
		super(name, id, NetherrackBricksType.class);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		/*int meta = NetherrackBricks.NETHERRACK_BRICKS.getMeta();
		if (world.getTileMeta(x, y, z) == meta) {
			for (BlockDirection dir : BlockDirection.VALUES) {
				if (isSoulTile(world.getTileId(x + dir.getX(), y + dir.getY(), z + dir.getZ()))) {
					return TextureListener.getBlockTexture("netherrack_bricks_soul");
				}
			}
		}
		return super.method_1626(world, x, y, z, side);*/
		if (world.getTileMeta(x, y, z) != NetherrackBricksType.NETHERRACK_BRICKS.getMeta()) {
			return super.method_1626(world, x, y, z, side);
		}
		int texture = MHelper.getRandomHash(y, x, z) & 3;
		return texture == 0 ? super.method_1626(world, x, y, z, side) : TextureListener.getBlockTexture("netherrack_bricks_" + texture);
	}
	
	private boolean isSoulTile(int id) {
		return id == BlockBase.SOUL_SAND.id || id == BlockListener.getBlockID("soul_soil");
	}
}
