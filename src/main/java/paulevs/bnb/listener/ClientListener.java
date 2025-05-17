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
import net.minecraft.item.DyeItem;
import net.minecraft.level.BlockView;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.client.event.color.block.BlockColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.color.item.ItemColorsRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.entity.EntityRendererRegisterEvent;
import net.modificationstation.stationapi.api.client.event.render.model.LoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.event.texture.TextureRegisterEvent;
import net.modificationstation.stationapi.api.client.gui.screen.GuiHandler;
import net.modificationstation.stationapi.api.client.registry.GuiHandlerRegistry;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.client.texture.atlas.ExpandableAtlas;
import net.modificationstation.stationapi.api.event.mod.InitEvent;
import net.modificationstation.stationapi.api.event.registry.GuiHandlerRegistryEvent;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.registry.ItemRegistry;
import net.modificationstation.stationapi.api.registry.Registry;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import net.modificationstation.stationapi.api.world.BlockStateView;
import net.modificationstation.stationapi.impl.worldgen.BiomeColorsImpl;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.achievement.BNBAchievementPage;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.crafting.SpinningWheelBlock;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.block.fluid.SulphuricAcidFlowingBlock;
import paulevs.bnb.block.fluid.SulphuricAcidStillBlock;
import paulevs.bnb.block.stone.AmetrineBlock;
import paulevs.bnb.block.stone.SoulSandstoneTexturedBlock;
import paulevs.bnb.command.BNBCommandManager;
import paulevs.bnb.entity.ChlorophateSpiderEntity;
import paulevs.bnb.entity.FalurianSpiderEntity;
import paulevs.bnb.entity.ObsidianBoatEntity;
import paulevs.bnb.entity.PirozenSpiderEntity;
import paulevs.bnb.entity.renderer.NetherSpiderRenderer;
import paulevs.bnb.entity.renderer.ObsidianBoatRenderer;
import paulevs.bnb.gui.container.SpinningWheelContainer;
import paulevs.bnb.gui.screen.SpinningWheelScreen;
import paulevs.bnb.item.NetherHygrometerItem;
import paulevs.bnb.item.PortalCompassItem;
import paulevs.bnb.noise.FloatNoise;
import paulevs.bnb.noise.PerlinNoise;
import paulevs.bnb.rendering.BNBSkyRenderer;
import paulevs.bnb.rendering.BNBWeatherRenderer;
import paulevs.bnb.rendering.LavaRenderer;
import paulevs.bnb.rendering.OBJModel;
import paulevs.bnb.util.ColorUtil;
import paulevs.bnb.world.biome.BNBBiomeSource;
import paulevs.bnb.world.biome.BNBBiomes;
import paulevs.bnb.world.terrain.TerrainMap;
import paulevs.bnb.world.terrain.TerrainRegion;
import paulevs.bnb.world.terrain.features.RiversFeature;
import paulevs.bnb.world.terrain.features.TerrainFeature;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.ToIntBiFunction;

@Environment(EnvType.CLIENT)
public class ClientListener {
	private static final Gson GSON = new GsonBuilder().create();
	
	public static int netherrackAshTexture;
	public static int ashTexture;
	
	@EventListener
	public void onGUIRegister(GuiHandlerRegistryEvent event) {
		Registry.register(GuiHandlerRegistry.INSTANCE, SpinningWheelBlock.GUI_ID, new GuiHandler(
			(player, inventory, packet) -> new SpinningWheelScreen(
				new SpinningWheelContainer(player.inventory, (SpinningWheelBlockEntity) inventory
			)), SpinningWheelBlockEntity::new
		));
	}
	
