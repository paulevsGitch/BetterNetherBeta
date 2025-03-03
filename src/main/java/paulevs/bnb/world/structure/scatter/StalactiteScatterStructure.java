package paulevs.bnb.world.structure.scatter;

import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class StalactiteScatterStructure extends ScatterStructure {
	protected final boolean inverted;
	
	public StalactiteScatterStructure(int radius, int count, boolean inverted) {
		super(radius, count);
		this.inverted = inverted;
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		int dx = center.x - pos.x;
		int dz = center.z - pos.z;
		int height = (int) ((1.0F - MCMath.sqrt(dx * dx + dz * dz) / radius) * 7 + random.nextFloat());
		if (height < 1) return;
		
		int offset = inverted ? -1 : 1;
		for (int i = 1; i < height; i++) {
			if (!level.getBlockState(pos.x, pos.y + i * offset, pos.z).isAir()) {
				height = i;
			}
		}
		
		BlockState state = BNBBlocks.NETHERRACK_STALACTITE.getDefaultState().with(BNBBlockProperties.INVERTED, inverted);
		for (int i = 0; i < height; i++) {
			int thickness = height - i - 1;
			level.setBlockState(pos.x, pos.y + i * offset, pos.z, state.with(BNBBlockProperties.THICKNESS, thickness));
		}
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		return level.getBlockState(pos).isAir() && level.getBlockState(pos.x, pos.y + (inverted ? 1 : -1), pos.z).isIn(BNBBlockTags.NETHERRACK_TERRAIN);
	}
}
