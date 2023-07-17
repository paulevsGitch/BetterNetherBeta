package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.vbe.block.VBELeavesBlock;

import java.util.Random;

public class NetherLeavesBlock extends VBELeavesBlock {
	public NetherLeavesBlock(Identifier id) {
		super(id, Material.LEAVES, 9);
		setHardness(LEAVES.getHardness());
		disableNotifyOnMetaDataChange();
		setSounds(GRASS_SOUNDS);
		disableStat();
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
		checkVine(level, x, y - 1, z);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		super.onScheduledTick(level, x, y, z, random);
		checkVine(level, x, y - 1, z);
	}
	
	private void checkVine(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (state.getBlock() instanceof NetherCeilPlantBlock && !state.getBlock().canPlaceAt(level, x, y, z)) {
			level.scheduleTick(x, y, z, state.getBlock().id, 0);
		}
	}
}
