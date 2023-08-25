package paulevs.bnb.mixin.client;

import net.minecraft.client.gui.screen.menu.MainMenuScreen;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.sound.BNBSoundManager;

@Mixin(MainMenuScreen.class)
public class MainMenuScreenMixin {
	@Inject(method = "<init>", at = @At("TAIL"))
	private void bnb_onInit(CallbackInfo info) {
		BNBSoundManager.setInTheNether(false);
	}
}
