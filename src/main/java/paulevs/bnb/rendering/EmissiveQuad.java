package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.render.model.BakedQuad;

@Environment(EnvType.CLIENT)
public interface EmissiveQuad {
	void setEmissive(boolean emissive);
	
	boolean isEmissive();
	
	static EmissiveQuad cast(BakedQuad quad) {
		return (EmissiveQuad) quad;
	}
}
