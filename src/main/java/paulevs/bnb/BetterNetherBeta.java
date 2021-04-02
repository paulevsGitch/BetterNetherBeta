package paulevs.bnb;

import net.modificationstation.stationloader.api.client.event.texture.TextureRegister;
import net.modificationstation.stationloader.api.common.event.block.BlockRegister;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import net.modificationstation.stationloader.api.common.event.level.biome.BiomeRegister;
import net.modificationstation.stationloader.api.common.event.recipe.RecipeRegister;
import net.modificationstation.stationloader.api.common.mod.StationMod;
import paulevs.bnb.listeners.BiomeListener;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.listeners.ChunkListener;
import paulevs.bnb.listeners.EffectiveProvider;
import paulevs.bnb.listeners.RecipeListener;
import paulevs.bnb.listeners.TextureListener;

public class BetterNetherBeta implements StationMod {
	public static final String MOD_ID = "bnb";
	public static JsonConfig configBlocks = new JsonConfig("blocks");
	
	@Override
	public void preInit() {
		BlockRegister.EVENT.register(new BlockListener());
		TextureRegister.EVENT.register(new TextureListener());
		EffectiveBlocksProvider.EVENT.register(new EffectiveProvider());
		//ModelRegister.EVENT.register(new ModelListener());
		ChunkListener.EVENT.register(new ChunkListener());
		BiomeRegister.EVENT.register(new BiomeListener());
		RecipeRegister.EVENT.register(new RecipeListener());
	}
	
	public static String getID(String name) {
		return MOD_ID + "." + name;
	}
}
