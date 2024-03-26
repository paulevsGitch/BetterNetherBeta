package paulevs.bnb.weather;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec2I;
import net.minecraft.util.maths.Vec3D;
import org.lwjgl.opengl.GL11;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Environment(EnvType.CLIENT)
public class BNBWeatherRenderer {
	private static final float TO_RADIANS = (float) (Math.PI / 180);
	private static final float PI2 = (float) (Math.PI * 2.0);
	private static final float[] SMOKE_COLOR = new float[3];
	private static final float[] HSV = new float[3];
	private static final float[] RANDOM_OFFSET;
	private static final float[] SMOKE_RANDOM;
	private static final Vec2I[] SMOKE_OFFSETS;
	
	private static float[] smokeDensity;
	private static int smokeDensityWidth;
	private static int smokeDensityHeight;
	private static float weatherDelta;
	private static WeatherType prevWeather;
	private static WeatherType weather;
	private static int rainTexture;
	private static int smokeTexture1;
	private static int smokeTexture2;
	
	public static void updateTextures(TextureManager manager) {
		rainTexture = manager.getTextureId("/assets/bnb/stationapi/textures/environment/lava_rain.png");
		smokeTexture1 = manager.getTextureId("/assets/bnb/stationapi/textures/environment/smoke_1.png");
		smokeTexture2 = manager.getTextureId("/assets/bnb/stationapi/textures/environment/smoke_2.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, smokeTexture1);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, smokeTexture2);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_LINEAR);
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, 0);
		
		try {
			InputStream stream = BNBWeatherRenderer.class.getResourceAsStream(
				"/assets/bnb/stationapi/textures/environment/smoke_density.png"
			);
			BufferedImage density = ImageIO.read(stream);
			smokeDensityWidth = density.getWidth();
			smokeDensityHeight = density.getHeight();
			smokeDensity = new float[smokeDensityWidth * smokeDensityHeight];
			int index = 0;
			for (int y = 0; y < density.getHeight(); y++) {
				for (int x = 0; x < density.getWidth(); x++) {
					smokeDensity[index++] = (density.getRGB(x, y) & 255) / 255.0F;
				}
			}
			stream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	public static void render(Minecraft minecraft, float delta) {
		updateWeather(delta);
		renderSmoke(minecraft, delta);
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
		
		int r = (int) (fogColor[0] * 255);
		int g = (int) (fogColor[1] * 255);
		int b = (int) (fogColor[2] * 255);
		Color.RGBtoHSB(r, g, b, HSV);
		HSV[0] += 0.03F;
		if (HSV[0] > 1.0F) HSV[0] -= 1.0F;
		HSV[1] *= 0.9F;
		HSV[2] = 1.0F;
		//HSV[2] *= 1.5F;
		//if (HSV[2] > 1.0F) HSV[2] = 1.0F;
		int rgb = Color.HSBtoRGB(HSV[0], HSV[1], HSV[2]);
		SMOKE_COLOR[0] = ((rgb >> 16) & 255) / 255F;
		SMOKE_COLOR[1] = ((rgb >> 8) & 255) / 255F;
		SMOKE_COLOR[2] = (rgb & 255) / 255F;
		
		/*for (byte i = 0; i < 3; i++) {
			SMOKE_COLOR[i] = fogColor[i] * 1.25F;
		}*/
	}
	
	public static float getFogDensity() {
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
	
	private static int wrap(int value, int side) {
		int result = (value - value / side * side);
		return result < 0 ? result + side : result;
	}
	
	private static float getSmokeDensity(int x, int z) {
		x = wrap(x, smokeDensityWidth);
		z = wrap(z, smokeDensityHeight);
		return smokeDensity[z * smokeDensityWidth + x];
	}
	
	private static void renderSmoke(Minecraft minecraft, float delta) {
		Entity entity = minecraft.viewEntity;
		
		float smokeTime = ((int) (minecraft.level.getLevelTime() % 24000) + delta) / 24000.0F * 30.0F * PI2;
		
		double ex = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, entity.prevRenderX, entity.x);
		double ey = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, entity.prevRenderY, entity.y);
		double ez = net.modificationstation.stationapi.api.util.math.MathHelper.lerp(delta, entity.prevRenderZ, entity.z);
		
		int cx = MathHelper.floor(entity.x / 32.0);
		int cz = MathHelper.floor(entity.z / 32.0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, smokeTexture1);
		int lastTerxture = smokeTexture1;
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
		GL11.glDepthMask(false);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.start();
		tessellator.setOffset(-ex, -ey, -ez);
		
		int y1 = 32;
		int y2 = 200;
		
		for (Vec2I offset : SMOKE_OFFSETS) {
			int sx = cx + offset.x;
			int sz = cz + offset.z;
			
			int px = sx << 5;
			int pz = sz << 5;
			
			float fx = (float) (ex - px);
			float fz = (float) (ez - pz);
			float l = fx * fx + fz * fz;
			float alpha;
			if (l > 0) {
				l = MathHelper.sqrt(l) / 0.5F;
				fx /= l;
				fz /= l;
				float v = fx;
				fx = -fz;
				fz = v;
				alpha = l / 512.0F;
				alpha = alpha < 0.625F ? alpha * 1.75F - 0.125F : -2.666F * alpha + 2.666F;
				alpha *= getSmokeDensity(sx, sz);// * 0.5F;
				if (alpha < 0.01F) continue;
			}
			else {
				continue;
			}
			
			int randomIndex = ((sx & 15) << 4 | (sz & 15)) * 7;
			int texture = SMOKE_RANDOM[randomIndex++] < 1.0F ? smokeTexture1 : smokeTexture2;
			float offsetY = MathHelper.sin(SMOKE_RANDOM[randomIndex++] + smokeTime) * 32.0F;
			float offsetX = SMOKE_RANDOM[randomIndex++];
			float offsetZ = SMOKE_RANDOM[randomIndex++];
			float scaleH = SMOKE_RANDOM[randomIndex++];
			float u1 = SMOKE_RANDOM[randomIndex++];
			float v1 = SMOKE_RANDOM[randomIndex];
			
			float u2 = 1.0F - u1;
			float v2 = 1.0F - v1;
			
			float scale = 40.0F * scaleH;
			double x1 = px + offsetX + fx * scale;
			double x2 = px + offsetX - fx * scale;
			double z1 = pz + offsetZ + fz * scale;
			double z2 = pz + offsetZ - fz * scale;
			
			if (texture != lastTerxture) {
				GL11.glBindTexture(GL11.GL_TEXTURE_2D, texture);
				lastTerxture = texture;
				tessellator.render();
				tessellator.start();
			}
			
			tessellator.color(SMOKE_COLOR[0], SMOKE_COLOR[1], SMOKE_COLOR[2], alpha);
			
			tessellator.vertex(x1, y1 + offsetY, z1, u1, v2);
			tessellator.vertex(x1, y2 + offsetY, z1, u1, v1);
			tessellator.vertex(x2, y2 + offsetY, z2, u2, v1);
			tessellator.vertex(x2, y1 + offsetY, z2, u2, v2);
		}
		
		tessellator.setOffset(0.0, 0.0, 0.0);
		tessellator.render();
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glDepthMask(true);
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
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, rainTexture);
		
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
		
		List<Vec2I> offsets = new ArrayList<>();
		for (byte x = -8; x <= 8; x++) {
			for (byte z = -8; z <= 8; z++) {
				offsets.add(new Vec2I(x, z));
			}
		}
		offsets.sort((v1, v2) -> {
			int l1 = v1.x * v1.x + v1.z * v1.z;
			int l2 = v2.x * v2.x + v2.z * v2.z;
			return Integer.compare(l2, l1);
		});
		SMOKE_OFFSETS = offsets.toArray(Vec2I[]::new);
		
		SMOKE_RANDOM = new float[256 * 7];
		for (int i = 0; i < SMOKE_RANDOM.length; i += 7) {
			SMOKE_RANDOM[i] = random.nextFloat() * 2.0F;
			SMOKE_RANDOM[i + 1] = random.nextFloat() * PI2;
			SMOKE_RANDOM[i + 2] = random.nextFloat() * 16.0F - 8.0F;
			SMOKE_RANDOM[i + 3] = random.nextFloat() * 16.0F - 8.0F;
			SMOKE_RANDOM[i + 4] = random.nextFloat() * 0.8F + 0.8F;
			SMOKE_RANDOM[i + 5] = random.nextBoolean() ? 1.0F : 0.0F;
			SMOKE_RANDOM[i + 6] = random.nextBoolean() ? 1.0F : 0.0F;
		}
	}
}
