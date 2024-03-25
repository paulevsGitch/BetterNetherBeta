package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.StationRenderAPI;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlas;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import paulevs.bnb.weather.BNBWeatherRenderer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;
import java.nio.IntBuffer;

@Environment(EnvType.CLIENT)
public class PortalCompassTexture {
	private static final IntBuffer PIXELS = BufferUtils.createIntBuffer(256);
	private static final int[] BACKGROUND = new int[256];
	private static int x;
	private static int y;
	
	public static void updateSprite(Atlas.Sprite sprite) {
		x = sprite.getX();
		y = sprite.getY();
		
		try {
			InputStream stream = BNBWeatherRenderer.class.getResourceAsStream(
				"/assets/bnb/stationapi/textures/item/portal_compass.png"
			);
			BufferedImage img = ImageIO.read(stream);
			int width = img.getWidth();
			int height = img.getHeight();
			if (width != 16 || height != 16) {
				throw new RuntimeException("Portal compass texture is not 16x16");
			}
			img.getRGB(0, 0, 16, 16, BACKGROUND, 0, 16);
			stream.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		/*for (short i = 0; i < 256; i++) {
			int rgba = BACKGROUND[i];
			int a = ((rgba >> 24) & 255) << 8;
			int b = ((rgba >> 8) & 255) << 24;
			BACKGROUND[i] = rgba & 0x00FF00FF | a | b;
		}*/
	}
	
	public static void updateForItem(ItemStack stack) {
		System.out.println("Update " + stack);
		PIXELS.put(BACKGROUND);
		int abgr = stack.hashCode() | (255 << 24);
		PIXELS.put(0, abgr);
		PIXELS.put(1, abgr);
		PIXELS.put(16, abgr);
		PIXELS.put(17, abgr);
		PIXELS.position(0);
		StationRenderAPI.getBakedModelManager().getAtlas(Atlases.GAME_ATLAS_TEXTURE).bindTexture();
		GL11.glTexSubImage2D(GL11.GL_TEXTURE_2D, 0, x, y, 16, 16, GL11.GL_RGBA, GL11.GL_UNSIGNED_BYTE, PIXELS);
	}
}
