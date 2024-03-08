package paulevs.bnb.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.model.LoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import paulevs.bnb.BNB;
import paulevs.bnb.achievement.BNBAchievementPage;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.entity.CrimsonSpiderEntity;
import paulevs.bnb.entity.renderer.NetherSpiderRenderer;
import paulevs.bnb.entity.PoisonSpiderEntity;
import paulevs.bnb.entity.WarpedSpiderEntity;
import paulevs.bnb.rendering.LavaRenderer;
import paulevs.bnb.rendering.OBJModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ClientListener {
	private static final Gson GSON = new GsonBuilder().create();
	
	@EventListener
	public void onTextureRegister(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		Block.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		Block.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		Block.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
		
		LavaRenderer.flowTexture = blockAtlas.addTexture(BNB.id("block/lava_flow")).index;
		for (int i = 0; i < 16; i++) {
			Identifier id = BNB.id("block/lava_still_" + i);
			LavaRenderer.STILL_TEXTURES[i] = blockAtlas.addTexture(id).index;
		}
		
		BNBBlocks.CRIMSON_PLANKS.texture = blockAtlas.addTexture(BNB.id("block/crimson_planks")).index;
		BNBBlocks.WARPED_PLANKS.texture = blockAtlas.addTexture(BNB.id("block/warped_planks")).index;
		BNBBlocks.POISON_PLANKS.texture = blockAtlas.addTexture(BNB.id("block/poison_planks")).index;
		
		BNBAchievementPage.getInstance().updateTextures(blockAtlas);
		
		debugTerrain();
		printTranslations();
	}
	
	@EventListener
	public void onModelLoad(LoadUnbakedModelEvent event) throws IOException {
		if (event.identifier.namespace != BNB.NAMESPACE) return;
		if (!event.identifier.path.startsWith("block/")) return;
		
		InputStream stream = getAsStream(event.identifier);
		if (stream == null) return;
		
		InputStreamReader inputStreamReader = new InputStreamReader(stream);
		JsonReader jsonReader = new JsonReader(inputStreamReader);
		JsonObject obj = GSON.fromJson(jsonReader, JsonObject.class);
		jsonReader.close();
		inputStreamReader.close();
		stream.close();
		
		if (!obj.has("obj")) return;
		
		String path = obj.get("obj").getAsString();
		stream = getAsStream(path);
		if (stream == null) {
			BNB.LOGGER.warn("Missing OBJ model: " + path);
			return;
		}
		
		inputStreamReader = new InputStreamReader(stream);
		BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
		String[] data = bufferedReader.lines().filter(line -> !line.isEmpty() && !line.startsWith("#")).toArray(String[]::new);
		bufferedReader.close();
		inputStreamReader.close();
		stream.close();
		
		JsonObject texturesObj = obj.get("textures").getAsJsonObject();
		Map<String, SpriteIdentifier> textures = new HashMap<>();
		texturesObj.entrySet().forEach(entry -> {
			Identifier spriteID = Identifier.of(entry.getValue().getAsString());
			textures.put(entry.getKey(), SpriteIdentifier.of(Atlases.GAME_ATLAS_TEXTURE, spriteID));
		});
		
		Vec3f offset = Vec3f.ZERO;
		if (obj.has("offset")) {
			JsonArray preOffset = obj.get("offset").getAsJsonArray();
			offset = new Vec3f(
				preOffset.get(0).getAsFloat(),
				preOffset.get(1).getAsFloat(),
				preOffset.get(2).getAsFloat()
			);
		}
		
		event.model = new OBJModel(path, data, textures, offset);
	}
	
	@EventListener
	public void onEntityRenderRegister(EntityRendererRegisterEvent event) {
		event.renderers.put(CrimsonSpiderEntity.class, new NetherSpiderRenderer("crimson_spider_e"));
		event.renderers.put(WarpedSpiderEntity.class, new NetherSpiderRenderer("warped_spider_e"));
		event.renderers.put(PoisonSpiderEntity.class, new NetherSpiderRenderer("poison_spider_e"));
	}
	
	private InputStream getAsStream(Identifier id) {
		String path = "assets/bnb/stationapi/models/" + id.path + ".json";
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	private InputStream getAsStream(String path) {
		path = "assets/bnb/stationapi/models/" + path + ".obj";
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	// TODO remove that after release
	private void debugTerrain() {
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
	
	private void printTranslations() {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		printBlockTranslations();
		printItemTranslations();
	}
	
	private void printBlockTranslations() {
		StringBuilder builder = new StringBuilder();
		BlockRegistry.INSTANCE.forEach(block -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			if (id == null || id.namespace != BNB.NAMESPACE) return;
			String name = I18n.translate(block.getTranslatedName());
			if (name.startsWith("tile.")) {
				builder.append(name.replace("bnb:", ""));
				builder.append("=");
				builder.append(fastTranslate(name));
				builder.append("\n");
			}
		});
		if (builder.isEmpty()) return;
		BNB.LOGGER.info("Block Translations\n================\n" + builder + "================");
	}
	
	private void printItemTranslations() {
		StringBuilder builder = new StringBuilder();
		ItemRegistry.INSTANCE.forEach(item -> {
			Identifier id = ItemRegistry.INSTANCE.getId(item);
			if (id == null || id.namespace != BNB.NAMESPACE) return;
			String name = I18n.translate(item.getTranslatedName());
			if (name.startsWith("item.")) {
				builder.append(name.replace("bnb:", ""));
				builder.append("=");
				builder.append(fastTranslate(name));
				builder.append("\n");
			}
		});
		if (builder.isEmpty()) return;
		BNB.LOGGER.info("Item Translations\n================\n" + builder + "================");
	}
	
	private String fastTranslate(String name) {
		int index1 = name.indexOf(":") + 1;
		int index2 = name.indexOf(".", index1);
		char[] data = name.substring(index1, index2).toCharArray();
		data[0] = Character.toUpperCase(data[0]);
		for (int i = 1; i < data.length - 1; i++) {
			if (data[i] == '_') {
				data[i] = ' ';
				data[i + 1] = Character.toUpperCase(data[i + 1]);
			}
		}
		return new String(data);
	}
}
