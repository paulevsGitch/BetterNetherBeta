package paulevs.bnb.block;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.function.Supplier;

public class TreeSaplingBlock extends NetherSaplingBlock {
	private final Supplier<Structure> bigTree;
	private final boolean[] mask;
	private final int maskWidth;
	private final int maskHeight;
	
	public TreeSaplingBlock(Identifier id, Supplier<Structure> normalTree, String[] bigTreeShape, Supplier<Structure> bigTree) {
		super(id, normalTree);
		this.bigTree = bigTree;
		maskWidth = bigTreeShape[0].length();
		maskHeight = bigTreeShape.length;
		mask = new boolean[maskWidth * bigTreeShape.length];
		for (int i = 0; i < mask.length; i++) {
			mask[i] = bigTreeShape[i / maskWidth].charAt(i % maskWidth) != ' ';
		}
	}
	
	private boolean containsShape(Level level, int x, int y, int z) {
		for (int i = 0; i < mask.length; i++) {
			if (!mask[i]) continue;
			int px = x + i % maskWidth;
			int pz = z + i / maskWidth;
			if (!level.getBlockState(px, y, pz).isOf(this)) return false;
		}
		return true;
	}
	
	private boolean tryGrowLarge(Level level, int x, int y, int z) {
		for (int i = 0; i < mask.length; i++) {
			if (!mask[i]) continue;
			int px = x - i % maskWidth;
			int pz = z - i / maskWidth;
			if (containsShape(level, px, y, pz)) {
				int tx = px + (maskWidth >> 1);
				int tz = pz + (maskHeight >> 1);
				if (bigTree.get().generate(level, level.random, tx, y, tz)) {
					for (int j = 0; j < mask.length; j++) {
						if (!mask[j]) continue;
						int mx = px + j % maskWidth;
						int mz = pz + j / maskWidth;
						if (level.getBlockState(mx, y, mz).isOf(this)) {
							level.setBlockState(mx, y, mz, States.AIR.get());
						}
					}
					return true;
				}
			}
		}
		return false;
	}
	
	@Override
	protected void grow(Level level, int x, int y, int z, boolean force) {
		if (!force && level.random.nextInt(16) > 0) return;
		
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		
		int stage = state.get(BNBBlockProperties.STAGE_4);
		if (stage < 3) {
			int increment = force ? Math.max(stage + 2, 3) : stage + 1;
			state = state.with(BNBBlockProperties.STAGE_4, increment);
			level.setBlockState(x, y, z, state);
			return;
		}
		
		if (tryGrowLarge(level, x, y, z)) return;
		if (structure.get().generate(level, level.random, x, y, z) && level.getBlockState(x, y, z).isOf(this)) {
			level.setBlockState(x, y, z, States.AIR.get());
		}
	}
}
