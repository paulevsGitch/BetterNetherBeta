package paulevs.bnb.sound;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.sound.SoundEntry;
import net.modificationstation.stationapi.api.registry.Identifier;
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
			String path = "assets/" + key.modID + "/stationapi/sounds/" + key.id + ".ogg";
			URL url = Thread.currentThread().getContextClassLoader().getResource(path);
			if (url == null) {
				BNB.LOGGER.warn("Sound " + path + " is missing!");
				return null;
			}
			return new SoundEntry(path, url);
		});
	}
	
	private static final SoundEntry[] MUSIC = new SoundEntry[] {
		getSound(BNB.id("music/faultlines-asher_fulero")),
		getSound(BNB.id("music/glimpsing_infinity-asher_fulero")),
		getSound(BNB.id("music/lament_of_the_ancients-asher_fulero")),
		getSound(BNB.id("music/sinister_cathedral-asher_fulero")),
		getSound(BNB.id("music/an_excuse_to_do_less_not_more-patches")),
		getSound(BNB.id("music/those_things_are_more_fun_with_other_people-patches"))
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
