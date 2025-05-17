package paulevs.bnb.mixin.client;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.modificationstation.stationapi.api.client.render.model.json.JsonUnbakedModel;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import paulevs.bnb.BNB;

@Mixin(value = JsonUnbakedModel.Deserializer.class, remap = false)
public class JsonUnbakedModelMixin {
	@WrapOperation(
		method = "Lnet/modificationstation/stationapi/api/client/render/model/json/JsonUnbakedModel$Deserializer;resolveReference(Lnet/modificationstation/stationapi/api/util/Identifier;Ljava/lang/String;)Lcom/mojang/datafixers/util/Either;",
		at = @At(
			value = "INVOKE",
			target = "Lnet/modificationstation/stationapi/api/util/Identifier;tryParse(Ljava/lang/String;)Lnet/modificationstation/stationapi/api/util/Identifier;"
		)
	)
	private static Identifier bnb_changeTexture(String path, Operation<Identifier> original) {
		Identifier result = original.call(path);
		if (result.namespace == Namespace.MINECRAFT && result.path.equals("block/netherrack")) {
			result = BNB.id("block/netherrack");
		}
		return result;
	}
}
