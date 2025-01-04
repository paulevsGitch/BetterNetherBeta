package paulevs.bnb.world.structure.scatter;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.MossCoverBlock;

import java.util.Random;

public class BlockMossScatterStructure extends VolumeScatterStructure {
	private static final BlockState MOSSY_NETHERRACK = BNBBlocks.MOSSY_NETHERRACK.getDefaultState();
	private static final BlockState MOSS_BLOCK = BNBBlocks.NETHER_MOSS_BLOCK.getDefaultState();
	private final MutableBlockPos bp = new MutableBlockPos();
	
	public BlockMossScatterStructure(int radius, float density) {
		super(radius, density);
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), MOSS_BLOCK);
		
		for (byte dx = -1; dx < 2; dx++) {
			int wx = pos.x + dx;
			for (byte dz = -1; dz < 2; dz++) {
				int wz = pos.z + dz;
				for (byte dy = -1; dy < 2; dy++) {
					int wy = pos.y + dy;
					BlockState state = level.getBlockState(wx, wy, wz);
					if (state.isOf(Block.NETHERRACK)) {
						level.setBlockState(wx, wy, wz, MOSSY_NETHERRACK);
					}
				}
			}
		}
		
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			bp.set(pos.x, pos.y, pos.z).move(dir);
			if (random.nextBoolean()) continue;
			BlockState state = level.getBlockState(bp);
			if (!state.isAir()) continue;
			state = BNBBlocks.NETHER_MOSS_COVER.getStructureState(level, bp.getX(), bp.getY(), bp.getZ());
			if (state != null) {
				level.setBlockState(bp, state);
			}
		}
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		return level.getBlockState(pos.getX(), pos.getY(), pos.getZ()).isIn(BNBBlockTags.NETHERRACK_TERRAIN);
	}
}
