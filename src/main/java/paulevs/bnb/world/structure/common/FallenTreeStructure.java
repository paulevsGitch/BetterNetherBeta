package paulevs.bnb.world.structure.common;

import net.minecraft.block.Block;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class FallenTreeStructure extends Structure {
	private final Block log;
	private final int minLength;
	private final int deltaLength;
	
	public FallenTreeStructure(Block log, int minLength, int maxLength) {
		this.log = log;
		this.minLength = minLength;
		this.deltaLength = maxLength - minLength + 1;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y - 1, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		if (level.getBlockState(x, y, z).getMaterial().isLiquid()) return false;
		int length = random.nextInt(deltaLength) + minLength;
		Direction dir = Direction.fromHorizontal(random.nextInt(2));
		int px = x - dir.getOffsetX() * (length >> 1);
		int pz = z - dir.getOffsetZ() * (length >> 1);
		BlockState state = log.getDefaultState().with(BNBBlockProperties.AXIS, dir.getAxis());
		for (int i = 0; i < length; i++) {
			if (!level.getBlockState(px, y, pz).getMaterial().isReplaceable()) continue;
			level.setBlockState(px, y, pz, state);
			px += dir.getOffsetX();
			pz += dir.getOffsetZ();
		}
		return true;
	}
}
