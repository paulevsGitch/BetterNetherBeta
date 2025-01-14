package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.render.Frustum;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec2I;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import org.lwjgl.opengl.GL11;
import paulevs.bnb.BNBClient;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

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
	private static final LocalHeightCache NEAR_CACHE = new LocalHeightCache();
	private static final LocalHeightCache FAR_CACHE = new LocalHeightCache();
	private static final LocalBoolCache PUDDLES_CACHE = new LocalBoolCache();
	private static final float PI2 = (float) (Math.PI * 2.0);
	private static final float[] SMOKE_COLOR = new float[3];
	private static final float[] HSV = new float[3];
	private static final float[] RANDOM_OFFSET;
	private static final float[] SMOKE_RANDOM;
	private static final Vec2I[] SMOKE_OFFSETS;
	private static final Thread CACHE_UPDATE;
	
	private static volatile boolean canRun = true;
	private static float[] smokeDensity;
	private static int smokeDensityWidth;
	private static int smokeDensityHeight;
	private static float weatherDelta;
	private static WeatherType prevWeather;
	private static WeatherType weather;
	private static int rainTexture;
	private static int smokeTexture;
	private static int lavaPuddleTexture;
	private static Frustum frustum;
	private static byte rainRadius;
	private static byte innerRadius;
	private static byte puddlesRadius;
	
	public static void updateTextures(TextureManager manager) {
		rainTexture = manager.getTextureId("/assets/bnb/stationapi/textures/environment/lava_rain.png");
		smokeTexture = manager.getTextureId("/assets/bnb/stationapi/textures/environment/smoke.png");
		lavaPuddleTexture = manager.getTextureId("/assets/bnb/stationapi/textures/environment/lava_puddle.png");
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, smokeTexture);
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
		frustum = (Frustum) Frustum.getInstance();
		updateWeather(delta);
		Vec3D cameraPos = getPosition(minecraft.viewEntity);
		renderSmoke(minecraft, delta, cameraPos);
		if (prevWeather == WeatherType.RAIN || weather == WeatherType.RAIN) {
			renderRain(minecraft, delta, cameraPos);
		}
		if (prevWeather == WeatherType.DRIZZLE || weather == WeatherType.DRIZZLE) {
			renderDrizzle(minecraft, delta, cameraPos);
		}
	}
	
	public static void updateFog(float[] fogColor) {
		if (isCurrentWeather(WeatherType.DRIZZLE)) {
			float alpha = getIntensity(WeatherType.DRIZZLE);
			fogColor[0] = MathHelper.lerp(alpha * 0.25F, fogColor[0], 0.5F);
			fogColor[1] = MathHelper.lerp(alpha * 0.25F, fogColor[1], 0.01F);
			fogColor[2] = MathHelper.lerp(alpha * 0.25F, fogColor[2], 0.0F);
		}
		if (isCurrentWeather(WeatherType.RAIN)) {
			float alpha = getIntensity(WeatherType.RAIN);
			fogColor[0] = MathHelper.lerp(alpha, fogColor[0], 0.5F);
			fogColor[1] = MathHelper.lerp(alpha, fogColor[1], 0.01F);
			fogColor[2] = MathHelper.lerp(alpha, fogColor[2], 0.0F);
		}
		
		int r = (int) (fogColor[0] * 255);
		int g = (int) (fogColor[1] * 255);
		int b = (int) (fogColor[2] * 255);
		Color.RGBtoHSB(r, g, b, HSV);
		HSV[0] += 0.03F;
		if (HSV[0] > 1.0F) HSV[0] -= 1.0F;
		HSV[1] *= 0.9F;
		HSV[2] = 1.0F;
		int rgb = Color.HSBtoRGB(HSV[0], HSV[1], HSV[2]);
		SMOKE_COLOR[0] = ((rgb >> 16) & 255) / 255F;
		SMOKE_COLOR[1] = ((rgb >> 8) & 255) / 255F;
		SMOKE_COLOR[2] = (rgb & 255) / 255F;
	}
	
	public static float getFogDensity() {
		if (prevWeather == null) return 1.0F;
		return MathHelper.lerp(
			weatherDelta,
			prevWeather.fogIntensity,
			weather.fogIntensity
		);
	}
	
	public static boolean isCurrentWeather(WeatherType type) {
		return prevWeather == type || weather == type;
	}
	
	public static float getIntensity(WeatherType type) {
		if (type == prevWeather && type == weather) return 1.0F;
		if (type != prevWeather && type != weather) return 0.0F;
		return prevWeather != type ? weatherDelta : 1.0F - weatherDelta;
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
	
	private static void renderSmoke(Minecraft minecraft, float delta, Vec3D cameraPos) {
		Entity entity = minecraft.viewEntity;
		
		float smokeTime = ((int) (minecraft.level.getLevelTime() % 24000) + delta) / 24000.0F * 30.0F * PI2;
		
		double ex = MathHelper.lerp(delta, entity.prevRenderX, entity.x);
		double ey = MathHelper.lerp(delta, entity.prevRenderY, entity.y);
		double ez = MathHelper.lerp(delta, entity.prevRenderZ, entity.z);
		
		int cx = MCMath.floor(entity.x / 32.0);
		int cz = MCMath.floor(entity.z / 32.0);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, smokeTexture);
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_FOG);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
		GL11.glDepthMask(false);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.setOffset(-ex, -ey, -ez);
		tessellator.start();
		
		final int y1 = 96;
		final int y2 = 250;
		float intensity = MathHelper.lerp(getIntensity(WeatherType.CLEAR), 0.1F, 0.5F);
		
		for (Vec2I offset : SMOKE_OFFSETS) {
			int sx = cx + offset.x;
			int sz = cz + offset.z;
			
			int px = sx << 5;
			int pz = sz << 5;
			
			float fx = (float) (ex - px);
			float fz = (float) (ez - pz);
			float l = fx * fx + fz * fz;
			float alpha;
			
			if (l < 0.001F) continue;
			
			l = MCMath.sqrt(l) * 2.0F;
			fx /= l;
			fz /= l;
			float v = fx;
			fx = -fz;
			fz = v;
			alpha = l / 512.0F;
			alpha = alpha < 0.625F ? alpha * 1.75F - 0.125F : -2.666F * alpha + 2.666F;
			alpha *= intensity * getSmokeDensity(sx, sz);
			if (alpha < 0.01F) continue;
			
			int randomIndex = ((sx & 15) << 4 | (sz & 15)) * 7;
			float offsetY = MCMath.sin(SMOKE_RANDOM[randomIndex++] + smokeTime) * 32.0F;
			float offsetX = SMOKE_RANDOM[randomIndex++];
			float offsetZ = SMOKE_RANDOM[randomIndex++];
			float scaleH = SMOKE_RANDOM[randomIndex++];
			float u1 = SMOKE_RANDOM[randomIndex++];
			float u2 = SMOKE_RANDOM[randomIndex++];
			float v1 = SMOKE_RANDOM[randomIndex];
			float v2 = 1.0F - v1;
			
			float scale = 40.0F * scaleH;
			double x1 = px + offsetX + fx * scale;
			double x2 = px + offsetX - fx * scale;
			double z1 = pz + offsetZ + fz * scale;
			double z2 = pz + offsetZ - fz * scale;
			double py1 = y1 + offsetY;
			double py2 = y2 + offsetY;
			
			if (areIsInvisible(cameraPos, x1, py1, z1, x2, py2, z2)) continue;
			
			tessellator.color(SMOKE_COLOR[0], SMOKE_COLOR[1], SMOKE_COLOR[2], alpha);
			
			tessellator.vertex(x1, py1, z1, u1, v2);
			tessellator.vertex(x1, py2, z1, u1, v1);
			tessellator.vertex(x2, py2, z2, u2, v1);
			tessellator.vertex(x2, py1, z2, u2, v2);
		}
		
		tessellator.render();
		tessellator.setOffset(0.0, 0.0, 0.0);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glEnable(GL11.GL_FOG);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
		GL11.glDepthMask(true);
	}
	
	private static void renderDrizzle(Minecraft minecraft, float delta, Vec3D cameraPos) {
		LivingEntity entity = minecraft.viewEntity;
		double x = MathHelper.lerp(delta, entity.prevRenderX, entity.x);
		double y = MathHelper.lerp(delta, entity.prevRenderY, entity.y);
		double z = MathHelper.lerp(delta, entity.prevRenderZ, entity.z);
		
		int ix = MCMath.floor(entity.x);
		int iy = MCMath.floor(entity.y);
		int iz = MCMath.floor(entity.z);
		
		Level level = minecraft.level;
		int rainTop = level.getTopY();
		
		if (iy - rainTop > 40) return;
		
		float vOffset = (float) (((double) level.getLevelTime() + delta) * 0.03 % 1.0);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, rainTexture);
		
		tessellator.start();
		float intensity = getIntensity(WeatherType.DRIZZLE);
		if (isCurrentWeather(WeatherType.RAIN)) intensity = 1.0F;
		tessellator.color(1F, 1F, 1F, intensity);
		tessellator.setOffset(-x, -y, -z);
		
		for (byte dx = (byte) -rainRadius; dx <= rainRadius; dx++) {
			int wx = ix + dx;
			int lx = wx & 3;
			for (byte dz = (byte) -rainRadius; dz <= rainRadius; dz++) {
				int wz = iz + dz;
				int lz = wz & 3;
				if ((lx != 0 || lz != 0) && (lx != 2 || lz != 2)) continue;
				renderDrizzleSection(wx, wz, cameraPos, tessellator, vOffset);
			}
		}
		
		tessellator.render();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, lavaPuddleTexture);
		vOffset = (float) (((double) level.getLevelTime() + delta) * 0.03);
		
		tessellator.start();
		
		for (byte dx = (byte) -puddlesRadius; dx <= puddlesRadius; dx++) {
			int wx = ix + dx;
			int lx = wx & 3;
			for (byte dz = (byte) -puddlesRadius; dz <= puddlesRadius; dz++) {
				int wz = iz + dz;
				int lz = wz & 3;
				if ((lx != 0 || lz != 0) && (lx != 2 || lz != 2)) continue;
				renderLavaPuddles(wx, wz, cameraPos, tessellator, vOffset, puddlesRadius, intensity);
			}
		}
		
		tessellator.render();
		tessellator.setOffset(0.0, 0.0, 0.0);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
	}
	
	private static void renderRain(Minecraft minecraft, float delta, Vec3D cameraPos) {
		LivingEntity entity = minecraft.viewEntity;
		double x = MathHelper.lerp(delta, entity.prevRenderX, entity.x);
		double y = MathHelper.lerp(delta, entity.prevRenderY, entity.y);
		double z = MathHelper.lerp(delta, entity.prevRenderZ, entity.z);
		
		int ix = MCMath.floor(entity.x);
		int iz = MCMath.floor(entity.z);
		
		Level level = minecraft.level;
		
		float vOffset = (float) (((double) level.getLevelTime() + delta) * 0.03 % 1.0);
		
		Tessellator tessellator = Tessellator.INSTANCE;
		
		GL11.glDisable(GL11.GL_CULL_FACE);
		GL11.glNormal3f(0.0F, 1.0F, 0.0F);
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.01F);
		GL11.glColor4f(1F, 1F, 1F, 1F);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, rainTexture);
		
		tessellator.start();
		float intensity = getIntensity(WeatherType.RAIN);
		tessellator.color(1F, 1F, 1F, intensity);
		tessellator.setOffset(-x, -y, -z);
		
		for (byte dx = (byte) -rainRadius; dx <= rainRadius; dx++) {
			int wx = (ix & -4) + (dx << 2);
			for (byte dz = (byte) -rainRadius; dz <= rainRadius; dz++) {
				if (Math.abs(dx) < innerRadius && Math.abs(dz) < innerRadius) continue;
				int wz = (iz & -4) + (dz << 2);
				renderLargeSection( wx, wz, cameraPos, tessellator, vOffset);
			}
		}
		
		for (byte dx = (byte) -rainRadius; dx <= rainRadius; dx++) {
			int wx = ix + dx;
			for (byte dz = (byte) -rainRadius; dz <= rainRadius; dz++) {
				int wz = iz + dz;
				if (((wx + wz) & 1) == 0) continue;
				renderNormalSection( wx, wz, cameraPos, tessellator, vOffset);
			}
		}
		
		tessellator.render();
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, lavaPuddleTexture);
		vOffset = (float) (((double) level.getLevelTime() + delta) * 0.03);
		
		tessellator.start();
		
		for (byte dx = (byte) -puddlesRadius; dx <= puddlesRadius; dx++) {
			int wx = ix + dx;
			for (byte dz = (byte) -puddlesRadius; dz <= puddlesRadius; dz++) {
				int wz = iz + dz;
				renderLavaPuddles(wx, wz, cameraPos, tessellator, vOffset, rainRadius, intensity);
			}
		}
		
		tessellator.render();
		tessellator.setOffset(0.0, 0.0, 0.0);
		
		GL11.glEnable(GL11.GL_CULL_FACE);
		GL11.glDisable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.1f);
	}
	
	private static void renderLargeSection(int x, int z, Vec3D pos, Tessellator tessellator, float vOffset) {
		short[] y12 = FAR_CACHE.getData(x >> 2, z >> 2);
		short y1 = y12[0];
		short y2 = y12[1];
		if (y1 == Short.MAX_VALUE || y2 == y1) return;
		if (areIsInvisible(pos, x - 2, y1, z - 2, x + 3, y2, z + 3)) return;
		
		float v1 = RANDOM_OFFSET[(x & 15) << 4 | (z & 15)] - vOffset;
		float v2 = (y2 - y1) * 0.0625F + v1;
		
		float u1 = ((x + z) & 3) * 0.25F;
		float u2 = u1 + 0.25F;
		
		float dx = (float) (pos.x - (x + 0.5));
		float dz = (float) (pos.z - (z + 0.5));
		float l = dx * dx + dz * dz;
		if (l > 0) {
			l = MCMath.sqrt(l) / 0.5F;
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
	
	private static void renderNormalSection(int x, int z, Vec3D pos, Tessellator tessellator, float vOffset) {
		short[] y12 = NEAR_CACHE.getData(x, z);
		short y1 = y12[0];
		short y2 = y12[1];
		if (y1 == Short.MAX_VALUE || y2 == y1) return;
		if (areIsInvisible(pos, x, y1, z, x + 1, y2, z + 1)) return;
		
		float v1 = RANDOM_OFFSET[(x & 15) << 4 | (z & 15)] - vOffset;
		float v2 = (y2 - y1) * 0.0625F + v1;
		
		float u1 = ((x + z) & 3) * 0.25F;
		float u2 = u1 + 0.25F;
		
		float dx = (float) (pos.x - (x + 0.5));
		float dz = (float) (pos.z - (z + 0.5));
		float l = dx * dx + dz * dz;
		if (l > 0) {
			l = MCMath.sqrt(l) / 0.5F;
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
	
	private static void renderLavaPuddles(int x, int z, Vec3D pos, Tessellator tessellator, float vOffset, float radius, float intensity) {
		if (!PUDDLES_CACHE.getData(x, z)) return;
		short[] y12 = NEAR_CACHE.getData(x, z);
		short y1 = y12[0];
		short y2 = y12[1];
		if (y1 == Short.MAX_VALUE || y2 == y1) return;
		if (areIsInvisible(pos, x, y1 + 1, z, x + 1, y1 + 1, z + 1)) return;
		
		float dx = (float) (x - pos.x);
		float dy = (float) (y1 - pos.y) * 0.5F;
		float dz = (float) (z - pos.z);
		float alpha = 1F - MCMath.sqrt(dx * dx + dy * dy + dz * dz) / radius;
		alpha = alpha * 4F * intensity;
		if (alpha <= 0.01F) return;
		if (alpha > 1F) alpha = 1F;
		
		int randomIndex = (x & 15) << 4 | (z & 15);
		float randomOffsetV = RANDOM_OFFSET[randomIndex];
		
		vOffset += randomOffsetV;
		float delta = (vOffset) % 1.0F * 4.0F;
		if (delta > 2.0F) return;
		
		int tableOffset = MCMath.floor(vOffset) * 3;
		
		float randomOffsetX = RANDOM_OFFSET[(randomIndex + 17 + tableOffset) & 255] * 0.625F - 0.3125F;
		float randomOffsetZ = RANDOM_OFFSET[(randomIndex + 13 + tableOffset) & 255] * 0.625F - 0.3125F;
		
		float v1 = MCMath.floor(Math.min(delta * 5.0, 4.0F)) * 0.2F;
		float v2 = v1 + 0.2F;
		
		alpha *= 1.0F - delta * 0.5F;
		tessellator.color(1.0F, 1.0F, 1.0F, alpha);
		
		float scale = Math.min(delta, 1.0F);
		if (scale < 0.01F) return;
		
		float scaleMin = MathHelper.lerp(scale, 0.5F, 0.375F);
		float scaleMax = MathHelper.lerp(scale, 0.5F, 0.625F);
		float px = Math.round((x + randomOffsetX) * 16.0F) * 0.0625F;
		float pz = Math.round((z + randomOffsetZ) * 16.0F) * 0.0625F;
		float x1 = px + scaleMin;
		float x2 = px + scaleMax;
		float z1 = pz + scaleMin;
		float z2 = pz + scaleMax;
		float h = y1 + 1.01F;
		
		tessellator.vertex(x1, h, z1, 0.0F, v1);
		tessellator.vertex(x1, h, z2, 0.0F, v2);
		tessellator.vertex(x2, h, z2, 1.0F, v2);
		tessellator.vertex(x2, h, z1, 1.0F, v1);
	}
	
	private static void renderDrizzleSection(int x, int z, Vec3D pos, Tessellator tessellator, float vOffset) {
		short[] y12 = NEAR_CACHE.getData(x, z);
		short y1 = y12[0];
		short y2 = y12[1];
		if (y1 == Short.MAX_VALUE || y2 == y1) return;
		if (areIsInvisible(pos, x, y1, z, x + 1, y2, z + 1)) return;
		
		int index = (x & 15) << 4 | (z & 15);
		float v1 = RANDOM_OFFSET[index] - vOffset;
		float v2 = (y2 - y1) * 0.0625F + v1;
		
		float u1 = ((x + z) & 3) * 0.25F;
		float u2 = u1 + 0.25F;
		
		float du = Math.round(RANDOM_OFFSET[(index + 13) & 255] * 16) * 0.0625F;
		u1 = u1 * 0.25F + du;
		u2 = u2 * 0.25F + du;
		
		float dx = (float) (pos.x - (x + 0.5));
		float dz = (float) (pos.z - (z + 0.5));
		float l = dx * dx + dz * dz;
		if (l > 0) {
			l = MCMath.sqrt(l) / 0.5F;
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
		
		double x1 = x + 0.5 + dx * 0.25F;
		double x2 = x + 0.5 - dx * 0.25F;
		double z1 = z + 0.5 + dz * 0.25F;
		double z2 = z + 0.5 - dz * 0.25F;
		
		tessellator.vertex(x1, y1, z1, u1, v2);
		tessellator.vertex(x1, y2, z1, u1, v1);
		tessellator.vertex(x2, y2, z2, u2, v1);
		tessellator.vertex(x2, y1, z2, u2, v2);
	}
	
	private static Vec3D getPosition(LivingEntity entity) {
		return Vec3D.getFromCacheAndSet(entity.x, entity.y, entity.z);
	}
	
	private static boolean areIsInvisible(Vec3D cameraPos, double x1, double y1, double z1, double x2, double y2, double z2) {
		x1 -= cameraPos.x;
		y1 -= cameraPos.y;
		z1 -= cameraPos.z;
		x2 -= cameraPos.x;
		y2 -= cameraPos.y;
		z2 -= cameraPos.z;
		return !frustum.isInside(x1, y1, z1, x2, y2, z2);
	}
	
	private static void updateCache() {
		while (canRun) {
			try {
				//noinspection BusyWait
				Thread.sleep(100);
			}
			catch (InterruptedException e) {
				throw new RuntimeException(e);
			}
			
			Minecraft minecraft = BNBClient.getMinecraft();
			
			if (minecraft == null || minecraft.level == null || minecraft.level.dimension.id != -1) continue;
			if (!isCurrentWeather(WeatherType.RAIN) && !isCurrentWeather(WeatherType.DRIZZLE)) continue;
			
			rainRadius = minecraft.options.fancyGraphics ? (byte) 10 : (byte) 5;
			puddlesRadius = minecraft.options.fancyGraphics ? (byte) 15 : (byte) 7;
			innerRadius = (byte) ((rainRadius >> 1) - 1);
			
			LivingEntity entity = minecraft.viewEntity;
			if (entity == null) continue;
			
			int ix = MCMath.floor(entity.x);
			int iz = MCMath.floor(entity.z);
			
			for (byte dx = (byte) -puddlesRadius; dx <= puddlesRadius; dx++) {
				int wxn = ix + dx;
				for (byte dz = (byte) -puddlesRadius; dz <= puddlesRadius; dz++) {
					int wzn = iz + dz;
					Chunk chunk = minecraft.level.getChunk(wxn, wzn);
					short max = BNBWeatherManager.getWeatherTop(chunk, wxn & 15, wzn & 15);
					short min = BNBWeatherManager.getWeatherBottom(chunk, wxn & 15, max, wzn & 15);
					NEAR_CACHE.setData(wxn, wzn, min, max);
					
					Block block = chunk.getBlockState(wxn & 15, min, wzn & 15).getBlock();
					PUDDLES_CACHE.setData(wxn, wzn, block.isFullCube());
				}
			}
			
			for (byte dx = (byte) -rainRadius; dx <= rainRadius; dx++) {
				int wxf = (ix & -4) + (dx << 2);
				for (byte dz = (byte) -rainRadius; dz <= rainRadius; dz++) {
					if (Math.abs(dx) < innerRadius && Math.abs(dz) < innerRadius) continue;
					int wzf = (iz & -4) + (dz << 2);
					Chunk chunk = minecraft.level.getChunk(wxf, wzf);
					short max = BNBWeatherManager.getWeatherTop(chunk, wxf & 15, wzf & 15);
					short min = BNBWeatherManager.getWeatherBottom(chunk, wxf & 15, max, wzf & 15);
					FAR_CACHE.setData(wxf >> 2, wzf >> 2, min, max);
				}
			}
		}
	}
	
	public static void stop() {
		canRun = false;
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
			float u1 = random.nextBoolean() ? 0.0F : 0.5F;
			float u2 = u1 + 0.5F;
			
			if (random.nextBoolean()) {
				float u = u1;
				u1 = u2;
				u2 = u;
			}
			
			SMOKE_RANDOM[i] = random.nextFloat() * PI2;
			SMOKE_RANDOM[i + 1] = random.nextFloat() * 16.0F - 8.0F;
			SMOKE_RANDOM[i + 2] = random.nextFloat() * 16.0F - 8.0F;
			SMOKE_RANDOM[i + 3] = random.nextFloat() * 0.8F + 0.8F;
			SMOKE_RANDOM[i + 4] = u1;
			SMOKE_RANDOM[i + 5] = u2;
			SMOKE_RANDOM[i + 6] = random.nextBoolean() ? 1.0F : 0.0F;
		}
		
		CACHE_UPDATE = new Thread(BNBWeatherRenderer::updateCache);
		CACHE_UPDATE.setName("BNB Weather Cache");
		CACHE_UPDATE.start();
	}
}
