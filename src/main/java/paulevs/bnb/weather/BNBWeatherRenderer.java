package paulevs.bnb.weather;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3D;
import org.lwjgl.opengl.GL11;

import java.util.Random;

@Environment(EnvType.CLIENT)
public class BNBWeatherRenderer {
	private static final float TO_RADIANS = (float) (Math.PI / 180);
	private static final float[] RANDOM_OFFSET;
	private static float weatherDelta;
	private static WeatherType prevWeather;
	private static WeatherType weather;
	
	public static void render(Minecraft minecraft, float delta) {
		updateWeather(delta);
		if (prevWeather == WeatherType.LAVA_RAIN || weather == WeatherType.LAVA_RAIN) {
			renderRain(minecraft, delta);
		}
	}
	
	public static void updateFog(float[] fogColor) {
		if (isCurrentWeather(WeatherType.LAVA_RAIN)) {
			float alpha = getIntensity(WeatherType.LAVA_RAIN);
			fogColor[0] = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(alpha, fogColor[0], 0.5F);
			fogColor[1] = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(alpha, fogColor[1], 0.01F);
			fogColor[2] = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(alpha, fogColor[2], 0.0F);
		}
	}
	
	public static float getFogDensity() {
		/*if (isCurrentWeather(WeatherType.LAVA_RAIN)) return 1.0F - getIntensity(WeatherType.LAVA_RAIN) * 0.5F;
		if (isCurrentWeather(WeatherType.FOG)) return 1.0F - getIntensity(WeatherType.FOG) * 0.5F;
		return 1.0F;*/
		if (prevWeather == null) return 1.0F;
		return net.modificationstation.stationapi.api.util.math.MathHelper.lerp(
			weatherDelta,
			prevWeather.fogIntensity,
			weather.fogIntensity
		);
	}
	
	public static boolean isCurrentWeather(WeatherType type) {
		return prevWeather == type || weather == type;
	}
	
	public static float getIntensity(WeatherType type) {
		float intensity = 1.0F;
		if (prevWeather != type) intensity = weatherDelta;
		else if (weather != type) intensity = 1.0F - weatherDelta;
		return intensity;
	}
	
	private static void updateWeather(float delta) {
		if (prevWeather == null) {
			weather = BNBWeatherManager.getCurrentWeather();
			prevWeather = weather;
			weatherDelta = 0.0F;
			return;
		}
		weatherDelta = Math.min(weatherDelta + delta * 0.002F, 1.0F);
		if (weatherDelta == 1.0F) {
			prevWeather = weather;
			weather = BNBWeatherManager.getCurrentWeather();
			weatherDelta = 0.0F;
		}
	}
	
