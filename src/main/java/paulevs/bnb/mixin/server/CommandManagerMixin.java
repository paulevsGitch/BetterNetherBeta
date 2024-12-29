package paulevs.bnb.mixin.server;

import net.minecraft.server.command.Command;
import net.minecraft.server.command.CommandManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.command.BNBCommandManager;

@Mixin(CommandManager.class)
public class CommandManagerMixin {
	@Inject(method = "processCommand", at = @At("HEAD"), cancellable = true)
	private void bnb_processCommand(Command command, CallbackInfo info) {
		if (BNBCommandManager.processServer(command)) info.cancel();
	}
}
