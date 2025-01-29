package paulevs.bnb.block.stone;

import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.item.BNBItems;

import java.util.List;

public class SulphurBlock extends BNBNetherrack {
	private final int dropCount;
	
	public SulphurBlock(Identifier identifier, int dropCount) {
		super(identifier);
		this.dropCount = dropCount;
	}
	
	@Override
	public List<ItemStack> getDropList(Level world, int x, int y, int z, BlockState state, int meta) {
		return List.of(new ItemStack(BNBItems.SULPHUR, dropCount));
	}
}
