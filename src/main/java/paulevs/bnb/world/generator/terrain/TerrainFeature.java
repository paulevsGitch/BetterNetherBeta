package paulevs.bnb.world.generator.terrain;

import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.world.generator.TerrainSDF;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.Random;

public abstract class TerrainFeature implements TerrainSDF {
	protected static final float PI_HALF = (float) (Math.PI * 0.5);
	protected static final Random RANDOM = new Random(0);
	
	public abstract void setSeed(int seed);
	
	protected float gradient(float y, float minY, float maxY, float minValue, float maxValue) {
		if (y <= minY) return minValue;
		if (y >= maxY) return maxValue;
		return MathHelper.lerp((y - minY) / (maxY - minY), minValue, maxValue);
	}
	
	public void debugImage() {
		BufferedImage buffer = new BufferedImage(1024, 512, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) (buffer.getRaster().getDataBuffer())).getData();
		
		for (int x = 0; x < 1024; x++) {
			for (int y = 0; y < 256; y++) {
				int color = getDensity(x, 255 - y, 0) > 0.5F ? 255 : 0;
				if (color == 0 && (255 - y) < 32) {
					pixels[y * 1024 + x] = Color.RED.getRGB();
					continue;
				}
				pixels[y * 1024 + x] = 255 << 24 | color << 16 | color << 8 | color;
			}
			for (int z = 0; z < 256; z++) {
				pixels[(z + 256) * 1024 + x] = Color.RED.getRGB();
				for (int y = 128; y >= 31; y--) {
					if (getDensity(x, y, z) > 0.5F) {
						pixels[(z + 256) * 1024 + x] = 255 << 24 | y << 16 | y << 8 | y;
						break;
					}
				}
			}
		}
		
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(buffer)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	}
}
