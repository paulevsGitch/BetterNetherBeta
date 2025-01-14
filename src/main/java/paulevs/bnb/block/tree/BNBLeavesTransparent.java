package paulevs.bnb.block.tree;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.LeavesBlock;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class BNBLeavesTransparent extends BNBLeavesBlock {
	public BNBLeavesTransparent(Identifier id) {
		super(id);
		setLightOpacity(4);
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	@Environment(value= EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		if (view instanceof BlockStateView blockStateView) {
			BlockState state = blockStateView.getBlockState(x, y, z);
			if (state.getBlock() instanceof LeavesBlock || !state.isOpaque()) {
				return true;
			}
			return super.isSideRendered(view, x, y, z, side);
		}
		return super.isSideRendered(view, x, y, z, side);
	}
}
