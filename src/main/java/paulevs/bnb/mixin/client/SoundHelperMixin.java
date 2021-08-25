package paulevs.bnb.mixin.client;

import net.minecraft.class_267;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.sound.SoundHelper;
import net.minecraft.util.SoundMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.block.sound.NetherSoundSource;
import paulscode.sound.SoundSystem;

@Mixin(SoundHelper.class)
public class SoundHelperMixin {
	private static final NetherSoundSource SOURCE = new NetherSoundSource();
	
	@Shadow
	private static SoundSystem soundSystem;
	@Shadow
	private static boolean field_2673;
	@Shadow
	private GameOptions gameOptions;
	@Shadow
	private SoundMap soundMapMusic;
	@Shadow // Sound source ID
	private int field_2671;
	
	@Inject(method = "playSound", at = @At("HEAD"), cancellable = true)
	private void bnb_playSound(String name, float x, float y, float z, float volume, float pitch, CallbackInfo info) {
		if (field_2673 && this.gameOptions.sound > 0 && volume > 0) {
			class_267 sound = this.soundMapMusic.method_958(name);
			if (sound == null && name.startsWith(BetterNetherBeta.MOD_ID)) {
				sound = SOURCE.getSound(name);
				if (sound != null) {
					this.field_2671 = (this.field_2671 + 1) % 256;
					String sourceName = "sound_" + this.field_2671;
					float soundVolume = 16.0F;
					if (volume > 1.0F) {
						soundVolume *= volume;
					}

					soundSystem.newSource(volume > 1.0F, sourceName, sound.field_2127, sound.field_2126, false, x, y, z, 2, soundVolume);
					soundSystem.setPitch(sourceName, pitch);
					if (volume > 1.0F) {
						volume = 1.0F;
					}

					soundSystem.setVolume(sourceName, volume * this.gameOptions.sound);
					soundSystem.play(sourceName);
				}
				
				info.cancel();
			}
		}
	}
}
