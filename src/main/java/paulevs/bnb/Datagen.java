package paulevs.bnb;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.google.gson.stream.JsonWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class Datagen {
	private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
	
	public static void makeFullBlock(String name) {
		JsonObject root = new JsonObject();
		JsonObject variants = new JsonObject();
		root.add("variants", variants);
		JsonObject variant = new JsonObject();
		variants.add("", variant);
		variant.addProperty("model", "bnb:block/" + name);
		saveJson(root, new File("../src/main/resources/assets/bnb/stationapi/blockstates/" + name + ".json"));
		
		root = new JsonObject();
		root.addProperty("parent", "minecraft:block/cube_all");
		JsonObject textures = new JsonObject();
		root.add("textures", textures);
		textures.addProperty("all", "bnb:block/" + name);
		saveJson(root, new File("../src/main/resources/assets/bnb/stationapi/models/block/" + name + ".json"));
		
		root = new JsonObject();
		root.addProperty("parent", "bnb:block/" + name);
		saveJson(root, new File("../src/main/resources/assets/bnb/stationapi/models/item/" + name + ".json"));
	}
	
	private static void saveJson(JsonObject obj, File out) {
		if (out.exists()) return;
		try {
			FileWriter fileWriter = new FileWriter(out);
			JsonWriter writer = new JsonWriter(fileWriter);
			writer.setIndent("\t");
			GSON.toJson(obj, writer);
			writer.flush();
			writer.close();
			fileWriter.close();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
}
