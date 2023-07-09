package paulevs.bnb.sound;

import net.minecraft.block.BlockSounds;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;

public class BNBSounds {
	public static final BlockSounds NYLIUM_BLOCK = new NetherBlockSound("nylium", 1.0F, 1.0F);
	
	public static final Identifier NETHER_FOREST_AMBIENCE = BNB.id("ambient/nether_forest");
	public static final Identifier DEEP_DARK_AMBIENCE = BNB.id("ambient/deep_dark");
	public static final Identifier BAMBOO_FOREST_AMBIENCE = BNB.id("ambient/bamboo_forest");
	public static final Identifier GRASSLANDS_AMBIENCE = BNB.id("ambient/grasslands");
	public static final Identifier SWAMPLAND_AMBIENCE = BNB.id("ambient/nether_bog");
}
