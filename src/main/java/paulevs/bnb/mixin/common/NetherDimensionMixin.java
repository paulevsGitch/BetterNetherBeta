package paulevs.bnb.mixin.common;

import net.minecraft.level.biome.Biome;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.NetherDimension;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.world.dimension.StationDimension;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.world.biome.BNBBiomes;
import paulevs.bnb.world.generator.decorator.BNBChunkStatus;
import paulevs.bnb.world.generator.decorator.BNBWorldChunk;

import java.util.Collection;

@Mixin(NetherDimension.class)
public class NetherDimensionMixin extends Dimension implements StationDimension {
	@Override
	public int getHeight() {
		return 256;
	}
	
	@Inject(method = "pregenLight", at = @At("HEAD"), cancellable = true)
	private void bnb_pregenLight(CallbackInfo info) {
		info.cancel();
		for (byte i = 0; i < 16; i++) {
			float delta = i / 15F;
			this.lightCurve[i] = MathHelper.lerp(delta, 0.3F, 1.0F);
		}
	}
	
	@Override
	public Collection<Biome> getBiomes() {
		return BNBBiomes.BIOMES;
	}
	
	@Inject(method = "canSpawnOn", at = @At("HEAD"))
	private void canSpawnOn(int x, int z, CallbackInfoReturnable<Boolean> info) {
		Chunk chunk = level.getChunk(x, z);
		if (chunk instanceof BNBWorldChunk worldChunk) {
			BNBChunkStatus status = worldChunk.bnb_getStatus();
			while (status == BNBChunkStatus.EMPTY) {
				try {
					Thread.sleep(10);
				}
				catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
				status = worldChunk.bnb_getStatus();
			}
		}
	}
}
