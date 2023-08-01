package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BaseBlock;
import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.vbe.block.VBELeavesBlock;

import java.util.Collections;
import java.util.List;
import java.util.Random;

public class NetherLeavesBlock extends VBELeavesBlock {
	private BaseBlock sapling;
	
	public NetherLeavesBlock(Identifier id) {
		super(id, Material.LEAVES, 9);
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
		int count = LEAVES.getDropCount(level.random);
		if (count == 0) return Collections.emptyList();
		return Collections.singletonList(new ItemStack(sapling, count, 0));
	}
	
	public void setSapling(BaseBlock sapling) {
		this.sapling = sapling;
	}
	
	private void tickVine(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.getBlock() instanceof NetherVineBlock vine) {
			vine.tick(level, x, y, z);
		}
	}
}
