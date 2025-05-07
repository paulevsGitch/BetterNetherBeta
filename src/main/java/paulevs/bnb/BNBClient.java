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
		depthBlend = heightBlend((float) minecraft.viewEntity.y);
	}
	
	public static float getDepthBlend() {
		return depthBlend;
	}
	
	public static float getLight(float light, int y) {
		if (y > 80) return light;
		float light2 = light * light;
		return MathHelper.lerp(heightBlend(y), light, light2);
	}
	
	private static float heightBlend(float y) {
		return MathHelper.clamp((80 - y) / 32.0F, 0.0F, 1.0F);
	}
}
