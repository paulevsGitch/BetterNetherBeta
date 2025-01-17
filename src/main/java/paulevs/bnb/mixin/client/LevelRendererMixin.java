package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.LevelRenderer;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3D;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.rendering.BNBSkyRenderer;
import paulevs.bnb.world.generator.decorator.BNBChunkStatus;
import paulevs.bnb.world.generator.decorator.BNBWorldChunk;

@Mixin(LevelRenderer.class)
public class LevelRendererMixin {
	@Shadow private Minecraft minecraft;
	@Shadow private Level level;
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_onInit(Minecraft mc, TextureManager manager, CallbackInfo info) {
		BNBSkyRenderer.init(manager);
	}
	
	@Inject(method = "renderSky", at = @At("HEAD"), cancellable = true)
	private void bnb_renderSky(float delta, CallbackInfo info) {
		if (level.dimension.id != -1) return;
		BNBSkyRenderer.renderSky(minecraft);
		info.cancel();
	}
	
	@WrapOperation(method = "renderEntitiesFromPos", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;canRenderFrom(Lnet/minecraft/util/maths/Vec3D;)Z"
	))
	private boolean bnb_skipEmptyChunks(Entity entity, Vec3D pos, Operation<Boolean> original) {
		if (level.dimension.id == -1) {
			BNBWorldChunk chunk = BNBWorldChunk.cast(level.getChunkFromCache(entity.chunkX, entity.chunkZ));
			if (chunk.bnb_getStatus() != BNBChunkStatus.FINISHED) return false;
		}
		return original.call(entity, pos);
	}
}