	private static void renderRain(Minecraft minecraft, float delta) {
		LivingEntity entity = minecraft.viewEntity;
		double x = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, entity.prevRenderX, entity.x);
		double y = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, entity.prevRenderY, entity.y);
		double z = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, entity.prevRenderZ, entity.z);
		
		int ix = MathHelper.floor(entity.x);
		int iy = MathHelper.floor(entity.y);
		int iz = MathHelper.floor(entity.z);
		
		int radius = minecraft.options.fancyGraphics ? 10 : 5;
		int radiusCenter = radius / 2 - 1;
		Level level = minecraft.level;
		int rainTop = level.getTopY();
		
		if (iy - rainTop > 40) return;
		
		float vOffset = (float) (((double) level.getLevelTime() + delta) * 0.03 % 1.0);
		Vec3D pos = getPosition(entity);
		Vec3D dir = getViewDirection(entity);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		int tex = minecraft.textureManager.getTextureId("/assets/bnb/stationapi/textures/environment/lava_rain.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, tex);
		
		tessellator.start();
		tessellator.color(1F, 1F, 1F, getIntensity(WeatherType.LAVA_RAIN));
		tessellator.setOffset(-x, -y, -z);
		
		for (byte dx = (byte) -radius; dx <= radius; dx++) {
			int wx = (ix & -4) + (dx << 2);
			for (byte dz = (byte) -radius; dz <= radius; dz++) {
				if (Math.abs(dx) < radiusCenter && Math.abs(dz) < radiusCenter) continue;
				int wz = (iz & -4) + (dz << 2);
				renderLargeSection(level, wx, wz, pos, dir, tessellator, vOffset);
			}
		}
		
		for (byte dx = (byte) -radius; dx <= radius; dx++) {
			int wx = ix + dx;
			for (byte dz = (byte) -radius; dz <= radius; dz++) {
				int wz = iz + dz;
				renderNormalSection(level, wx, wz, pos, dir, tessellator, vOffset);
			}
		}
		
		tessellator.setOffset(0.0, 0.0, 0.0);
		tessellator.render();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
	}
	
	private static void renderLargeSection(Level level, int x, int z, Vec3D pos, Vec3D dir, Tessellator tessellator, float vOffset) {
		int y1 = BNBWeatherManager.getWeatherTop(level, x, z);
		int y2 = BNBWeatherManager.getWeatherBottom(level, x, y1, z);
		
		if (y2 - y1 == 0) return;
		
		boolean visible = pointIsVisible(pos, dir, x + 0.5, y1, z + 0.5);
		visible |= pointIsVisible(pos, dir, x + 0.5, y1 + ((y2 - y1) >> 1), z + 0.5);
		visible |= pointIsVisible(pos, dir, x + 0.5, y2, z + 0.5);
		if (!visible) return;
		
		float v1 = RANDOM_OFFSET[(x & 15) << 4 | (z & 15)] - vOffset;
		float v2 = (y2 - y1) * 0.0625F + v1;
		
		float u1 = ((x + z) & 3) * 0.25F;
		float u2 = u1 + 0.25F;
		
		float dx = (float) (pos.x - (x + 0.5));
		float dz = (float) (pos.z - (z + 0.5));
		float l = dx * dx + dz * dz;
		if (l > 0) {
			l = MathHelper.sqrt(l) / 0.5F;
			dx /= l;
			dz /= l;
			float v = dx;
			dx = -dz;
			dz = v;
		}
		else {
			dx = 0.5F;
			dz = 0;
		}
		
		double x1 = x + 0.5 + dx;
		double x2 = x + 0.5 - dx;
		double z1 = z + 0.5 + dz;
		double z2 = z + 0.5 - dz;
		
		tessellator.vertex(x1, y1, z1, u1, v2);
		tessellator.vertex(x1, y2, z1, u1, v1);
		tessellator.vertex(x2, y2, z2, u2, v1);
		tessellator.vertex(x2, y1, z2, u2, v2);
	}
	
	private static void renderNormalSection(Level level, int x, int z, Vec3D pos, Vec3D dir, Tessellator tessellator, float vOffset) {
		int y1 = BNBWeatherManager.getWeatherTop(level, x, z);
		int y2 = BNBWeatherManager.getWeatherBottom(level, x, y1, z);
		
		if (y2 - y1 == 0) return;
		
		boolean visible = pointIsVisible(pos, dir, x + 0.5, y1, z + 0.5);
		visible |= pointIsVisible(pos, dir, x + 0.5, y1 + ((y2 - y1) >> 1), z + 0.5);
		visible |= pointIsVisible(pos, dir, x + 0.5, y2, z + 0.5);
		if (!visible) return;
		
		float v1 = RANDOM_OFFSET[(x & 15) << 4 | (z & 15)] - vOffset;
		float v2 = (y2 - y1) * 0.0625F + v1;
		
		float u1 = ((x + z) & 3) * 0.25F;
		float u2 = u1 + 0.25F;
		
		float dx = (float) (pos.x - (x + 0.5));
		float dz = (float) (pos.z - (z + 0.5));
		float l = dx * dx + dz * dz;
		if (l > 0) {
			l = MathHelper.sqrt(l) / 0.5F;
			dx /= l;
			dz /= l;
			float v = dx;
			dx = -dz;
			dz = v;
		}
		else {
			dx = 0.5F;
			dz = 0;
		}
		
		double x1 = x + 0.5 + dx;
		double x2 = x + 0.5 - dx;
		double z1 = z + 0.5 + dz;
		double z2 = z + 0.5 - dz;
		
		tessellator.vertex(x1, y1, z1, u1, v2);
		tessellator.vertex(x1, y2, z1, u1, v1);
		tessellator.vertex(x2, y2, z2, u2, v1);
		tessellator.vertex(x2, y1, z2, u2, v2);
	}
	
	private static Vec3D getPosition(LivingEntity entity) {
		return Vec3D.getFromCacheAndSet(entity.x, entity.y, entity.z);
	}
	
	private static Vec3D getViewDirection(LivingEntity entity) {
		float yaw = entity.prevYaw + (entity.yaw - entity.prevYaw);
		float pitch = entity.prevPitch + (entity.pitch - entity.prevPitch);
		
		yaw = -yaw * TO_RADIANS - (float) Math.PI;
		float cosYaw = MathHelper.cos(yaw);
		float sinYaw = MathHelper.sin(yaw);
		float cosPitch = -MathHelper.cos(-pitch * TO_RADIANS);
		
		return Vec3D.getFromCacheAndSet(
			sinYaw * cosPitch,
			(MathHelper.sin(-pitch * ((float) Math.PI / 180))),
			cosYaw * cosPitch
		);
	}
	
	private static boolean pointIsVisible(Vec3D position, Vec3D normal, double x, double y, double z) {
		return normal.x * (x - position.x) + normal.y * (y - position.y) + normal.z * (z - position.z) > 0;
	}
	
	static {
		RANDOM_OFFSET = new float[256];
		Random random = new Random(0);
		for (short i = 0; i < 256; i++) {
			RANDOM_OFFSET[i] = random.nextFloat();
		}
	}
}
