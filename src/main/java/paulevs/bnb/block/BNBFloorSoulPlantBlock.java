package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

public class BNBFloorSoulPlantBlock extends BNBFloorPlantBlock {
	public BNBFloorSoulPlantBlock(Identifier id) {
		super(id);
	}
	
	public BNBFloorSoulPlantBlock(Identifier id, Material material, boolean needShears) {
		super(id, material, needShears);
	}
	
	@Override
	protected boolean isGround(BlockState state) {
		return state.isIn(BNBBlockTags.SOUL_TERRAIN);
	}
}
