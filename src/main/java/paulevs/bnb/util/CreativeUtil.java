package paulevs.bnb.util;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.player.PlayerBase;
import paulevs.creative.CreativePlayer;

public class CreativeUtil {
	private static boolean installed = FabricLoader.getInstance().isModLoaded("creative");
	
	public static boolean isCreativeInstalled() {
		return installed;
	}
	
	public static boolean isInCreative(PlayerBase player) {
		if (!isCreativeInstalled()) {
			return false;
		}
		if (player instanceof CreativePlayer) {
			return ((CreativePlayer) player).isCreative();
		}
		return false;
	}
}
