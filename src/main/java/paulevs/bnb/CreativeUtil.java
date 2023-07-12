package paulevs.bnb;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerBase;
import paulevs.bhcreative.BHCreative;

public class CreativeUtil {
	private static final boolean NOT_INSTALLED = !FabricLoader.getInstance().isModLoaded("bhcreative");
	
	public static boolean isCreative(PlayerBase player) {
		if (NOT_INSTALLED) return false;
		return BHCreative.isInCreative(player);
	}
}