	@EventListener
	public void onTextureRegister(TextureRegisterEvent event) {
		final ExpandableAtlas blockAtlas = Atlases.getTerrain();
		
		netherrackAshTexture = blockAtlas.addTexture(BNB.id("block/netherrack_ash")).index;
		ashTexture = blockAtlas.addTexture(BNB.id("block/ash")).index;
		
		Block.NETHERRACK.texture = blockAtlas.addTexture(BNB.id("block/netherrack")).index;
		Block.GLOWSTONE.texture = blockAtlas.addTexture(BNB.id("block/glowstone")).index;
		Block.SOUL_SAND.texture = blockAtlas.addTexture(BNB.id("block/soul_sand")).index;
		SulphuricAcidStillBlock.texture = blockAtlas.addTexture(BNB.id("block/sulphuric_acid_still")).index;
		SulphuricAcidFlowingBlock.texture = blockAtlas.addTexture(BNB.id("block/sulphuric_acid_flowing")).index;
		
		LavaRenderer.flowTexture = blockAtlas.addTexture(BNB.id("block/lava_flow")).index;
		for (byte i = 0; i < 16; i++) {
			Identifier id = BNB.id("block/lava_still_" + i);
			LavaRenderer.STILL_TEXTURES[i] = blockAtlas.addTexture(id).index;
			id = BNB.id("block/ametrine_" + i);
			AmetrineBlock.TEXTURES[i] = blockAtlas.addTexture(id).index;
		}
		
		SoulSandstoneTexturedBlock.TEXTURES[0] = blockAtlas.addTexture(BNB.id("block/soul_sandstone_top")).index;
		SoulSandstoneTexturedBlock.TEXTURES[1] = blockAtlas.addTexture(BNB.id("block/soul_sandstone_bottom")).index;
		SoulSandstoneTexturedBlock.TEXTURES[2] = blockAtlas.addTexture(BNB.id("block/soul_sandstone_side")).index;
		
		BNBBlocks.UPDATE_TEXTURE_INTERFACE.forEach(update -> update.updateTextures(blockAtlas));
		
		BNBAchievementPage.getInstance().updateTextures(blockAtlas);
		BNBWeatherRenderer.updateTextures(BNBClient.getMinecraft().textureManager);
		
		ExpandableAtlas itemAtlas = Atlases.getGuiItems();
		
		for (byte i = 0; i < 64; i++) {
			PortalCompassItem.TEXTURES[i] = itemAtlas.addTexture(BNB.id("item/portal_compass_" + i)).index;
		}
		
		for (byte i = 0; i < 32; i++) {
			NetherHygrometerItem.TEXTURES[i] = itemAtlas.addTexture(BNB.id("item/nether_hygrometer_" + i)).index;
		}
		
		BNBSkyRenderer.init(BNBClient.getMinecraft().textureManager);
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
		event.renderers.put(FalurianSpiderEntity.class, new NetherSpiderRenderer("falurian_spider_e"));
		event.renderers.put(PirozenSpiderEntity.class, new NetherSpiderRenderer("pirozen_spider_e"));
		event.renderers.put(ChlorophateSpiderEntity.class, new NetherSpiderRenderer("chlorophate_spider_e"));
		event.renderers.put(ObsidianBoatEntity.class, new ObsidianBoatRenderer());
	}
	
	@EventListener
	public void onInit(InitEvent event) {
		BNBCommandManager.registerClient();
		printTranslations();
		//debugTerrain();
		biomeColors();
	}
	
