package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import paulevs.bnb.block.properties.BNBBlockProperties;

public class PillarBlock extends TemplateBlockBase {
	public PillarBlock(Identifier id, Material material) {
		super(id, material);
		setDefaultState(getDefaultState().with(BNBBlockProperties.AXIS, Axis.Y));
	}
	
	@Override
	public void appendProperties(Builder<BlockBase, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.AXIS);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Axis axis = context.getSide().getAxis();
		return getDefaultState().with(BNBBlockProperties.AXIS, axis);
	}
}
