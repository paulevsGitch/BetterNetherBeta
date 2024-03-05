package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import paulevs.bnb.block.property.BNBBlockProperties;

public class PillarBlock extends TemplateBlock {
	public PillarBlock(Identifier id, Material material) {
		super(id, material);
		setDefaultState(getDefaultState().with(BNBBlockProperties.AXIS, Axis.Y));
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.AXIS);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Axis axis = context.getSide().getAxis();
		return getDefaultState().with(BNBBlockProperties.AXIS, axis);
	}
}
