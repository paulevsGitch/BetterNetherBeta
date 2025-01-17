package paulevs.bnb.block.stone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.item.BNBItems;

import java.util.List;

public class AmetrineShards extends ShardsBlock {
	public AmetrineShards(Identifier id) {
		super(id);
		setLightEmittance(0.25F);
		setHardness(0.75F);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.TYPE_16);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return 1.5F;
	}
	
	@Override
	public void onBlockPlaced(Level level, int x, int y, int z, BlockState replacedState) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		level.setBlockState(x, y, z, state.with(BNBBlockProperties.TYPE_16, AmetrineBlock.getTypeAt(x, y, z)));
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		return List.of(new ItemStack(BNBItems.AMETRINE_SHARD, 1 + level.random.nextInt(2)));
	}
}
