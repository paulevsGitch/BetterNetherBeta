package paulevs.bnb.world.structure.common;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.BNBBlockTags;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PillarStructure extends Structure {
	private final List<PillarEntry> entries = new ArrayList<>();
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y - 1, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		if (!level.getBlockState(x, y, z).isAir()) return false;
		for (PillarEntry entry : entries) {
			int height = entry.minHeight;
			if (entry.delta > 1) height += random.nextInt(entry.delta);
			for (int i = 0; i < height; i++) {
				level.setBlockState(x, y, z, entry.state);
				y++;
			}
		}
		return true;
	}
	
	public PillarStructure addSection(BlockState state, int minHeight, int maxHeight) {
		entries.add(new PillarEntry(state, minHeight, maxHeight - minHeight + 1));
		return this;
	}
	
	private record PillarEntry (BlockState state, int minHeight, int delta) {}
}
