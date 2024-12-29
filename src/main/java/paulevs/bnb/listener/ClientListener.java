package paulevs.bnb.listener;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.minecraft.block.Block;
import net.minecraft.client.resource.language.I18n;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.model.LoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.client.render.model.UnbakedModel;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.achievement.BNBAchievementPage;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.SoulSandstoneTexturedBlock;
import paulevs.bnb.block.SpinningWheelBlock;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.command.BNBCommands;
import paulevs.bnb.entity.CrimsonSpiderEntity;
import paulevs.bnb.entity.ObsidianBoatEntity;
import paulevs.bnb.entity.PirozenSpiderEntity;
import paulevs.bnb.entity.PoisonSpiderEntity;
import paulevs.bnb.entity.renderer.NetherSpiderRenderer;
import paulevs.bnb.entity.renderer.ObsidianBoatRenderer;
import paulevs.bnb.gui.container.SpinningWheelContainer;
import paulevs.bnb.gui.screen.SpinningWheelScreen;
import paulevs.bnb.item.PortalCompassItem;
import paulevs.bnb.rendering.BNBConnectedTextures;
import paulevs.bnb.rendering.BNBWeatherRenderer;
import paulevs.bnb.rendering.LavaRenderer;
import paulevs.bnb.rendering.OBJModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

@Environment(EnvType.CLIENT)
public class ClientListener {
	private static final Gson GSON = new GsonBuilder().create();
	
	@EventListener
	public void onGUIRegister(GuiHandlerRegistryEvent event) {
		Registry.register(GuiHandlerRegistry.INSTANCE, SpinningWheelBlock.GUI_ID, new GuiHandler(
			(player, inventory, packet) -> new SpinningWheelScreen(
				new SpinningWheelContainer(player.inventory, (SpinningWheelBlockEntity) inventory
			)),
			() -> null
		));
	}
	
	@EventListener
	public void onTextureRegister(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		
		Block.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		Block.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		Block.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
		Block.GRAVEL.texture = blockAtlas.addTexture(BNB.id("block/gravel")).index;
		
		LavaRenderer.flowTexture = blockAtlas.addTexture(BNB.id("block/lava_flow")).index;
		for (byte i = 0; i < 16; i++) {
			Identifier id = BNB.id("block/lava_still_" + i);
			LavaRenderer.STILL_TEXTURES[i] = blockAtlas.addTexture(id).index;
		}
		
		SoulSandstoneTexturedBlock.TEXTURES[0] = blockAtlas.addTexture(BNB.id("block/soul_sandstone_top")).index;
		SoulSandstoneTexturedBlock.TEXTURES[1] = blockAtlas.addTexture(BNB.id("block/soul_sandstone_bottom")).index;
		SoulSandstoneTexturedBlock.TEXTURES[2] = blockAtlas.addTexture(BNB.id("block/soul_sandstone_side")).index;
		
		Direction[] direction = Direction.values();
		BNBConnectedTextures.add4SideTextures(BNBBlocks.FALURIAN_MOSS_BLOCK, BNB.id("block/falurian_moss_side"), direction);
		BNBConnectedTextures.add4SideTextures(BNBBlocks.PIROZEN_MOSS_BLOCK, BNB.id("block/pirozen_moss_side"), direction);
		BNBConnectedTextures.add4SideTextures(BNBBlocks.OBSIDIAN_GRAVEL, BNB.id("block/obsidian_gravel_side"), direction);
		BNBConnectedTextures.add4SideTextures(Block.GRAVEL, BNB.id("block/gravel_side"), direction);
		BNBConnectedTextures.add4SideTextures(BNBBlocks.MAROON_NYLIUM, BNB.id("block/maroon_nylium_side"), Direction.UP);
		BNBConnectedTextures.add4SideTextures(BNBBlocks.TURQUOISE_NYLIUM, BNB.id("block/turquoise_nylium_side"), Direction.UP);
		BNBConnectedTextures.add4SideTextures(BNBBlocks.POISON_NYLIUM, BNB.id("block/poison_nylium_side"), Direction.UP);
		BNBConnectedTextures.add4SideTextures(BNBBlocks.GRAPE_NYLIUM, BNB.id("block/grape_nylium_side"), Direction.UP);
		
		BNBBlocks.UPDATE_TEXTURE_SINGLE.forEach(block -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			if (id != null) {
				block.texture = blockAtlas.addTexture(BNB.id("block/" + id.path)).index;
			}
		});
		
