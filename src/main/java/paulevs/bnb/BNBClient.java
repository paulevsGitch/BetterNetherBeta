package paulevs.bnb;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;

@Environment(EnvType.CLIENT)
public class BNBClient {
	@SuppressWarnings("deprecation")
	public static Minecraft getMinecraft() {
		return (Minecraft) FabricLoader.getInstance().getGameInstance();
	}
	
	static {
		/*Datagen.makeFullBlock("soul_sandstone_bricks");
		Datagen.makeFullBlock("soul_sandstone_tiles");
		Datagen.makeSquareRecipe("soul_sandstone_bricks", BNB.id("soul_sandstone"), BNB.id("soul_sandstone_bricks"), 4);
		Datagen.makeSquareRecipe("soul_sandstone_bricks", BNB.id("soul_sandstone_bricks"), BNB.id("soul_sandstone_tiles"), 4);
		Datagen.makeStairsRecipe("soul_sandstone_bricks_stairs", BNB.id("soul_sandstone_bricks"), BNB.id("soul_sandstone_bricks_stairs"));
		Datagen.makeSlabRecipe("soul_sandstone_bricks_stairs", BNB.id("soul_sandstone_bricks"), BNB.id("soul_sandstone_bricks_stairs"));
		Datagen.makeStairsRecipe("soul_sandstone_tiles_stairs", BNB.id("soul_sandstone_tiles"), BNB.id("soul_sandstone_tiles_stairs"));
		Datagen.makeSlabRecipe("soul_sandstone_tiles_stairs", BNB.id("soul_sandstone_tiles"), BNB.id("soul_sandstone_tiles_stairs"));*/
		//Datagen.makeFullBlock("jalumine_planks");
		//Datagen.makeFullBlock("soul_soil");
		
		/*JsonObject obj = new JsonObject();
		obj.addProperty("parent", "minecraft:block/cross");
		JsonObject textures = new JsonObject();
		obj.add("textures", textures);
		
		for (int i = 0; i < 16; i++) {
			textures.addProperty("cross", "bnb:block/ametrine_shards_" + i);
			Datagen.saveJson(obj, new File("../src/main/resources/assets/bnb/stationapi/models/block/ametrine_shards_" + i + ".json"));
			textures.remove("cross");
		}*/
	}
}
