package paulevs.bnb.mixin.common;

import net.minecraft.level.biome.Biome;
import net.modificationstation.stationapi.api.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.bnb.world.biome.BNBBiomeData;

@Mixin(Biome.class)
public class BiomeMixin implements BNBBiomeData {
	@Unique private Identifier bnb_biomeAmbience;
	
	@Override
	public Biome bnb_setBiomeAmbience(Identifier ambienceID) {
		bnb_biomeAmbience = ambienceID;
		return Biome.class.cast(this);
	}
	
	@Override
	public Identifier bnb_getBiomeAmbience() {
		return bnb_biomeAmbience;
	}
}
