package paulevs.bnb.world.biome;

import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityEntry;
import net.minecraft.entity.monster.Ghast;
import net.minecraft.entity.monster.ZombiePigman;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.Vec3f;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.ClientUtil;

public class NetherBiome extends Biome {
	private List<Structure> trees = Lists.newArrayList();
	private List<Structure> plants = Lists.newArrayList();
	private List<Structure> ceil = Lists.newArrayList();
	
	private BlockState topBlock = new BlockState(BlockBase.NETHERRACK);
	
	private int treeCount = 0;
	private int plantCount = 0;
	private int ceilCount = 0;
	
	private boolean hasFire = true;
	
	private Vec3f fogColor = Vec3f.method_1293(0.2F, 0.03F, 0.03F);
	
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
	
	@Override
	public Structure getTree(Random rand) {
		return trees.get(rand.nextInt(trees.size()));
	}
	
	public int getMaxTreeCount() {
		return trees.isEmpty() ? 0 : treeCount;
	}
	
	public void setMaxTreeCount(int treeCount) {
		this.treeCount = treeCount;
	}
	
	public int getMaxPlantCount() {
		return plants.isEmpty() ? 0 : plantCount;
	}
	
	public void setMaxPlantCount(int plantCount) {
		this.plantCount = plantCount;
	}
	
	public int getMaxCeilPlantCount() {
		return ceil.isEmpty() ? 0 : ceilCount;
	}
	
	public void setMaxCeilPlantCount(int plantCount) {
		this.ceilCount = plantCount;
	}
	
	public BlockState getTopBlock() {
		return topBlock;
	}
	
	public void setTopBlock(BlockState block) {
		this.topBlock = block;
	}
	
	public void addTree(Structure structure) {
		trees.add(structure);
	}
	
	public void addPlant(Structure structure) {
		plants.add(structure);
	}
	
	public void addCeilPlant(Structure structure) {
		ceil.add(structure);
	}

	public Structure getPlant(Random random) {
		return plants.get(random.nextInt(plants.size()));
	}
	
	public Structure getCeilPlant(Random random) {
		return ceil.get(random.nextInt(ceil.size()));
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
	
	public int getParticleID(Random random) {
		return 0;
	}
}
