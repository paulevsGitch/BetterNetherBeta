package paulevs.bnb;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;

public class BNB {
	public static final ModID MOD_ID = ModID.of("bnb");
	
	public static Identifier id(String name) {
		return MOD_ID.id(name);
	}
}
