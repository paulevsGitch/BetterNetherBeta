package paulevs.bnb.mixin.client;

import net.modificationstation.stationapi.api.client.render.model.BakedQuad;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.bnb.rendering.EmissiveQuad;

@Mixin(value = BakedQuad.class, remap = false)
public class BakedQuadMixin implements EmissiveQuad {
	@Unique private boolean isEmissive;
	
	@Override
	public void setEmissive(boolean emissive) {
		isEmissive = emissive;
	}
	
	@Override
	public boolean isEmissive() {
		return isEmissive;
	}
}
