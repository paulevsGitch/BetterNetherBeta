package paulevs.bnb.util;

import net.fabricmc.loader.api.FabricLoader;
import paulevs.materialexcavator.MaterialExcavator;

public class MaterialExcavatorUtil {
	private static final boolean HAS_ME = FabricLoader.getInstance().isModLoaded("materialexcavator");
	
	public static float scaleMiningSpeed(float speed) {
		if (!HAS_ME) return speed;
		return MaterialExcavator.scaleSpeed(speed);
	}
}
