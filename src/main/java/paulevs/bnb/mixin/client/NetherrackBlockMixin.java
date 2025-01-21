package paulevs.bnb.mixin.client;

import net.minecraft.block.Block;
import net.minecraft.block.NetherrackBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.world.BlockStateView;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.listener.ClientListener;

@Mixin(NetherrackBlock.class)
public abstract class NetherrackBlockMixin extends Block {
	public NetherrackBlockMixin(int id, Material material) {
		super(id, material);
	}
	
	@Override
	public int getTexture(BlockView view, int x, int y, int z, int side) {
		BlockState state = ((BlockStateView) view).getBlockState(x, y + 1, z);
		if (side > 0 && (state.isOf(BNBBlocks.ASH_BLOCK) || state.isOf(BNBBlocks.ASH_LAYER))) {
			return side == 1 ? ClientListener.ashTexture : ClientListener.netherrackAshTexture;
		}
		return super.getTexture(view, x, y, z, side);
	}
}
