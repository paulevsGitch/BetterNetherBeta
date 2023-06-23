package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;

public class NetherFloorPlantBlock extends NetherPlantBlock {
	public NetherFloorPlantBlock(Identifier id) {
		super(id, Material.PLANT);
		this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.875F, 0.875F, 0.875F);
	}
	
	@Override
	protected boolean canStay(Level level, int x, int y, int z) {
		return isGround(level.getBlockState(x, y - 1, z));
	}
	
	protected boolean isGround(BlockState state) {
		BlockBase block = state.getBlock();
		return block.isFullCube() && block.isFullOpaque();
	}
}
