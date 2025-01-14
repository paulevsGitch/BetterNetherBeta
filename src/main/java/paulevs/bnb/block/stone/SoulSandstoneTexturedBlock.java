package paulevs.bnb.block.stone;

import net.minecraft.block.Block;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.mixin.common.StairsBlockAccessor;

public class SoulSandstoneTexturedBlock extends SoulSandstoneBlock {
	public static final int[] TEXTURES = new int[3];
	
	public SoulSandstoneTexturedBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public int getTexture(int side) {
		Direction dir = Direction.byId(side);
		return switch (dir) {
			case UP -> TEXTURES[0];
			case DOWN -> TEXTURES[1];
			default -> TEXTURES[2];
		};
	}
	
	@Override
	public int getTexture(BlockView view, int x, int y, int z, int side) {
		Direction dir = Direction.byId(side);
		if (dir == Direction.UP) return TEXTURES[0];
		if (dir == Direction.DOWN) return TEXTURES[1];
		BlockState state = ((BlockStateView) view).getBlockState(x, y + 1, z);
		Block stateBlock = state.getBlock();
		if (stateBlock instanceof StairsBlockAccessor accessor) {
			stateBlock = accessor.bnb_getTemplate();
		}
		return stateBlock == this ? TEXTURES[1] : TEXTURES[2];
	}
}
