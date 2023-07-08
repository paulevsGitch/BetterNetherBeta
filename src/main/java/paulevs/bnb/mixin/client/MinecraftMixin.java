package paulevs.bnb.mixin.client;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@ModifyConstant(method = "switchDimension", constant = @Constant(doubleValue = 8.0))
	private double bnb_changeNetherScale(double value) {
		return 1.0;
	}
}
