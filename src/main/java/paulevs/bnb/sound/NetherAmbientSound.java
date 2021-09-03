package paulevs.bnb.sound;

import net.minecraft.class_267;
import paulevs.bnb.BetterNetherBeta;

public class NetherAmbientSound {
	private final String path;
	private class_267 sound;
	
	public NetherAmbientSound(String name) {
		path = String.format("%s.sound.ambient.%s", BetterNetherBeta.MOD_ID, name);
	}
	
	public class_267 getSound(NetherSoundSource source) {
		if (sound == null) {
			sound = source.getSoundDirectly(path);
		}
		return sound;
	}
}
