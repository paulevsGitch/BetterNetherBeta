package paulevs.bnb;

import net.fabricmc.loader.api.FabricLoader;
import net.fabricmc.loader.api.metadata.ModMetadata;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.block.TileEntityRegister;
import net.modificationstation.stationloader.api.common.event.item.ItemRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import paulevs.bnb.effects.StatusEffects;
import paulevs.bnb.light.BNColorLights;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.listeners.ChunkListener;
import paulevs.bnb.listeners.EffectiveProvider;
import paulevs.bnb.listeners.ItemListener;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.listeners.RecipeListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.listeners.TileEntityListener;
import paulevs.bnb.tab.BNTabInventory;
import paulevs.bnb.util.CreativeUtil;
import paulevs.bnb.util.RadialSearch;

import java.util.Locale;

public class BetterNetherBeta implements StationMod {
	public static final String MOD_ID = "bnb";
	public static JsonConfig configBlocks = new JsonConfig("blocks");
	public static JsonConfig configItems = new JsonConfig("items");
	
	@Override
	public void preInit() {
		RadialSearch.init();
		StatusEffects.register();
		ModMetadata metadata = FabricLoader.getInstance().getModContainer(MOD_ID).get().getMetadata();
		ItemRegister.EVENT.register(new ItemListener(), metadata);
		BlockRegister.EVENT.register(new BlockListener(), metadata);
		RecipeRegister.EVENT.register(new RecipeListener());
		TextureRegister.EVENT.register(new TextureListener());
		EffectiveBlocksProvider.EVENT.register(new EffectiveProvider());
		ModelRegister.EVENT.register(new ModelListener());
		ChunkListener.EVENT.register(new ChunkListener());
		BiomeRegister.EVENT.register(new BiomeListener());
		TileEntityRegister.EVENT.register(new TileEntityListener());
		
		if (CreativeUtil.isCreativeInstalled()) {
			BNTabInventory.createTab();
		}
		
		if (BNColorLights.isColorLightsInstalled()) {
			BNColorLights.initColorLights();
		}
	}
	
	public static String getID(String name) {
		return MOD_ID + "." + name;
	}
	
	public static String getTexturePath(String category) {
		return String.format(Locale.ROOT, "/assets/%s/textures/%s/", MOD_ID, category);
	}
	
	public static String getTexturePath(String category, String name) {
		return String.format(Locale.ROOT, "/assets/%s/textures/%s/%s.png", MOD_ID, category, name);
	}
}
