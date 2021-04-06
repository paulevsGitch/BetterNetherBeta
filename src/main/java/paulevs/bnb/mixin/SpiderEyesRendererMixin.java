package paulevs.bnb.mixin;

import org.lwjgl.opengl.GL11;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.SpiderEyesRenderer;
import net.minecraft.client.render.entity.model.EntityModelBase;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.NetherMob;

@Mixin(SpiderEyesRenderer.class)
public abstract class SpiderEyesRendererMixin extends LivingEntityRenderer {
	private static final String[] TEXTURES = new String[] {
		"/assets/" + BetterNetherBeta.MOD_ID + "/textures/entity/crimson_spider_e.png",
		"/assets/" + BetterNetherBeta.MOD_ID + "/textures/entity/warped_spider_e.png",
		"/assets/" + BetterNetherBeta.MOD_ID + "/textures/entity/poison_spider_e.png"
	};
	
	public SpiderEyesRendererMixin(EntityModelBase model, float shadowScale) {
		super(model, shadowScale);
	}
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void bnb_spiderRender(net.minecraft.entity.monster.Spider spider, int i, float f, CallbackInfoReturnable<Boolean> info) {
		int type = ((NetherMob) spider).getMobType();
		if (type > 0) {
			this.bindTexture(TEXTURES[type - 1]);
			float var4 = (1.0F - spider.getBrightnessAtEyes(1.0F)) * 0.5F;
			GL11.glEnable(GL11.GL_BLEND);
			GL11.glDisable(GL11.GL_ALPHA_TEST);
			GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
			GL11.glColor4f(1.0F, 1.0F, 1.0F, var4);
			info.setReturnValue(true);
			info.cancel();
		}
	}
}
