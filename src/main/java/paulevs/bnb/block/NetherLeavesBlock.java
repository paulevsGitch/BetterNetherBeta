package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.vbe.block.VBELeavesBlock;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NetherLeavesBlock extends VBELeavesBlock {
	private Block sapling;
	
	public NetherLeavesBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_LEAVES, 15);
		setHardness(LEAVES.getHardness());
		setLightOpacity(255);
	}
	
	@Override
	@Environment(value= EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		return !view.isFullOpaque(x, y, z);
	}
	
	@Override
	public boolean isFullOpaque() {
		return true;
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int blockID) {
		super.onAdjacentBlockUpdate(level, x, y, z, blockID);
		tickVine(level, x, y - 1, z);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		super.onScheduledTick(level, x, y, z, random);
		tickVine(level, x, y - 1, z);
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (level.random.nextInt(31) == 0) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(sapling));
	}
	
	public void setSapling(Block sapling) {
		this.sapling = sapling;
	}
	
	private void tickVine(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.getBlock() instanceof NetherVineBlock vine) {
			vine.tick(level, x, y, z);
		}
	}
}
