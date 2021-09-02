package paulevs.bnb.listeners;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.client.texture.TextureFactory;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.JsonUtil;
import paulevs.bnb.util.ResourceUtil;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

public class TextureListener implements TextureRegister {
	private static final Map<String, Integer> BLOCK_TEXTURES = Maps.newHashMap();
	private static final Map<String, Integer> ITEM_TEXTURES = Maps.newHashMap();
	
	@Override
	public void registerTextures() {
		TextureFactory textureFactory = TextureFactory.INSTANCE;
		TextureRegistry terrain = TextureRegistry.getRegistry("TERRAIN");
		TextureRegistry items = TextureRegistry.getRegistry("GUI_ITEMS");
		String pathBlock = BetterNetherBeta.getTexturePath("block");
		String pathItem = BetterNetherBeta.getTexturePath("item");
		loadTextureMap(textureFactory, terrain, pathBlock, BLOCK_TEXTURES);
		loadTextureMap(textureFactory, items, pathItem, ITEM_TEXTURES);
		ModelListener.updateModels();
	}
	
	private void loadTextureMap(TextureFactory factory, TextureRegistry registry, String path, Map<String, Integer> map) {
		JsonObject animation = JsonUtil.loadJson(path + "animation_speed.json");
		List<String> list = ResourceUtil.getResourceFiles(path);
		list.forEach((texture) -> {
			if (texture.endsWith(".png")) {
				int width = 16;
				int height = 16;
				try {
					InputStream stream = ResourceUtil.getResourceAsStream(path + texture);
					DataInputStream data = new DataInputStream(stream);
					data.skip(16);
					width = data.readInt();
					height = data.readInt();
					data.close();
					stream.close();
				}
				catch (Exception e) {
					e.printStackTrace();
				}
				boolean normal = width == height;
				int index = normal ? factory.addTexture(registry, path + texture) : factory.addAnimatedTexture(registry, path + texture, getAnimationSpeed(animation, path + texture));
				String name = texture.substring(0, texture.lastIndexOf('.'));
				map.put(name, index);
			}
		});
	}
	
	private int getAnimationSpeed(JsonObject animation, String texture) {
		String name = texture.substring(texture.lastIndexOf('/') + 1, texture.lastIndexOf('.'));
		if (name.endsWith("_e")) {
			name = name.substring(0, name.length() - 2);
		}
		JsonElement obj = animation.get(name);
		return obj == null ? 1 : obj.getAsInt();
	}
	
	public static int getEmissiveBlockTexture(String name) {
		return BLOCK_TEXTURES.getOrDefault(name + "_e", -1);
	}
	
	public static int getSolidBlockTexture(String name) {
		return BLOCK_TEXTURES.getOrDefault(name, -1);
	}
	
	public static int getBlockTexture(String name) {
		return BlockUtil.isLightPass() ? getBlockTextureA(name + "_e", "empty") : BLOCK_TEXTURES.getOrDefault(name, 0);
	}
	
	public static int getBlockTexture(String name, String alternative) {
		if (BlockUtil.isLightPass()) {
			Integer value = BLOCK_TEXTURES.get(name + "_e");
			if (value == null) {
				value = BLOCK_TEXTURES.get(alternative + "_e");
			}
			return value == null ? BLOCK_TEXTURES.get("empty") : value.intValue();
		}
		return getBlockTextureA(name, alternative);
	}
	
	private static int getBlockTextureA(String name, String alternative) {
		Integer value = BLOCK_TEXTURES.get(name);
		if (value == null) {
			value = BLOCK_TEXTURES.get(alternative);
		}
		return value == null ? 0 : value.intValue();
	}
	
	public static int getItemTexture(String name) {
		return ITEM_TEXTURES.getOrDefault(name, 0);
	}
}
