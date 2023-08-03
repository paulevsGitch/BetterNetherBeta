package paulevs.bnb.mixin.common;

import net.minecraft.block.BaseBlock;
import net.minecraft.block.GlowstoneBlock;
import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.block.properties.BNBBlockProperties;

@Mixin(GlowstoneBlock.class)
public class GlowstoneBlockMixin extends BaseBlock {
	public GlowstoneBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_onInit(int j, int arg, Material par3, CallbackInfo ci) {
		this.setDefaultState(getDefaultState().with(BNBBlockProperties.DIRECTION, Direction.UP));
	}
	
	@Override
	public void appendProperties(Builder<BaseBlock, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.DIRECTION);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction face = context.getSide();
		return getDefaultState().with(BNBBlockProperties.DIRECTION, face);
	}
}
