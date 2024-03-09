package paulevs.bnb.mixin.client;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.Entity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.entity.ObsidianBoatEntity;

@Mixin(EntityRenderer.class)
public class EntityRendererMixin {
	@Inject(method = "render(Lnet/minecraft/entity/Entity;DDDF)V", at = @At("HEAD"), cancellable = true)
	private void bnb_disableFireRendering(Entity entity, double par2, double par3, double par4, float par5, CallbackInfo info) {
		if (entity instanceof ObsidianBoatEntity) info.cancel();
	}
}
