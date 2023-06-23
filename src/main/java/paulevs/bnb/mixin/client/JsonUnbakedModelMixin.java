package paulevs.bnb.mixin.client;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import net.modificationstation.stationapi.api.client.render.model.ModelBakeSettings;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElement;
import net.modificationstation.stationapi.api.client.render.model.json.ModelElementFace;
import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.rendering.EmissiveQuad;

@Mixin(value = JsonUnbakedModel.class, remap = false)
public class JsonUnbakedModelMixin {
	@Inject(method = "createQuad", at = @At("RETURN"))
	private static void bnb_createQuad(ModelElement element, ModelElementFace elementFace, Sprite sprite, Direction side, ModelBakeSettings settings, Identifier id, CallbackInfoReturnable<BakedQuad> info) {
		BakedQuad quad = info.getReturnValue();
		if (sprite.getContents().getId().id.endsWith("_e")) {
			EmissiveQuad emissiveQuad = EmissiveQuad.cast(quad);
			emissiveQuad.setEmissive(true);
		}
	}
}
