package paulevs.bnb.world.structure.scatter;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.SpiderNetBlock;

import java.util.Random;

public class CocoonStructure extends VolumeScatterStructure {
	protected final BlockState cocoon;
	
	public CocoonStructure(Block cocoon) {
		super(6, 0.8F);
		this.cocoon = cocoon.getDefaultState();
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!cocoon.getBlock().canPlaceAt(level, x, y, z)) return false;
		int count = 0;
		for (int i = 0; i < 25; i++) {
			int px = x + random.nextInt(3) - 1;
			int py = y + random.nextInt(3) - 1;
			int pz = z + random.nextInt(3) - 1;
			if (!cocoon.getBlock().canPlaceAt(level, px, py, pz)) continue;
			level.setBlockState(px, py, pz, cocoon);
			count++;
		}
		if (count == 0) return false;
		return super.generate(level, random, x, y, z);
	}
	
	@Override
	protected void place(Level level, Random random, BlockPos pos, BlockPos center) {
		BlockState state = BNBBlocks.SPIDER_NET.getFacingState(level, pos.x, pos.y, pos.z);
		if (SpiderNetBlock.isEmpty(state)) return;
		level.setBlockState(pos, state);
	}
	
	@Override
	protected boolean canPlaceAt(Level level, BlockPos pos) {
		BlockState state = level.getBlockState(pos);
		return state.getMaterial().isReplaceable() && state.getMaterial() != Material.LAVA;
	}
}
