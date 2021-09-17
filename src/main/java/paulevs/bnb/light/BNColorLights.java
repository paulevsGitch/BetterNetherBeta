package paulevs.bnb.light;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.BlockBase;
import paulevs.betterlight.LightColors;
import paulevs.betterlight.LightRegistry;
import paulevs.bnb.block.types.DarkshroomType;
import paulevs.bnb.block.types.FlameBambooType;
import paulevs.bnb.block.types.NetherPlantType;
import paulevs.bnb.block.types.SoulPlantType;
import paulevs.bnb.listeners.BlockListener;

public class BNColorLights {
	private static final boolean ENABLED = FabricLoader.getInstance().isModLoaded("betterlight");
	
	public static void initColorLights() {
		LightRegistry.EVENT.register(new LightRegistry() {
			@Override
			public void registerLightSources() {
				int color = LightColors.getColor("29dfeb");
				LightColors.setBlockLight(BlockListener.getBlockID("darkshroom"), DarkshroomType.CENTER.getMeta(), color);
				LightColors.setBlockLight(BlockListener.getBlockID("darkshroom"), DarkshroomType.SIDE.getMeta(), color);
				
				color = LightColors.getColor("7cf2f5");
				LightColors.setBlockLight(BlockListener.getBlockID("soul_spire_block"), color);
				LightColors.setBlockLight(BlockListener.getBlockID("soul_grass"), SoulPlantType.SOUL_BULBITE.getMeta(), color);
				LightColors.setBlockLight(BlockListener.getBlockID("soul_spire"), LightColors.getColor("adfdff"));
				
				color = LightColors.getColor("ff9741");
				LightColors.setBlockLight(BlockBase.STILL_LAVA, color);
				LightColors.setBlockLight(BlockBase.FLOWING_LAVA, color);
				
				color = LightColors.getColor("e23f36");
				int id = BlockListener.getBlockID("flame_bamboo");
				LightColors.setBlockLight(id, FlameBambooType.TOP.getMeta(), color);
				LightColors.setBlockLight(id, FlameBambooType.TOP_INACTIVE.getMeta(), color);
				LightColors.setBlockLight(id, FlameBambooType.LADDER.getMeta(), color);
				LightColors.setBlockLight(id, FlameBambooType.MIDDLE.getMeta(), color);
				
				LightColors.setBlockLight(BlockListener.getBlockID("nether_grass"), NetherPlantType.LANTERN_GRASS.getMeta(), LightColors.getColor("f7e28f"));
				LightColors.setBlockLight(BlockListener.getBlockID("nether_grass"), NetherPlantType.LAMELLARIUM.getMeta(), LightColors.getColor("6aa1b5"));
			}
		});
	}
	
	public static boolean isColorLightsInstalled() {
		return ENABLED;
	}
}
