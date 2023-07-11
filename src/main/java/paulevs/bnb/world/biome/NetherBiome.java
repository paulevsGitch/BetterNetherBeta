package paulevs.bnb.world.biome;

import net.minecraft.block.BaseBlock;
import net.minecraft.entity.EntityEntry;
import net.minecraft.level.biome.BaseBiome;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import paulevs.bnb.world.structures.placers.StructurePlacer;

import java.util.ArrayList;
import java.util.List;

public class NetherBiome extends BaseBiome {
	protected static final BlockState NETHERRACK = BaseBlock.NETHERRACK.getDefaultState();
	private final Vec3f fogColor = new Vec3f(0.2F, 0.03F, 0.03F);
	private final List<StructurePlacer> placers = new ArrayList<>();
	public final Identifier id;
	
	private float fogDensity = 1F;
	private Identifier ambientSound;
	
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
	
	public BlockState getFillBlock() {
		return NETHERRACK;
	}
	
	public BlockState getSurfaceBlock() {
		return NETHERRACK;
	}
	
	public Vec3f getFogColor() {
		return fogColor;
	}
	
	public NetherBiome addStructure(StructurePlacer placer) {
		placers.add(placer);
		return this;
	}
	
	public List<StructurePlacer> getStructures() {
		return placers;
	}
	
	public Identifier getAmbientSound() {
		return ambientSound;
	}
	
	public NetherBiome setAmbientSound(Identifier ambientSound) {
		this.ambientSound = ambientSound;
		return this;
	}
	
	public NetherBiome setFogColor(int rgb) {
		float r = ((rgb >> 16) & 255) / 255.0F;
		float g = ((rgb >> 8) & 255) / 255.0F;
		float b = (rgb & 255) / 255.0F;
		fogColor.set(r, g, b);
		return this;
	}
}
