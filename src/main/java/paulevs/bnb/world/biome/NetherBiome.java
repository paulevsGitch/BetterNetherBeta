package paulevs.bnb.world.biome;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityEntry;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Vec3f;

public class NetherBiome extends Biome {
	protected static final BlockState NETHERRACK = BlockBase.NETHERRACK.getDefaultState();
	private final Vec3f fogColor = new Vec3f(0.2F, 0.03F, 0.03F);
	public final Identifier id;
	
	private float fogDensity = 1F;
	
	public NetherBiome(Identifier id) {
		this.setName(id.toString());
		this.monsters.clear();
		this.creatures.clear();
		this.waterCreatures.clear();
		this.id = id;
	}
	
	@SuppressWarnings("unchecked")
	public void addMonsterSpawn(Class<?> entryClass, int rarity) {
		this.monsters.add(new EntityEntry(entryClass, rarity));
	}
	
	@SuppressWarnings("unchecked")
	public void addCreatureSpawn(Class<?> entryClass, int rarity) {
		this.creatures.add(new EntityEntry(entryClass, rarity));
	}
	
	public void setFogDensity(float density) {
		this.fogDensity = density;
	}
	
	public float getFogDensity() {
		return fogDensity;
	}
	
	public void buildSurfaceColumn(Chunk chunk, BlockPos pos) {}
	
	public BlockState getFillBlock(Chunk chunk, BlockPos pos) {
		return NETHERRACK;
	}
	
	public Vec3f getFogColor() {
		return fogColor;
	}
}
