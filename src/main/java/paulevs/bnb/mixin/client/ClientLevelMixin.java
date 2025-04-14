package paulevs.bnb.mixin.client;

import net.minecraft.client.level.ClientLevel;
import net.minecraft.client.network.ClientPlayerPacketHandler;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.DimensionData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.world.BNBWorldGenerator;
import paulevs.bnb.world.biome.BNBBiomeSource;

@Mixin(ClientLevel.class)
public abstract class ClientLevelMixin extends Level {
	public ClientLevelMixin(DimensionData dimensionData, String name, Dimension dimension, long seed) {
		super(dimensionData, name, dimension, seed);
	}
	
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_onClientLevelInit(ClientPlayerPacketHandler handler, long seed, int dimensionID, CallbackInfo info) {
		if (dimensionID != -1) return;
		BNBWorldGenerator.updateData(seed);
		dimension.biomeSource = new BNBBiomeSource(seed);
	}
}
