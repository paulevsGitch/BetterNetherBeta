package paulevs.bnb.mixin.client;

import net.minecraft.client.render.LevelRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(LevelRenderer.class)
public interface LevelRendererAccessor {
	@Accessor("sectionCounX")
	int bnb_getSectionCounX();
}
