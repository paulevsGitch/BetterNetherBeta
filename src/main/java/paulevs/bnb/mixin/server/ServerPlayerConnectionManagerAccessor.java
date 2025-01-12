package paulevs.bnb.mixin.server;

import net.minecraft.server.ServerPlayerConnectionManager;
import net.minecraft.server.ServerPlayerView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayerConnectionManager.class)
public interface ServerPlayerConnectionManagerAccessor {
	@Invoker("getPlayerView")
	ServerPlayerView bnb_getPlayerView(int dimensionID);
}
