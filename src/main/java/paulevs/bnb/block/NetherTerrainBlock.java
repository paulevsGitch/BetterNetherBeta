package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.sound.BNBSounds;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NetherTerrainBlock extends TemplateBlock {
	private final List<Structure> bonemealStructures = new ArrayList<>();
	private Biome targetBiome;
	
	public NetherTerrainBlock(Identifier id, Material material) {
		super(id, material);
		setTicksRandomly(true);
	}
	
	public NetherTerrainBlock(Identifier id) {
		super(id, Material.STONE);
		setHardness(NETHERRACK.getHardness());
		setSounds(BNBSounds.NYLIUM_BLOCK);
		setTicksRandomly(true);
	}
	
	public void setTargetBiome(Biome targetBiome) {
		this.targetBiome = targetBiome;
	}
	
	public void addBonemealStructure(Structure structure) {
		bonemealStructures.add(structure);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		if (level.isRemote) return;
		BlockState above = level.getBlockState(x, y + 1, z);
		
		if (blocksGrow(above)) {
			level.setBlockState(x, y, z, NETHERRACK.getDefaultState());
			level.updateBlock(x, y, z);
			return;
		}
		
		if (targetBiome == null) return;
		
		Direction dir = Direction.fromHorizontal(random.nextInt(4));
		int px = x + dir.getOffsetX();
		int pz = z + dir.getOffsetX();
		
		if (level.getBiomeSource().getBiome(px, pz) != targetBiome) return;
		if (!level.getBlockState(px, y, pz).isOf(NETHERRACK)) return;
		above = level.getBlockState(px, y + 1, pz);
		if (blocksGrow(above)) return;
		
		level.setBlockState(px, y, pz, level.getBlockState(x, y, z));
		level.updateBlock(px, y, pz);
	}
	
	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, BlockState state) {
		if (bonemealStructures.isEmpty()) return false;
		if (level.isRemote) return true;
		for (Structure structure : bonemealStructures) {
			structure.generate(level, level.random, x, y, z);
		}
		level.updateArea(x - 16, y - 16, z - 16, x + 16, y + 16, z + 16);
		return true;
	}
	
	private boolean blocksGrow(BlockState state) {
		if (state.isAir()) return false;
		if (state.getBlock().isFullOpaque() && state.getBlock().isFullCube() && LIGHT_OPACITY[state.getBlock().id] > 5) return true;
		return state.getMaterial().isLiquid();
	}
}
