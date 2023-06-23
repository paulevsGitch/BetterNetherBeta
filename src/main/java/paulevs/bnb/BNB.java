package paulevs.bnb;

import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.registry.ModID;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BNB {
	public static final ModID MOD_ID = ModID.of("bnb");
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static Identifier id(String name) {
		return MOD_ID.id(name);
	}
}
