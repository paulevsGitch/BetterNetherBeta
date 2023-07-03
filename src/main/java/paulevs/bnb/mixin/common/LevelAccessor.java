package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.dimension.DimensionData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Level.class)
public interface LevelAccessor {
	@Accessor("dimensionData")
	DimensionData bnb_getDimData();
}
