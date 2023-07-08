package paulevs.bnb.sound;

import net.minecraft.block.BlockSounds;
import net.minecraft.client.sound.SoundEntry;

import java.net.URL;
import java.util.Random;

public class BNBSounds {
	public static final BlockSounds NYLIUM_BLOCK = new NetherBlockSound("nylium", 1.0F, 1.0F);
	
	public static final SoundEntry NETHER_FOREST_AMBIENCE = getSound("ambient/nether_forest");
	public static final SoundEntry DEEP_DARK_AMBIENCE = getSound("ambient/deep_dark");
	public static final SoundEntry BAMBOO_FOREST_AMBIENCE = getSound("ambient/bamboo_forest");
	public static final SoundEntry GRASSLANDS_AMBIENCE = getSound("ambient/grasslands");
	public static final SoundEntry SWAMPLAND_AMBIENCE = getSound("ambient/nether_bog");
	
	private static final SoundEntry[] MUSIC = new SoundEntry[] {
		getSound("music/faultlines-asher_fulero"),
		getSound("music/glimpsing_infinity-asher_fulero"),
		getSound("music/lament_of_the_ancients-asher_fulero"),
		getSound("music/sinister_cathedral-asher_fulero"),
		getSound("music/an_excuse_to_do_less_not_more-patches"),
		getSound("music/those_things_are_more_fun_with_other_people-patches")
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
	
	private static SoundEntry getSound(String name) {
		String path = "assets/bnb/stationapi/sounds/" + name + ".ogg";
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		return new SoundEntry(path, url);
	}
}
