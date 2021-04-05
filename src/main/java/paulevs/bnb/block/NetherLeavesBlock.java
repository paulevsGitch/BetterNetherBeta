package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.types.NetherLeaves;

public class NetherLeavesBlock extends MultiBlock/* implements BlockModelProvider*/ {
	public NetherLeavesBlock(String name, int id) {
		super(name, id, Material.LEAVES, NetherLeaves.class);
		this.setHardness(LEAVES.getHardness());
		this.sounds(GRASS_SOUNDS);
	}
	
	/*@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		boolean render = !BlockUtil.isOpaque(level.getTileId(x - 1, y, z));
		render |= !BlockUtil.isOpaque(level.getTileId(x + 1, y, z));
		render |= !BlockUtil.isOpaque(level.getTileId(x, y - 1, z));
		render |= !BlockUtil.isOpaque(level.getTileId(x, y + 1, z));
		render |= !BlockUtil.isOpaque(level.getTileId(x, y, z - 1));
		render |= !BlockUtil.isOpaque(level.getTileId(x, y, z + 1));
		return render ? ModelListener.getBlockModel(getVariant(meta).getName()) : null;
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int meta) {
		return null;
	}*/
}
