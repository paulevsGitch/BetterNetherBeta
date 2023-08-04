package paulevs.bnb.block;

import net.minecraft.block.BaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.block.properties.BNBBlockMaterials;

public class NetherCeilPlantBlock extends NetherPlantBlock {
	public NetherCeilPlantBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_PLANT);
		this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 1.0F, 0.875F);
	}
	
	@Override
	protected boolean canStay(Level level, int x, int y, int z) {
		return isCeil(level.getBlockState(x, y + 1, z));
	}
	
	protected boolean isCeil(BlockState state) {
		BaseBlock block = state.getBlock();
		return (block.isFullCube() && block.isFullOpaque()) || block instanceof NetherLeavesBlock;
	}
}
