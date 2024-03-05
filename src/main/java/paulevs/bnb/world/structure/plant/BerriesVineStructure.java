package paulevs.bnb.world.structure.plant;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.bnb.block.NetherVineBlock;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.block.property.BNBBlockProperties.VineShape;

import java.util.Random;

public class BerriesVineStructure extends Structure {
	private final NetherVineBlock normal;
	private final NetherVineBlock berries;
	private final int minLength;
	private final int delta;
	
	public BerriesVineStructure(NetherVineBlock normal, NetherVineBlock berries, int minLength, int maxLength) {
		this.normal = normal;
		this.berries = berries;
		this.minLength = minLength;
		this.delta = maxLength - minLength + 1;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (!level.getBlockState(x, y, z).isAir()) return false;
		if (!normal.canPlaceAt(level, x, y, z)) return false;
		
		BlockState normal = this.normal.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.NORMAL);
		BlockState bottom = this.normal.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM);
		BlockState berriesNormal = this.berries.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.NORMAL);
		BlockState berriesBottom = this.berries.getDefaultState().with(BNBBlockProperties.VINE_SHAPE, VineShape.BOTTOM);
		int length = minLength + random.nextInt(delta);
		
		for (int i = 1; i <= length; i++) {
			BlockState state = level.getBlockState(x, y - 1, z);
			boolean norm = state.isAir() && i < length;
			boolean ber = random.nextInt(8) == 0;
			state = norm ? (ber ? berriesNormal : normal) : (ber ? berriesBottom : bottom);
			level.setBlockState(x, y, z, state);
			if (!norm) return i > 1;
			y--;
		}
		
		return true;
	}
}
