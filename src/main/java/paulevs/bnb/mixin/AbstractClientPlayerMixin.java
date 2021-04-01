package paulevs.bnb.mixin;

import org.lwjgl.input.Keyboard;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.AbstractClientPlayer;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import paulevs.bnb.tab.CreativeScreen;

@Mixin(AbstractClientPlayer.class)
public abstract class AbstractClientPlayerMixin extends PlayerBase {
	@Shadow
	protected Minecraft minecraft;
	
	public AbstractClientPlayerMixin(Level arg) {
		super(arg);
	}

	@Inject(method = "method_136", at = @At("HEAD"))
	public void bnb_openCreativeTab(int i, boolean flag, CallbackInfo info) {
		if (this.isAlive() && i == Keyboard.KEY_G) {
			AbstractClientPlayer player = (AbstractClientPlayer) (Object) this;
			minecraft.openScreen(new CreativeScreen(player.inventory));
		}
	}
}
