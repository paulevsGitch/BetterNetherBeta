package paulevs.bnb;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.PlayerEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.Namespace;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;

public class BNB {
	public static final Namespace NAMESPACE = Namespace.of("bnb");
	public static final Logger LOGGER = LogManager.getLogger();
	
	public static Identifier id(String name) {
		return NAMESPACE.id(name);
	}
	
	public static URL getURL(String path) {
		return Thread.currentThread().getContextClassLoader().getResource(path);
	}
	
	public static boolean isCreative(PlayerEntity player) {
		if (FabricLoader.getInstance().isModLoaded("bhcreative")) return false;
		return player.creative_isCreative();
	}
}
