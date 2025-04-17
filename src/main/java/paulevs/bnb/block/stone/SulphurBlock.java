package paulevs.bnb.block.stone;

import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.item.BNBItems;

import java.util.List;

public class SulphurBlock extends BNBNetherrack {
	private final int dropCountMin;
	private final int dropDelta;
	
	public SulphurBlock(Identifier identifier, int dropCountMin, int dropCountMax) {
		super(identifier);
		this.dropCountMin = dropCountMin;
		this.dropDelta = dropCountMax - dropCountMin + 1;
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		int count = dropDelta > 1 ? level.random.nextInt(dropDelta) + dropCountMin : dropCountMin;
		return List.of(new ItemStack(NETHERRACK), new ItemStack(BNBItems.SULPHUR, count));
	}
}
