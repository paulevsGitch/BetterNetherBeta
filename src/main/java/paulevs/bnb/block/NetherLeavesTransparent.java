package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class NetherLeavesTransparent extends NetherLeavesBlock {
	public NetherLeavesTransparent(Identifier id) {
		super(id);
		setLightOpacity(4);
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	@Environment(value=EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		if (!(view instanceof BlockStateView blockStateView)) {
			return super.isSideRendered(view, x, y, z, side);
		}
		Direction dir = Direction.byId(side);
		BlockState state = blockStateView.getBlockState(
			x + dir.getOffsetX(),
			y + dir.getOffsetY(),
			z + dir.getOffsetZ()
		);
		return state.isOf(this) || super.isSideRendered(view, x, y, z, side);
	}
}
