package paulevs.bnb.mixin.common;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.bnb.world.biome.BNBBiomeData;

import java.util.Random;

@Mixin(Biome.class)
public class BiomeMixin implements BNBBiomeData {
	@Unique private Identifier bnb_biomeAmbience;
	@Unique private int bnb_minIndex = -1;
	@Unique private int bnb_indexRange;
	
	@Override
	public Biome bnb_setBiomeAmbience(Identifier ambienceID) {
		bnb_biomeAmbience = ambienceID;
		return Biome.class.cast(this);
	}
	
	@Override
	public Identifier bnb_getBiomeAmbience() {
		return bnb_biomeAmbience;
	}
	
	@Override
	public Biome bnb_setParticleRange(int minIndex, int maxIndex) {
		bnb_minIndex = minIndex;
		bnb_indexRange = maxIndex - minIndex + 1;
		return Biome.class.cast(this);
	}
	
	@Override
	public int bnb_getParticleTexture(Random random) {
		if (bnb_minIndex == -1) return -1;
		return random.nextInt(bnb_indexRange) + bnb_minIndex;
	}
}
