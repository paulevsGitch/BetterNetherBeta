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
import paulevs.bnb.util.BlockState;

public class NetherBiome extends Biome {
	private List<Structure> trees = Lists.newArrayList();
	private List<Structure> plants = Lists.newArrayList();
	private List<Structure> ceil = Lists.newArrayList();
	
	private BlockState topBlock = new BlockState(BlockBase.NETHERRACK);
	
	private int treeCount = 0;
	private int plantCount = 0;
	private int ceilCount = 0;
	
	private boolean hasFire = true;
	
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
		return treeCount;
	}
	
	public void setMaxTreeCount(int treeCount) {
		this.treeCount = treeCount;
	}
	
	public int getMaxPlantCount() {
		return plantCount;
	}
	
	public void setMaxPlantCount(int plantCount) {
		this.plantCount = plantCount;
	}
	
	public int getMaxCeilPlantCount() {
		return ceilCount;
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
}
