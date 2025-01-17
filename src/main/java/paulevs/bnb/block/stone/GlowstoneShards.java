package paulevs.bnb.block.stone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.List;

public class GlowstoneShards extends ShardsBlock {
	public GlowstoneShards(Identifier id) {
		super(id);
		setLightEmittance(1.0F);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return 2.0F;
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		return List.of(new ItemStack(Item.glowstoneDust, 1 + level.random.nextInt(2)));
	}
}
