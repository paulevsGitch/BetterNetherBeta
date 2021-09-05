package paulevs.bnb.mixin.client;

import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.entity.CloudEntity;
import paulevs.bnb.entity.render.CloudEntityRenderer;

import java.util.Map;

@Mixin(EntityRenderDispatcher.class)
public class EntityRenderDispatcherMixin {
	@Shadow
	private Map renderers;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void bnb_initEntityRender(CallbackInfo info) {
		bnb_addRender(CloudEntity.class, new CloudEntityRenderer());
	}
	
	private <R extends EntityRenderer> void bnb_addRender(Class<? extends EntityBase> entityClass, R entityRender) {
		renderers.put(entityClass, entityRender);
		entityRender.setDispatcher(EntityRenderDispatcher.class.cast(this));
	}
}
