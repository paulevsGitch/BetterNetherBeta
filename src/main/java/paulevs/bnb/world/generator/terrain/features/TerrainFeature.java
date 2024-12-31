package paulevs.bnb.world.generator.terrain.features;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.world.generator.terrain.TerrainSDF;

import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.io.File;
import java.io.IOException;
import java.util.Random;

public abstract class TerrainFeature implements TerrainSDF {
	protected static final float PI_HALF = (float) (Math.PI * 0.5);
	protected static final float PI = (float) Math.PI;
	protected static final Random RANDOM = new Random(0);
	
	public abstract void setSeed(int seed);
	
	protected float gradient(float y, float minY, float maxY, float minValue, float maxValue) {
		if (y <= minY) return minValue;
		if (y >= maxY) return maxValue;
		return MathHelper.lerp((y - minY) / (maxY - minY), minValue, maxValue);
	}
	
	protected float gradient(float y, float minY, float maxY, float minValue, float midValue, float maxValue) {
		float midY = MathHelper.lerp(0.5F, minY, maxY);
		return Math.max(
			gradient(y, minY, midY, minValue, midValue),
			gradient(y, midY, maxY, midValue, maxValue)
		);
	}
	
	protected float smoothMax(float a, float b, float k) {
		return -smoothMin(-a, -b, k);
	}
	
	protected float smoothMin(float a, float b, float k) {
		float h = Math.max(k - Math.abs(a - b), 0.0F) / k;
		return Math.min(a, b) - h * h * k * 0.25F;
	}
	
	public void debugImage() {
		BufferedImage buffer = new BufferedImage(1024, 512, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) (buffer.getRaster().getDataBuffer())).getData();
		int lavaHeight = 96;
		
		for (int x = 0; x < 1024; x++) {
			for (int y = 0; y < 256; y++) {
				int color = getDensity(x, 255 - y, 0) > 0.5F ? 255 : 0;
				if (color == 0 && (255 - y) < lavaHeight) {
					pixels[y * 1024 + x] = Color.RED.getRGB();
					continue;
				}
				pixels[y * 1024 + x] = 255 << 24 | color << 16 | color << 8 | color;
			}
			for (int z = 0; z < 256; z++) {
				pixels[(z + 256) * 1024 + x] = Color.RED.getRGB();
				for (int y = 128; y > lavaHeight; y--) {
					if (getDensity(x, y, z) > 0.5F) {
						pixels[(z + 256) * 1024 + x] = 255 << 24 | y << 16 | y << 8 | y;
						break;
					}
				}
			}
		}
		
		// Added to specifically avoid StAPI JFrame issue
		try {
			ImageIO.write(buffer, "png", new File("./debug.png"));
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
		
		/*JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(buffer)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		frame.repaint();*/
	}
}
