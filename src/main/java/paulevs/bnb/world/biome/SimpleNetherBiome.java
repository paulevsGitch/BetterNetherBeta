package paulevs.bnb.world.biome;

import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;

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
	public BlockState getSurfaceBlock() {
		return surface;
	}
}
