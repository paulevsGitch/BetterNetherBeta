package paulevs.bnb.mixin.common;

import net.minecraft.entity.EntityRegistry;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import paulevs.bnb.entity.CloudEntity;

@Mixin(EntityRegistry.class)
public class EntityRegistryMixin {
	@Shadow
	private static void register(Class type, String name, int id) {}
	
	static {
		register(CloudEntity.class, "Cloud", 110);
	}
}
