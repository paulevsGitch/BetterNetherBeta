package paulevs.bnb.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class ClientUtil {
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
	
	public static boolean isFancyGraphics() {
		return getMinecraft().options.fancyGraphics;
	}
}
