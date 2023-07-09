package paulevs.bnb.mixin.common;

import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.impl.level.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.level.chunk.FlattenedChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(FlattenedChunk.class)
public abstract class FlattenedChunkMixin extends Chunk {
	@Shadow protected abstract ChunkSection getSection(int y);
	
	public FlattenedChunkMixin(Level arg, int i, int j) {
		super(arg, i, j);
	}
	
	@ModifyVariable(method = "getOrCreateSection", at = @At("HEAD"), argsOnly = true, remap = false)
	private boolean bnb_disableSkylight(boolean fillSkyLight) {
		return !level.dimension.noSkyLight;
	}
	
	@Inject(method = "getLight(Lnet/minecraft/level/LightType;III)I", at = @At("HEAD"), cancellable = true)
	private void bnb_disableSkylight1(LightType type, int x, int y, int z, CallbackInfoReturnable<Integer> info) {
		if (level.dimension.noSkyLight && type == LightType.SKY) {
			info.setReturnValue(0);
		}
	}
	
	@Inject(method = "getLight(IIII)I", at = @At("HEAD"), cancellable = true)
	private void bnb_disableSkylight2(int x, int y, int z, int light, CallbackInfoReturnable<Integer> info) {
		if (level.dimension.noSkyLight) {
			int lightLevel = -light;
			ChunkSection section = getSection(y);
			int blockLight = section == null ? 0 : section.getLight(LightType.BLOCK, x, y & 15, z);
			if (blockLight > lightLevel) {
				lightLevel = blockLight;
			}
			info.setReturnValue(lightLevel);
		}
	}
}
