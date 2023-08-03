package paulevs.bnb.world.structures.common;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.GlowstoneShards;
import paulevs.bnb.block.properties.BNBBlockProperties;

import java.util.Random;

public class CrystalStructure extends Structure {
	private final BlockState fullBlock;
	private final Direction direction;
	private final BlockState shards;
	private final int height;
	private final int radius;
	
	public CrystalStructure(BaseBlock fullBlock, GlowstoneShards shards, boolean ceiling, int height, int radius) {
		this.fullBlock = fullBlock.getDefaultState();
		this.direction = ceiling ? Direction.UP : Direction.DOWN;
		this.shards = shards.getDefaultState().with(BNBBlockProperties.DIRECTION, this.direction);
		this.height = height;
		this.radius = radius;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y + direction.getOffsetY(), z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		
		float scale = random.nextFloat() * 0.25F + 0.75F;
		int radius = MathHelper.floor(this.radius * scale + 0.5F);
		int sign = direction == Direction.UP ? -1 : 1;
		
		for (int dx = -radius; dx <= radius; dx++) {
			int px = x + dx;
			for (int dz = -radius; dz <= radius; dz++) {
				int pz = z + dz;
				float distance = 1 - MathHelper.sqrt(dx * dx + dz * dz) / radius;
				int height = MathHelper.floor(this.height * scale * distance - random.nextFloat() * 2F + 1F);
				
				if (height < 1) {
					if (height > -2 && random.nextInt(3) == 0) {
						for (int dy = 1; dy > -5; dy--) {
							int py = y + dy * sign;
							if (!level.getBlockState(px, py, pz).isAir()) continue;
							if (!level.getBlockState(px, py - sign, pz).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) continue;
							level.setBlockState(px, py, pz, shards);
							break;
						}
					}
					continue;
				}
				
				int start = random.nextInt(3) + 2;
				for (int dy = -start; dy < height; dy++) {
					int py = y + dy * sign;
					if (!level.getBlockState(px, py, pz).isAir()) continue;
					level.setBlockState(px, py, pz, fullBlock);
				}
				
				int py = y + height * sign;
				if (!level.getBlockState(px, py, pz).isAir()) continue;
				level.setBlockState(px, py, pz, shards);
			}
		}
		
		return true;
	}
}
