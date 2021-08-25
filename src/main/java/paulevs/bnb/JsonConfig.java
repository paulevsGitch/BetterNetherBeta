package paulevs.bnb;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import net.fabricmc.loader.api.FabricLoader;
import paulevs.bnb.util.JsonUtil;

import java.io.File;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Supplier;

public class JsonConfig {
	private final String name;
	private boolean requireSave;
	public JsonObject config;
	
	public JsonConfig(String name) {
		this.name = name;
	}
	
	public void load() {
		File file = getFile();
		requireSave = !file.exists();
		config = JsonUtil.loadJson(file);
	}
	
	public void save() {
		if (requireSave) {
			JsonUtil.saveJson(getFile(), config);
		}
	}
	
	private File getFile() {
		return new File(FabricLoader.getInstance().getConfigDir().toString(), BetterNetherBeta.MOD_ID + "/" + name + ".json");
	}
	
	private JsonElement get(String path, Supplier<JsonElement> def) {
		String[] parts = path.split("\\.");
		JsonElement element = config;
		int last = parts.length - 1;
		for (int i = 0; i < parts.length; i++) {
			String name = parts[i];
			if (!element.getAsJsonObject().has(name)) {
				requireSave = true;
				element.getAsJsonObject().add(name, i == last ? def.get() : new JsonObject());
			}
			element = element.getAsJsonObject().get(name);
		}
		return element;
	}
	
	private void set(String path, JsonElement value) {
		String[] parts = path.split("\\.");
		JsonElement element = config;
		int last = parts.length - 1;
		for (int i = 0; i < last; i++) {
			String name = parts[i];
			if (!element.getAsJsonObject().has(name)) {
				element.getAsJsonObject().add(name, new JsonObject());
			}
			element = element.getAsJsonObject().get(name);
		}
		requireSave = true;
		element.getAsJsonObject().add(parts[last], value);
	}
	
	public int getInt(String path, int def) {
		return get(path, () -> new JsonPrimitive(def)).getAsInt();
	}
	
	public void setInt(String path, int value) {
		set(path, new JsonPrimitive(value));
	}
	
	public Set<Integer> getSet(String path) {
		JsonObject obj = get(path, () -> new JsonObject()).getAsJsonObject();
		Set<Integer> result = new HashSet<Integer>();
		obj.entrySet().forEach((entry) -> {
			JsonElement element = entry.getValue();
			if (element.isJsonPrimitive() && element.getAsJsonPrimitive().isNumber()) {
				result.add(element.getAsInt());
			}
		});
		return result;
	}
}
