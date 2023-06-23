package paulevs.bnb.world.biome;

import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.BlockPos;

public class SimpleNetherBiome extends NetherBiome {
	private BlockState surface = NETHERRACK;
	
	public SimpleNetherBiome(Identifier id) {
		super(id);
	}
	
	public SimpleNetherBiome setSurface(BlockState surface) {
		this.surface = surface;
		return this;
	}
	
	@Override
	public void buildSurfaceColumn(Chunk chunk, BlockPos pos) {
		chunk.setBlockState(pos.getX(), pos.getY(), pos.getZ(), surface);
	}
}
