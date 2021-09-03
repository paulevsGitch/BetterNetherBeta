package paulevs.bnb.util;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.ParticleBase;
import paulevs.bnb.BetterNetherBeta;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class ClientUtil {
	private static final Random RANDOM = new Random();
	private static final boolean GLSL = FabricLoader.getInstance().isModLoaded("glsl");
	
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
	
	public static boolean isFancyGraphics() {
		return getMinecraft().options.fancyGraphics;
	}
	
	public static Random getRandom() {
		return RANDOM;
	}
	
	public static boolean haveShaders() {
		return GLSL;
	}
	
	public static int getTextureID(String texture) {
		return getMinecraft().textureManager.getTextureId("/assets/" + BetterNetherBeta.MOD_ID + "/textures/" + texture + ".png");
	}
	
	public static void addParticle(ParticleBase particle) {
		getMinecraft().particleManager.addParticle(particle);
	}
}
