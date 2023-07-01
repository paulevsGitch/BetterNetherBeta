package paulevs.bnb.sound;

import net.minecraft.block.BlockSounds;
import net.minecraft.client.sound.SoundEntry;

import java.net.URL;

public class NetherSounds {
	public static final BlockSounds NYLIUM_BLOCK = new NetherBlockSound("nylium", 1.0F, 1.0F);
	
	/*public static final NetherAmbientSound NETHER_FOREST_AMBIENCE = new NetherAmbientSound("nether_forest");
	public static final NetherAmbientSound DEEP_DARK_AMBIENCE = new NetherAmbientSound("deep_dark");
	public static final NetherAmbientSound BAMBOO_FOREST_AMBIENCE = new NetherAmbientSound("bamboo_forest");
	public static final NetherAmbientSound GRASSLANDS_AMBIENCE = new NetherAmbientSound("grasslands");
	public static final NetherAmbientSound SWAMPLAND_AMBIENCE = new NetherAmbientSound("nether_bog");*/
	
	public static final SoundEntry[] MUSIC = new SoundEntry[] {
		getSound("music/faultlines-asher_fulero"),
		getSound("music/glimpsing_infinity-asher_fulero"),
		getSound("music/lament_of_the_ancients-asher_fulero"),
		getSound("music/sinister_cathedral-asher_fulero")
	};
	
	private static SoundEntry getSound(String name) {
		String path = "assets/bnb/stationapi/sounds/" + name + ".ogg";
		URL url = Thread.currentThread().getContextClassLoader().getResource(path);
		return new SoundEntry(path, url);
	}
}
