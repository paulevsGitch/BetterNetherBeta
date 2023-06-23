package paulevs.bnb.listeners;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonReader;
import net.mine_diver.unsafeevents.listener.EventListener;
import net.modificationstation.stationapi.api.client.event.render.model.LoadUnbakedModelEvent;
import net.modificationstation.stationapi.api.client.texture.SpriteIdentifier;
import net.modificationstation.stationapi.api.client.texture.atlas.Atlases;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.util.math.Vec3f;
import paulevs.bnb.BNB;
import paulevs.bnb.rendering.OBJModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class ModelListener {
	private static final Gson GSON = new GsonBuilder().create();
	
	@EventListener
	public void onModelLoad(LoadUnbakedModelEvent event) throws IOException {
		if (event.identifier.modID != BNB.MOD_ID) return;
		if (!event.identifier.id.startsWith("block/")) return;
		
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
		if (stream == null) return;
		
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
	
	private InputStream getAsStream(Identifier id) {
		String path = "assets/bnb/stationapi/models/" + id.id + ".json";
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
	
	private InputStream getAsStream(String path) {
		path = "assets/bnb/stationapi/models/" + path + ".obj";
		return Thread.currentThread().getContextClassLoader().getResourceAsStream(path);
	}
}
