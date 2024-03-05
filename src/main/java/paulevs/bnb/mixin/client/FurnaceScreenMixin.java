package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.gui.screen.container.FurnaceScreen;
import net.minecraft.client.render.TextRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.block.entity.NetherrackFurnaceBlockEntity;

@Mixin(FurnaceScreen.class)
public class FurnaceScreenMixin {
	@Shadow private FurnaceBlockEntity furnace;
	
	@WrapOperation(method = "renderForeground", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/client/render/TextRenderer;drawText(Ljava/lang/String;III)V",
		ordinal = 0
	))
	private void bnb_renderTitle(TextRenderer renderer, String text, int x, int y, int color, Operation<Void> operation) {
		if (furnace instanceof NetherrackFurnaceBlockEntity) {
			String name = furnace.getInventoryName();
			int px = (176 - renderer.getTextWidth(name)) >> 1;
			operation.call(renderer, name, px, y, color);
		}
		else operation.call(renderer, text, x, y, color);
	}
}
