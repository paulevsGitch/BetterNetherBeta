package paulevs.bnb.block.plant;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.block.tree.BNBLeavesBlock;

public class BNBCeilPlantBlock extends BNBPlantBlock {
	public BNBCeilPlantBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_PLANT, true);
		this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 1.0F, 0.875F);
	}
	
	@Override
	protected boolean canStay(Level level, int x, int y, int z) {
		return isCeil(level.getBlockState(x, y + 1, z));
	}
	
	protected boolean isCeil(BlockState state) {
		Block block = state.getBlock();
		return (block.isFullCube() && block.isFullOpaque()) || block instanceof BNBLeavesBlock;
	}
}