		BNBBlocks.UPDATE_TEXTURE_INTERFACE.forEach(update -> update.updateTextures(blockAtlas));
		
		BNBAchievementPage.getInstance().updateTextures(blockAtlas);
		BNBWeatherRenderer.updateTextures(BNBClient.getMinecraft().textureManager);
		
		ExpandableAtlas itemAtlas = Atlases.getGuiItems();
		
		for (byte i = 0; i < 64; i++) {
			PortalCompassItem.TEXTURES[i] = itemAtlas.addTexture(BNB.id("item/portal_compass_" + i)).index;
		}
		
		debugTerrain();
		printTranslations();
	}
	
	@EventListener
	public void onModelLoad(LoadUnbakedModelEvent event) throws IOException {
		if (event.identifier.namespace != BNB.NAMESPACE) return;
		if (!event.identifier.path.startsWith("block/")) return;
		
		if (event.identifier.path.contains("moss_cover")) {
			UnbakedModel model = event.model;
		}
		
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
		event.renderers.put(CrimsonSpiderEntity.class, new NetherSpiderRenderer("falurian_spider_e"));
		event.renderers.put(PirozenSpiderEntity.class, new NetherSpiderRenderer("pirozen_spider_e"));
		event.renderers.put(PoisonSpiderEntity.class, new NetherSpiderRenderer("poison_spider_e"));
		event.renderers.put(ObsidianBoatEntity.class, new ObsidianBoatRenderer());
	}
	
	@EventListener
	public void onInit(InitEvent event) {
		BNBCommands.registerClient();
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
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		
		/*TerrainMap map = BNBWorldGenerator.getMapCopy();
		BufferedImage buffer = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				TerrainRegion region = map.getRegion(x << 2, z << 2);
				int color = switch (region) {
					case OCEAN_NORMAL -> 0xFFFF0000;
					case OCEAN_MOUNTAINS -> 0xFFFF3333;
					case SHORE_NORMAL -> 0xFFFFFF00;
					case SHORE_MOUNTAINS -> 0xFFFFFF00;
					case PLAINS -> 0xFF333333;
					case HILLS -> 0xFFcccccc;
					case MOUNTAINS -> 0xFFFFFFFF;
					case BRIDGES -> 0xFFFF00FF;
				};
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
		
		/*BNBWorldGenerator g = new BNBWorldGenerator();
		buffer = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				//int index = terrainMap.getSDFIndex(x, z);
				//System.out.println(index);
				Identifier id = BNBWorldGenerator.TERRAIN_MAP.getData(x << 2, z << 2);
				System.out.println(id);
				buffer.setRGB(x, z, id.hashCode() | 0xFF000000);
			}
		}
		
		frame = new JFrame();
		frame.add(new JLabel(new ImageIcon(buffer)));
		frame.setResizable(false);
		frame.pack();
		frame.setLocationRelativeTo(null);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);*/
		
		/*long t = System.currentTimeMillis();
		TerrainFeature feature = new StraightThinPillarsFeature();
		feature.setSeed(2);
		feature.debugImage();
		t = System.currentTimeMillis() - t;
		System.out.println("\n\nF: " + t + "\n\n");*/
		
		/*NetherBiome[] biomes = new NetherBiome[] {
			BNBBiomes.FALURIAN_FOREST,
			BNBBiomes.PIROZEN_FOREST,
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
				builder.append(name);
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
				builder.append(name);
				builder.append("=");
				builder.append(fastTranslate(name));
				builder.append("\n");
			}
		});
		if (builder.isEmpty()) return;
		BNB.LOGGER.info("Item Translations\n================\n" + builder + "================");
	}
	
	private String fastTranslate(String name) {
		int index1 = name.indexOf(".", name.indexOf(".") + 1) + 1;
		int index2 = name.indexOf(".", index1);
		char[] data = name.substring(index1, index2).toCharArray();
		data[0] = Character.toUpperCase(data[0]);
		for (int i = 1; i < data.length - 1; i++) {
			if (data[i] == '_') {
				data[i] = ' ';
				data[i + 1] = Character.toUpperCase(data[i + 1]);
			}
		}
		String result = new String(data);
		if (result.endsWith(" Half") || result.endsWith(" Full")) {
			result = result.substring(0, result.lastIndexOf(' '));
		}
		return result;
	}
}
