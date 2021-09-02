package paulevs.bnb.world.structures;

import net.minecraft.level.Level;
import paulevs.bnb.block.types.SoulTerrainType;
import paulevs.bnb.listeners.BlockListener;

import java.util.Random;

public class SoulSpireStructureConditional extends SoulSpireStructure {
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (level.getTileId(x, y - 1, z) != BlockListener.getBlockID("soul_soil") || level.getTileMeta(x, y - 1, z) != SoulTerrainType.SOUL_NYLIUM.getMeta()) {
			return false;
		}
		return super.generate(level, random, x, y, z);
	}
}
