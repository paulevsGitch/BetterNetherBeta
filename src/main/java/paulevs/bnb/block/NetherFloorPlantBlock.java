package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.block.properties.BNBBlockMaterials;

public class NetherFloorPlantBlock extends NetherPlantBlock {
	public NetherFloorPlantBlock(Identifier id) {
		this(id, BNBBlockMaterials.NETHER_PLANT_REPLACEABLE);
	}
	
	public NetherFloorPlantBlock(Identifier id, Material material) {
		super(id, material);
		this.setBoundingBox(0.125F, 0.0F, 0.125F, 0.875F, 0.875F, 0.875F);
	}
	
	@Override
	protected boolean canStay(Level level, int x, int y, int z) {
		return isGround(level.getBlockState(x, y - 1, z));
	}
	
	protected boolean isGround(BlockState state) {
		return state.isIn(BNBBlockTags.NETHERRACK_TERRAIN) || state.isIn(BNBBlockTags.ORGANIC_TERRAIN);
	}
}
