package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.container.FurnaceScreen;
import net.minecraft.client.render.TextRenderer;
import net.minecraft.util.hit.HitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.crafting.BNBFurnaceBlock;

@Mixin(FurnaceScreen.class)
public class FurnaceScreenMixin {
	@Shadow private FurnaceBlockEntity furnace;
	
	@WrapOperation(method = "renderForeground", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/TextRenderer;drawText(Ljava/lang/String;III)V",
		ordinal = 0
	))
	private void bnb_renderTitle(TextRenderer renderer, String text, int x, int y, int color, Operation<Void> operation) {
		if (bnb_getBlock() instanceof BNBFurnaceBlock) {
			String name = furnace.getInventoryName();
			int px = (176 - renderer.getTextWidth(name)) >> 1;
			operation.call(renderer, name, px, y, color);
		}
		else operation.call(renderer, text, x, y, color);
	}
	
	@Unique
	private Block bnb_getBlock() {
		Minecraft minecraft = BNBClient.getMinecraft();
		if (minecraft.level.isRemote) {
			HitResult hit = minecraft.hitResult;
			return minecraft.level.getBlockState(hit.x, hit.y, hit.z).getBlock();
		}
		return minecraft.level.getBlockState(furnace.x, furnace.y, furnace.z).getBlock();
	}
}
