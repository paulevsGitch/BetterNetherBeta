package paulevs.bnb.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

public class JsonUtil {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	private static JsonObject loadJsonResource(InputStream stream) {
		if (stream == null) {
			return new JsonObject();
		}
		JsonObject obj = null;
		try {
			InputStreamReader reader = new InputStreamReader(stream);
			obj = GSON.fromJson(reader, JsonObject.class);
			reader.close();
			stream.close();
		}
		catch (JsonSyntaxException | JsonIOException | IOException e) {
			e.printStackTrace();
		}
		return obj == null ? new JsonObject() : obj;
	}
	
	public static JsonObject loadJsonResource(String path) {
		return loadJsonResource(JsonUtil.class.getResourceAsStream(path));
	}
	
	public static JsonObject loadJsonFile(File file) {
		if (!file.exists()) {
			return new JsonObject();
		}
		try {
			return loadJsonResource(new FileInputStream(file));
		}
		catch (FileNotFoundException e) {
			e.printStackTrace();
			return new JsonObject();
		}
	}
	
	public static void saveJson(File file, JsonObject obj) {
		try {
			file.getParentFile().mkdirs();
			FileWriter writer = new FileWriter(file);
			GSON.toJson(obj, writer);
			writer.close();
		}
		catch (JsonIOException | IOException e) {
			e.printStackTrace();
		}
	}
}
