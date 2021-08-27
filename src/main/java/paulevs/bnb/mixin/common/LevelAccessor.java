package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.dimension.DimensionData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(Level.class)
public interface LevelAccessor {
	@Accessor("dimensionData")
	DimensionData bnb_getDimensionData();
	
	@Invoker
	boolean callIsChunkLoaded(int chunkX, int chunkY);
}
