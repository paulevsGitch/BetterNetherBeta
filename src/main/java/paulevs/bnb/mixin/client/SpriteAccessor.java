package paulevs.bnb.mixin.client;

import net.modificationstation.stationapi.api.client.texture.Sprite;
import net.modificationstation.stationapi.api.registry.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Sprite.class)
public interface SpriteAccessor {
	@Accessor("atlasId")
	Identifier bnb_getID();
}
