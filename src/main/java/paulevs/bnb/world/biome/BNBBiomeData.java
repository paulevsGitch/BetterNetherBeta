package paulevs.bnb.world.biome;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Util;

import java.util.Random;

public interface BNBBiomeData {
	default Biome bnb_setBiomeAmbience(Identifier ambienceID) {
		return Util.assertImpl();
	}
	
	default Identifier bnb_getBiomeAmbience() {
		return Util.assertImpl();
	}
	
	default Biome bnb_setParticleRange(int minIndex, int maxIndex) {
		return Util.assertImpl();
	}
	
	default int bnb_getParticleTexture(Random random) {
		return Util.assertImpl();
	}
}
