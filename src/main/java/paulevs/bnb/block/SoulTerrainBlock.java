package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

public class SoulTerrainBlock extends NetherTerrainBlock {
	public SoulTerrainBlock(Identifier id) {
		super(id, Material.DIRT);
		setHardness(SOUL_SAND.getHardness());
		setSounds(SOUL_SAND.sounds);
	}
	
	@Override
	public List<ItemStack> getDropList(Level world, int x, int y, int z, BlockState state, int meta) {
		return List.of(new ItemStack(SOUL_SAND));
	}
}
