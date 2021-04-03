package paulevs.bnb.listeners;

import java.io.DataInputStream;
import java.io.InputStream;
import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.client.texture.TextureFactory;
import net.modificationstation.stationloader.api.client.texture.TextureRegistry;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.util.JsonUtil;
import paulevs.bnb.util.ResourceUtil;

public class TextureListener implements TextureRegister {
	private static final Map<String, Integer> BLOCK_TEXTURES = Maps.newHashMap();
	
	@Override
	public void registerTextures() {
		TextureFactory textureFactory = TextureFactory.INSTANCE;
		TextureRegistry terrain = TextureRegistry.getRegistry("TERRAIN");
		String pathBlock = "/assets/" + BetterNetherBeta.MOD_ID + "/textures/block/";
		loadTextureMap(textureFactory, terrain, pathBlock, BLOCK_TEXTURES);
		ModelListener.updateModels();
	}
	
	private void loadTextureMap(TextureFactory factory, TextureRegistry registry, String path, Map<String, Integer> map) {
		System.out.println("Textures loaded");
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
		JsonElement obj = animation.get(texture);
		return obj == null ? 1 : obj.getAsInt();
	}
	
	public static int getBlockTexture(String name) {
		return BLOCK_TEXTURES.getOrDefault(name, 0);
	}
	
	public static int getBlockTexture(String name, String alternative) {
		Integer value = BLOCK_TEXTURES.get(name);
		if (value == null) {
			value = BLOCK_TEXTURES.get(alternative);
		}
		return value == null ? 0 : value.intValue();
	}
}
