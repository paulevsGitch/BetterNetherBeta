package paulevs.bnb.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import paulevs.bnb.block.NetherVineBlock;
import paulevs.bnb.util.BlockUtil;

@Mixin(Living.class)
public abstract class LivingMixin extends EntityBase {
	public LivingMixin(Level level) {
		super(level);
	}

	@Inject(method = "method_932", at = @At("RETURN"), cancellable = true, locals = LocalCapture.CAPTURE_FAILSOFT)
	private void bnb_blockIsLadder(CallbackInfoReturnable<Boolean> info, int x, int y, int z) {
		if (!info.getReturnValue()) {
			BlockBase block = BlockUtil.blockByID(this.level.getTileId(x, y, z));
			if (block instanceof NetherVineBlock) {
				this.onGround = true;
				if (this.velocityY > 0.15) {
					this.velocityY = 0.15;
				}
				info.setReturnValue(true);
				info.cancel();
			}
		}
	}
}
