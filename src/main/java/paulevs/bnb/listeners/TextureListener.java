package paulevs.bnb.listeners;

import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.BaseBlock;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;

public class TextureListener {
	@EventListener
	public void registerTextures(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		BaseBlock.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		BaseBlock.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		BaseBlock.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
		
		BNBBlocks.CRIMSON_PLANKS.texture = blockAtlas.addTexture(BNB.id("block/crimson_planks")).index;
		
		// TODO remove that after release
		/*if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			TerrainFeature feature = new DoubleBridgesFeature();
			feature.setSeed(2);
			//feature.debugImage();
		}*/
		
		/*NetherBiome[] biomes = new NetherBiome[] {
			BNBBiomes.CRIMSON_FOREST,
			BNBBiomes.WARPED_FOREST,
			BNBBiomes.POISON_FOREST,
			new NetherBiome(BNB.id("b")),
			new NetherBiome(BNB.id("b")),
			new NetherBiome(BNB.id("b"))
		};
		BiomeMap map = new BiomeMap(biomes);
		
		int scale = 1;
		BufferedImage buffer = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				NetherBiome biome = map.getBiome((x) * scale, (z) * scale);
				int color = biome.hashCode() | 255 << 24;
				buffer.setRGB(x, z, color);
			}
		}
		
		JFrame frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(buffer)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);*/
	}
}
