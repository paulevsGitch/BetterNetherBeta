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
	@Unique private int bnb_particleMinIndex = -1;
	@Unique private int bnb_particleIndexRange;
	@Unique private boolean bnb_particleEmissive;
	
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
	public Biome bnb_setParticleProperties(int minIndex, int maxIndex, boolean emissive) {
		bnb_particleMinIndex = minIndex;
		bnb_particleIndexRange = maxIndex - minIndex + 1;
		bnb_particleEmissive = emissive;
		return Biome.class.cast(this);
	}
	
	@Override
	public int bnb_getParticleTexture(Random random) {
		if (bnb_particleMinIndex == -1) return -1;
		return random.nextInt(bnb_particleIndexRange) + bnb_particleMinIndex;
	}
	
	@Override
	public boolean bnb_getParticleEmissive() {
		return bnb_particleEmissive;
	}
}