	@EventListener
	public void onBlockColorsRegister(BlockColorsRegisterEvent event) {
		final FloatNoise noiseR = new PerlinNoise();
		final FloatNoise noiseG = new PerlinNoise();
		final FloatNoise noiseB = new PerlinNoise();
		
		noiseR.setSeed(13);
		noiseG.setSeed(17);
		noiseB.setSeed(19);
		
		final ToIntBiFunction<BlockView, BlockPos> colorVariation = (level, pos) -> {
			int color = BiomeColorsImpl.GRASS_INTERPOLATOR.getColor(level.getBiomeSource(), pos.x, pos.z);
			
			if (pos.y < 80 && level.getBiomeSource() instanceof BNBBiomeSource source) {
				float delta = MathHelper.clamp((80 - pos.y) / 32.0F, 0.0F, 1.0F);
				int deepColor = BNBBiomes.DEEP_NETHER.getGrassColor().getColor(source, pos.x, pos.z);
				color = ColorUtil.blend(color, deepColor, delta);
			}
			
			double px = pos.x * 0.1;
			double py = pos.y * 0.1;
			double pz = pos.z * 0.1;
			
			float nr = noiseR.get(px, py, pz) * 0.4F + 0.6F;
			float ng = noiseG.get(px, py, pz) * 0.4F + 0.6F;
			float nb = noiseB.get(px, py, pz) * 0.4F + 0.6F;
			
			return ColorUtil.multiply(color, nr, ng, nb);
		};
		
		event.blockColors.registerColorProvider(
			(state, world, pos, tintIndex) -> {
				if (tintIndex != 0 || world == null || pos == null) return 0xFFFFFFFF;
				return colorVariation.applyAsInt(world, pos);
			},
			BNBBlocks.NETHERRACK_MYCORRUM, BNBBlocks.SOUL_MYCORRUM, BNBBlocks.MOSSY_NETHERRACK, BNBBlocks.MOSSY_HARDENED_NETHERRACK
		);
		
		event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
			if (world == null || pos == null) return 0xFF9A4545;
			
			int rnd = (int) MathHelper.hashCode(pos.x, pos.y, pos.z);
			
			BlockStateView view = (BlockStateView) world;
			state = view.getBlockState(pos.x, pos.y - 1, pos.z);
			if (state.isOf(Block.NETHERRACK) || state.isOf(BNBBlocks.HARDENED_NETHERRACK)) {
				float delta = (rnd & 7) / 7.0F;
				return ColorUtil.blend(0xFF9A4545, 0xFFC03939, delta);
			}
			
			int color = colorVariation.applyAsInt(world, pos);
			
			int dr = ((rnd) & 15) - 7;
			int dg = ((rnd >> 5) & 15) - 7;
			int db = ((rnd >> 10) & 15) - 7;
			
			int cr = MathHelper.clamp(ColorUtil.getR(color) + dr, 0, 255);
			int cg = MathHelper.clamp(ColorUtil.getG(color) + dg, 0, 255);
			int cb = MathHelper.clamp(ColorUtil.getB(color) + db, 0, 255);
			
			return ColorUtil.getRGB(cr, cg, cb);
		}, BNBBlocks.NETHER_SPROUTS);
		
		event.blockColors.registerColorProvider((state, level, pos, tintIndex) -> {
			if (tintIndex == -1 || level == null || pos == null) return 0xFFFFFFFF;
			int color = colorVariation.applyAsInt(level, pos);
			
			if (tintIndex == 0) {
				float[] hsv = ColorUtil.toHSV(color);
				hsv[0] -= 0.02F;
				hsv[2] *= 0.85F;
				color = ColorUtil.fromHSV(hsv);
			}
			else {
				float[] hsv = ColorUtil.toHSV(color);
				hsv[0] += 0.1F;
				hsv[1] *= 0.85F;
				hsv[2] = Math.min(hsv[2] * 2.0F, 1.0F);
				color = ColorUtil.fromHSV(hsv);
			}
			
			if (tintIndex == 1 && pos.y < 80 && level.getBiomeSource() instanceof BNBBiomeSource) {
				float delta = MathHelper.clamp((80 - pos.y) / 32.0F, 0.0F, 1.0F);
				color = ColorUtil.blend(color, 0xFF17A1A4, delta);
			}
			
			return color;
		}, BNBBlocks.NETHER_MOSS_COVER);
		
		event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> {
			if (world == null || pos == null) return 0xFFFFFFFF;
			int color = colorVariation.applyAsInt(world, pos);
			
			float[] hsv = ColorUtil.toHSV(color);
			hsv[0] -= 0.005F;
			hsv[2] *= 0.925F;
			
			return ColorUtil.fromHSV(hsv);
		}, BNBBlocks.NETHER_MOSS_BLOCK);
		
		int index = BNBBlocks.BLOCKS_WITH_ITEMS.indexOf(BNBBlocks.QUARTZ_GLASS_BLACK);
		for (byte i = 0; i < 16; i++) {
			final int color = i == 7 ? 0xC0C0C0 : DyeItem.COLORS[i];
			Block block = BNBBlocks.BLOCKS_WITH_ITEMS.get(index + i);
			event.blockColors.registerColorProvider((state, world, pos, tintIndex) -> color, block);
		}
	}
	
	@EventListener
	public void onItemColorsRegister(ItemColorsRegisterEvent event) {
		event.itemColors.register(
			(stack, tintIndex) -> tintIndex == 0 ? 0xFFC03939 : 0xFFFFFFFF,
			BNBBlocks.NETHERRACK_MYCORRUM, BNBBlocks.MOSSY_NETHERRACK, BNBBlocks.MOSSY_HARDENED_NETHERRACK
		);
		event.itemColors.register((stack, tintIndex) -> tintIndex == 0 ? Color.CYAN.getRGB() : 0xFFFFFFFF, BNBBlocks.SOUL_MYCORRUM);
		event.itemColors.register((stack, tintIndex) -> 0xFFB02921, BNBBlocks.NETHER_SPROUTS);
		event.itemColors.register((stack, tintIndex) -> 0xFFC03939, BNBBlocks.NETHER_MOSS_BLOCK);
		int index = BNBBlocks.BLOCKS_WITH_ITEMS.indexOf(BNBBlocks.QUARTZ_GLASS_BLACK);
		for (byte i = 0; i < 16; i++) {
			final int color = i == 7 ? 0xC0C0C0 : DyeItem.COLORS[i];
			Block block = BNBBlocks.BLOCKS_WITH_ITEMS.get(index + i);
			event.itemColors.register((stack, tintIndex) -> color, block);
		}
	}
	
	/*@EventListener
	public void onTooltipBuilding(TooltipBuildEvent event) {
		if (event.itemStack.getType() instanceof BlockItem blockItem && blockItem instanceof CustomTooltipProvider provider) {
			System.out.println("Tooltip!");
			String[] tooltip = provider.getTooltip(event.itemStack, "");
			event.tooltip.addAll(Arrays.asList(tooltip));
		}
		// if (event.itemStack.getType() instanceof BlockItem item) {
		// 	if (item.getBlock() instanceof CustomTooltipProvider provider) {
		// 		event.textManager.drawMultilineText(
		// 			String.join("\n", provider.getTooltip(event.itemStack, event.originalTooltip)),
		// 			event.mouseX,
		// 			event.mouseY,
		// 			10,
		// 			0xFFFFFFFF
		// 		);
		// 	}
		// }
	}*/
	
	private InputStream getAsStream(Identifier id) {
		String path = "assets/bnb/stationapi/models/" + id.path + ".json";
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	private InputStream getAsStream(String path) {
		path = "assets/bnb/stationapi/models/" + path + ".obj";
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	private void printTranslations() {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		try {
			File file = new File("../src/main/resources/assets/bnb/stationapi/lang/en_US.lang");
			if (!file.exists()) return;
			Path path = file.toPath();
			List<String> lines = Files.readAllLines(path);
			int size = lines.size();
			addBlockTranslations(lines);
			addItemTranslations(lines);
			if (size != lines.size()) {
				Collections.sort(lines);
				Files.writeString(path, String.join("\n", lines), StandardOpenOption.WRITE);
			}
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	private void addBlockTranslations(List<String> lines) {
		StringBuffer buffer = new StringBuffer(256);
		BlockRegistry.INSTANCE.forEach(block -> {
			Identifier id = BlockRegistry.INSTANCE.getId(block);
			if (id == null || id.namespace != BNB.NAMESPACE) return;
			String name = I18n.translate(block.getTranslatedName());
			if (name.startsWith("tile.")) {
				buffer.append(name);
				buffer.append("=");
				buffer.append(fastTranslate(name));
				lines.add(buffer.toString());
				buffer.setLength(0);
				
			}
		});
	}
	
	private void addItemTranslations(List<String> lines) {
		StringBuffer buffer = new StringBuffer(256);
		ItemRegistry.INSTANCE.forEach(item -> {
			Identifier id = ItemRegistry.INSTANCE.getId(item);
			if (id == null || id.namespace != BNB.NAMESPACE) return;
			String name = I18n.translate(item.getTranslatedName());
			if (name.startsWith("item.")) {
				buffer.append(name);
				buffer.append("=");
				buffer.append(fastTranslate(name));
				lines.add(buffer.toString());
				buffer.setLength(0);
			}
		});
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
	
	// TODO remove that after release
	private void debugTerrain() {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		
		//TerrainFeature feature = new CavesFeature();
		//feature.debugImage();
		
		//long t = System.currentTimeMillis();
		//TerrainFeature feature = new RiversFeature();
		//feature.setSeed(2);
		//feature.debugImage();
		//t = System.currentTimeMillis() - t;
		//System.out.println("\n\nF: " + t + "\n\n");
		
		TerrainFeature feature = new RiversFeature();
		TerrainMap regionMap = new TerrainMap();
		//BiomeMap biomeMap = new BiomeMap();
		
		regionMap.setSeed(10);
		//biomeMap.setSeed(10);
		regionMap.setRiversSeed(-512);
		feature.setSeed(-512);
		
		BufferedImage img1 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		//BufferedImage img2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		int scale = 4;
		
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				TerrainRegion region = regionMap.getRegion(x * scale, z * scale);
				
				int rgb;
				if (region == TerrainRegion.OCEAN_NORMAL || region == TerrainRegion.OCEAN_MOUNTAINS) rgb = 0x0000FF;
				else if (region == TerrainRegion.SHORE_NORMAL || region == TerrainRegion.SHORE_MOUNTAINS) rgb = 0x00CCFF;
				else if (region == TerrainRegion.RIVERS) rgb = 0xFF00FF;
				else rgb = 0x00FF00;
				
				img1.setRGB(x, z, 0xFF000000 | rgb);
				
				float dens = feature.getDensity(x * scale, 96, z * scale);
				if (dens < 0.5F) img1.setRGB(x, z, 0xFFFFFFFF);
				
				//Biome biome = biomeMap.getData(x * scale, z * scale);
				//img2.setRGB(x, z, 0xFF000000 | biome.name.hashCode());
			}
		}
		
		JFrame frame1 = new JFrame();
		frame1.add(new JLabel(new ImageIcon(img1)));
		frame1.pack();
		frame1.setResizable(false);
		frame1.setLocationRelativeTo(null);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame1.setVisible(true);
		
		/*JFrame frame2 = new JFrame();
		frame2.add(new JLabel(new ImageIcon(img2)));
		frame2.pack();
		frame2.setResizable(false);
		frame2.setLocationRelativeTo(null);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame2.setVisible(true);*/
		
		//TerrainFeature feature = new RiversFeature();
		//feature.debugImage();
		
		/*TerrainFeature feature = new RiversFeature();
		BufferedImage img2 = new BufferedImage(512, 512, BufferedImage.TYPE_INT_ARGB);
		
		for (int x = 0; x < 512; x++) {
			for (int z = 0; z < 512; z++) {
				float density = feature.getDensity(x << 2, 96, z << 2);
				int rgb = (int) (MathHelper.clamp(density * 0.5F + 0.5F, 0.0F, 1.0F) * 255);
				img2.setRGB(x, z, 0xFF000000 | rgb << 16 | rgb << 8 | rgb);
			}
		}
		
		JFrame frame2 = new JFrame();
		frame2.add(new JLabel(new ImageIcon(img2)));
		frame2.pack();
		frame2.setResizable(false);
		frame2.setLocationRelativeTo(null);
		frame2.setVisible(true);*/
	}
	
	// TODO remove that after release
	private void biomeColors() {
		if (!FabricLoader.getInstance().isDevelopmentEnvironment()) return;
		biomeColor(0xFF052a32, 0xB6C8CA);
	}
	
	// TODO remove that after release
	private void biomeColor(int color, int grassColor) {
		float r = ((color >> 16) & 255) / 255.0F;
		float g = ((color >> 8) & 255) / 255.0F;
		float b = (color & 255) / 255.0F;
		
		float grassR = ((grassColor >> 16) & 255) / 255.0F;
		float grassG = ((grassColor >> 8) & 255) / 255.0F;
		float grassB = (grassColor & 255) / 255.0F;
		
		r /= grassR;
		g /= grassG;
		b /= grassB;
		
		float mx = Math.max(r, Math.max(g, b));
		if (mx > 1.0F) {
			r = r / mx;
			g = g / mx;
			b = b / mx;
		}
		
		int ir = MathHelper.clamp((int) Math.ceil(r * 255.0F), 0, 255);
		int ig = MathHelper.clamp((int) Math.ceil(g * 255.0F), 0, 255);
		int ib = MathHelper.clamp((int) Math.ceil(b * 255.0F), 0, 255);
		
		System.out.println("Color: 0xFF" + Integer.toHexString(ir << 16 | ig << 8 | ib).toUpperCase(Locale.ROOT));
	}
}
