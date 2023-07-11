package paulevs.bnb.world.structures.scatters;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.MossBlock;

import java.util.Random;

public class BlockMossScatterStructure extends VolumeScatterStructure {
	private final BlockPos.Mutable bp = new BlockPos.Mutable();
	private final BlockState mossBlock;
	private final MossBlock moss;
	
	public BlockMossScatterStructure(int radius, float density, BaseBlock mossBlock, MossBlock moss) {
		super(radius, density);
		this.mossBlock = mossBlock.getDefaultState();
		this.moss = moss;
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), mossBlock);
		for (byte i = 0; i < 6; i++) {
			if (random.nextBoolean()) continue;
			Direction dir = Direction.byId(i);
			bp.set(pos).move(dir);
			if (!level.getBlockState(bp.getX(), bp.getY(), bp.getZ()).isAir()) continue;
			BlockState state = moss.getStructureState(level, bp);
			if (state != null) {
				level.setBlockState(bp.getX(), bp.getY(), bp.getZ(), state);
			}
		}
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		return level.getBlockState(pos.getX(), pos.getY(), pos.getZ()).isOf(BaseBlock.NETHERRACK);
	}
}
