package paulevs.bnb.world.biome;

import com.google.common.collect.Lists;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityEntry;
import net.minecraft.entity.monster.Ghast;
import net.minecraft.entity.monster.ZombiePigman;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.Vec3f;
import paulevs.bnb.sound.NetherAmbientSound;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.world.structures.StructureInstance;

import java.util.List;
import java.util.Random;

public class NetherBiome extends Biome {
	protected static final BlockState NETHERRRACK = new BlockState(BlockBase.NETHERRACK);
	private Vec3f fogColor = Vec3f.method_1293(0.2F, 0.03F, 0.03F);
	private final List<StructureInstance> structures = Lists.newArrayList();
	protected BlockState fillBlock = NETHERRRACK;
	protected BlockState topBlock = NETHERRRACK;
	private NetherAmbientSound ambientSound;
	private boolean hasFire = true;
	private float fogDensity = 1F;
	private int topDepth = 1;
	
	public NetherBiome(String name) {
		this.setName(name);
		this.monsters.clear();
		this.creatures.clear();
		this.waterCreatures.clear();
		this.addMonsterSpawn(ZombiePigman.class, 10);
		this.addMonsterSpawn(Ghast.class, 1);
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
	
	public BlockState getTopBlock(Level level, int x, int y, int z) {
		return topBlock;
	}
	
	public BlockState getBottomBlock(Level level, int x, int y, int z) {
		return getTopBlock(level, x, y, z);
	}
	
	public BlockState getFillBlock(Level level, int x, int y, int z) {
		return fillBlock;
	}
	
	public void setTopBlock(BlockState block) {
		this.topBlock = block;
	}
	
	public void setFillBlock(BlockState block) {
		this.fillBlock = block;
	}
	
	public void addStructure(Structure structure, float chance, int count) {
		structures.add(new StructureInstance(structure, chance, count));
	}
	
	public List<StructureInstance> getStructures() {
		return structures;
	}
	
	public void setFire(boolean fire) {
		this.hasFire = fire;
	}
	
	public boolean hasFire() {
		return hasFire;
	}
	
	public Vec3f getFogColor() {
		return fogColor;
	}
	
	public void setFogColor(float r, float g, float b) {
		fogColor.x = r;
		fogColor.y = g;
		fogColor.z = b;
		if (ClientUtil.haveShaders()) {
			fogColor.x *= 0.5;
			fogColor.y *= 0.5;
			fogColor.z *= 0.5;
		}
	}
	
	public void setFogColor(int r, int g, int b) {
		setFogColor(r / 255F, g / 255F, b / 255F);
	}
	
	public void setFogColor(String hex) {
		setFogColor(
			(float) Integer.parseInt(hex.substring(0, 2), 16) / 255F,
			(float) Integer.parseInt(hex.substring(2, 4), 16) / 255F,
			(float) Integer.parseInt(hex.substring(4, 6), 16) / 255F
		);
	}
	
	public float getParticleChance() {
		return -1;
	}
	
	public boolean isParticlesEmissive() {
		return true;
	}
	
	public int getParticleID(Random random) {
		return 0;
	}
	
	public void setTopDepth(int depth) {
		topDepth = depth;
	}
	
	public int getTopDepth() {
		return topDepth;
	}
	
	public void setAmbientSound(NetherAmbientSound ambientSound) {
		this.ambientSound = ambientSound;
	}
	
	public NetherAmbientSound getAmbientSound() {
		return ambientSound;
	}
}
