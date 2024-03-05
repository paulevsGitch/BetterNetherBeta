package paulevs.bnb.world.structure.scatter;

import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.MossBlock;

import java.util.Random;

public class MossScatterStructure extends VolumeScatterStructure {
	private final MossBlock moss;
	private BlockState state;
	
	public MossScatterStructure(int radius, float density, MossBlock moss) {
		super(radius, density);
		this.moss = moss;
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		level.setBlockState(pos.getX(), pos.getY(), pos.getZ(), state);
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		if (!level.getBlockState(pos.getX(), pos.getY(), pos.getZ()).isAir()) return false;
		state = moss.getStructureState(level, pos);
		return state != null;
	}
}
