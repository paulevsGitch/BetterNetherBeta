package paulevs.bnb.mixin.client;

import net.minecraft.util.maths.Vec3I;
import net.minecraft.util.maths.VectorCache;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(VectorCache.class)
public interface VectorCacheAccessor {
	@Accessor("data")
	Vec3I[] bnb_getData();
}
