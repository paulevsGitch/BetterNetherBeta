package paulevs.bnb.block.terrain;

import net.minecraft.block.material.Material;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.sound.BNBSounds;

import java.util.List;

public class NetherTerrainBlock extends TemplateBlock {
	public NetherTerrainBlock(Identifier id, Material material) {
		super(id, material);
		//setTicksRandomly(true);
		setHardness(NETHERRACK.getHardness() * 1.5F);
	}
	
	public NetherTerrainBlock(Identifier id) {
		this(id, Material.STONE);
		setHardness(NETHERRACK.getHardness());
		setSounds(BNBSounds.NYLIUM_BLOCK);
	}
	
	/*@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, BlockState state) {
		if (bonemealStructures.isEmpty()) return false;
		if (level.isRemote) return true;
		for (Structure structure : bonemealStructures) {
			structure.generate(level, level.random, x, y, z);
		}
		level.updateArea(x - 16, y - 16, z - 16, x + 16, y + 16, z + 16);
		return true;
	}*/
	
	@Override
	public List<ItemStack> getDropList(Level world, int x, int y, int z, BlockState state, int meta) {
		return List.of(new ItemStack(NETHERRACK));
	}
}
