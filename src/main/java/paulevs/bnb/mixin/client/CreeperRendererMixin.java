package paulevs.bnb.mixin.client;

import net.minecraft.client.render.entity.CreeperRenderer;
import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.model.Creeper;
import net.minecraft.client.render.entity.model.EntityModelBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.NetherMob;

@Mixin(CreeperRenderer.class)
public abstract class CreeperRendererMixin extends LivingEntityRenderer {
	private static final String BNB_SOUL_TEXTURE = BetterNetherBeta.getTexturePath("entity", "soul_creeper");
	
	@Shadow
	private EntityModelBase field_1685;
	
	public CreeperRendererMixin(EntityModelBase model, float shadowScale) {
		super(model, shadowScale);
	}
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void bnb_creeperRender(net.minecraft.entity.monster.Creeper creeper, int i, float f, CallbackInfoReturnable<Boolean> info) {
		int type = ((NetherMob) creeper).getMobType();
		if (type > 0) {
			if (model == null) {
				model = new Creeper();
			}
			this.bindTexture(BNB_SOUL_TEXTURE);
			//this.setModel(model);
			//float var4 = (1.0F - creeper.getBrightnessAtEyes(1.0F)) * 0.5F;
			//GL11.glEnable(GL11.GL_BLEND);
			//GL11.glDisable(GL11.GL_ALPHA_TEST);
			//GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			//GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			info.setReturnValue(true);
		}
	}
}
