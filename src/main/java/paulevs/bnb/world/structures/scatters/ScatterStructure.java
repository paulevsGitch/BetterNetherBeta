package paulevs.bnb.world.structures.scatters;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.util.math.BlockPos;

import java.util.Random;

public abstract class ScatterStructure extends Structure {
	protected final int radius2;
	protected final int radius;
	protected final int count;
	
	protected ScatterStructure(int radius, int count) {
		this.radius2 = radius * 2 + 1;
		this.radius = radius;
		this.count = count;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		BlockPos.Mutable pos = new BlockPos.Mutable();
		BlockPos center = new BlockPos(x, y, z);
		for (int i = 0; i < count; i++) {
			pos.setX(x + random.nextInt(radius2) - radius);
			pos.setZ(z + random.nextInt(radius2) - radius);
			pos.setY(y + 5);
			for (int j = 0; j < 11; j++) {
				if (canPlaceAt(level, pos)) {
					place(level, random, pos, center);
					break;
				}
				pos.setY(pos.getY() - 1);
			}
		}
		return true;
	}
	
	protected abstract void place(Level level, Random random, BlockPos pos, BlockPos center);
	
	protected abstract boolean canPlaceAt(Level level, BlockPos pos);
}
