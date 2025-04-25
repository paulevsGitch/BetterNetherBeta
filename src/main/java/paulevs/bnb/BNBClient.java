package paulevs.bnb;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.modificationstation.stationapi.api.util.math.MathHelper;

@Environment(EnvType.CLIENT)
public class BNBClient {
	private static float depthBlend;
	
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
	
	public static void updateDepthBlend() {
		Minecraft minecraft = getMinecraft();
		depthBlend = MathHelper.clamp((float) (80 - minecraft.viewEntity.y) / 32.0F, 0.0F, 1.0F);
	}
	
	public static float getDepthBlend() {
		return depthBlend;
	}
}
