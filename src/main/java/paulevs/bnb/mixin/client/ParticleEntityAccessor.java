package paulevs.bnb.mixin.client;

import net.minecraft.entity.technical.ParticleEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ParticleEntity.class)
public interface ParticleEntityAccessor {
	@Accessor("maxAge")
	void bnb_setAge(int maxAge);
}
