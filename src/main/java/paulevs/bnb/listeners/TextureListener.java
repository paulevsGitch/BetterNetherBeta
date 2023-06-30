package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BlockBase;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import paulevs.bnb.BNB;
import paulevs.bnb.world.generator.terrain.ArchipelagoFeature;
import paulevs.bnb.world.generator.terrain.TerrainFeature;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class TextureListener {
	@EventListener
	public void registerTextures(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		BlockBase.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		BlockBase.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		BlockBase.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
		
		BufferedImage buffer = new BufferedImage(1024, 512, BufferedImage.TYPE_INT_ARGB);
		int[] pixels = ((DataBufferInt) (buffer.getRaster().getDataBuffer())).getData();
		
		TerrainFeature feature = new ArchipelagoFeature();
		feature.setSeed(5);
		
		for (int x = 0; x < 1024; x++) {
			for (int y = 0; y < 256; y++) {
				int color = feature.getDensity(x, 255 - y, 0) > 0.5F ? 255 : 0;
				if (color == 0 && (255 - y) < 32) {
					pixels[y * 1024 + x] = Color.RED.getRGB();
					continue;
				}
				pixels[y * 1024 + x] = 255 << 24 | color << 16 | color << 8 | color;
			}
			for (int z = 0; z < 256; z++) {
				pixels[(z + 256) * 1024 + x] = Color.RED.getRGB();
				for (int y = 128; y >= 31; y--) {
					if (feature.getDensity(x, y, z) > 0.5F) {
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
