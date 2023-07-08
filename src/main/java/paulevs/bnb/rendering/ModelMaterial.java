package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;

@Environment(EnvType.CLIENT)
public record ModelMaterial(SpriteIdentifier texture, boolean emissive) {}
