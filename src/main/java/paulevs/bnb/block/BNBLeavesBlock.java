package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.vbe.block.VBEBlockTags;
import paulevs.vbe.block.VBELeavesBlock;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class BNBLeavesBlock extends VBELeavesBlock {
	private Block sapling;
	
	public BNBLeavesBlock(Identifier id, int radius) {
		super(id, BNBBlockMaterials.NETHER_LEAVES, radius);
		setHardness(LEAVES.getHardness());
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (sapling == null || level.random.nextInt(31) > 0) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(sapling));
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int blockID) {
		super.onAdjacentBlockUpdate(level, x, y, z, blockID);
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			tickNeighbour(level, x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
		}
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		super.onScheduledTick(level, x, y, z, random);
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			tickNeighbour(level, x + dir.getOffsetX(), y + dir.getOffsetY(), z + dir.getOffsetZ());
		}
	}
	
	public void setSapling(Block sapling) {
		this.sapling = sapling;
	}
	
	private void tickNeighbour(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this) && !state.isIn(VBEBlockTags.LEAVES)) {
			state.getBlock().onAdjacentBlockUpdate(level, x, y, z, state.getBlock().id);
		}
	}
}
