package paulevs.bnb.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundEntry;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class BNBClientSounds {
	private static final Map<Identifier, SoundEntry> SOUNDS = new HashMap<>();
	
	public static SoundEntry getSound(Identifier id) {
		if (id == null) return null;
		return SOUNDS.computeIfAbsent(id, key -> {
			String path = "assets/" + key.namespace + "/stationapi/sounds/" + key.path + ".ogg";
			URL url = BNB.getURL(path);
			if (url == null) {
				BNB.LOGGER.warn("Sound " + path + " is missing!");
				return null;
			}
			return new SoundEntry(path, url);
		});
	}
	
	private static final SoundEntry[] MUSIC = new SoundEntry[] {
		getSound(BNB.id("music/conner_crow_phlogiston")),
		getSound(BNB.id("music/conner_crow_glowstone_lullaby")),
		getSound(BNB.id("music/conner_crow_bravais"))
	};
	
	private static final byte[] MUSIC_INDEX_DATA = new byte[MUSIC.length];
	private static byte musicIndex;
	
	static {
		for (byte i = 0; i < MUSIC.length; i++) {
			MUSIC_INDEX_DATA[i] = i;
		}
		shuffleMusic(new Random());
	}
	
	public static SoundEntry getRandomMusic(Random random) {
		if (MUSIC.length == 1) return MUSIC[MUSIC_INDEX_DATA[0]];
		if (musicIndex == MUSIC.length) {
			byte value = MUSIC_INDEX_DATA[musicIndex - 1];
			shuffleMusic(random);
			while (MUSIC_INDEX_DATA[0] == value) {
				shuffleMusic(random);
			}
			musicIndex = 0;
		}
		return MUSIC[MUSIC_INDEX_DATA[musicIndex++]];
	}
	
	private static void shuffleMusic(Random random) {
		for (byte i = 0; i < MUSIC.length; i++) {
			byte i2 = (byte) random.nextInt(MUSIC.length);
			byte value = MUSIC_INDEX_DATA[i];
			MUSIC_INDEX_DATA[i] = MUSIC_INDEX_DATA[i2];
			MUSIC_INDEX_DATA[i2] = value;
		}
	}
}
