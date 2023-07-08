package paulevs.bnb.mixin.server;

import net.minecraft.server.ServerPlayerConnectionManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(ServerPlayerConnectionManager.class)
public class ServerPlayerConnectionManagerMixin {
	@ModifyConstant(method = "sendToOppositeDimension", constant = @Constant(doubleValue = 8.0))
	private double bnb_changeNetherScale(double value) {
		return 1.0;
	}
}
