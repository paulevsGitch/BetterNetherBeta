package paulevs.bnb.listeners;

import com.google.common.collect.Maps;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import paulevs.bnb.block.model.OBJBlockModel;
import paulevs.bnb.block.types.NetherDoublePlantType;
import paulevs.bnb.block.types.NetherLeavesType;
import paulevs.bnb.block.types.NetherPlantType;
import paulevs.bnb.block.types.NetherTreeFurType;
import paulevs.bnb.block.types.SoulPlantType;
import paulevs.bnb.block.types.UmbraPlantType;
import paulevs.bnb.util.BlockUtil;

import java.util.Map;

public class ModelListener implements ModelRegister {
	private static final Map<String, CustomModel> BLOCK_MODELS = Maps.newHashMap();
	
	@Override
	public void registerModels(Type type) {
		if (type == ModelRegister.Type.BLOCKS) {
			OBJBlockModel model;
			
			model = new OBJBlockModel("/assets/bnb/models/block/warped_fungus.obj", 16, 8, 0, 8, null).setTextures("warped_fungus", "warped_fungus_bottom");
			makeRotated("warped_fungus", model);
			
			model = new OBJBlockModel("/assets/bnb/models/block/crimson_fungus.obj", 16, 8, 0, 8, null).setTextures("crimson_fungus", "crimson_fungus_bottom");
			makeRotated("crimson_fungus", model);
			
			model = new OBJBlockModel("/assets/bnb/models/block/cocoon.obj", 16, 8, 0, 8, null);
			makeRotated("cocoon_crimson", model.setTextures("cocoon_crimson"));
			makeRotated("cocoon_warped", model.setTextures("cocoon_warped"));
			makeRotated("cocoon_poison", model.setTextures("cocoon_poison"));
			
			OBJBlockModel cross = new OBJBlockModel("/assets/bnb/models/block/cross.obj", 16, 8, 0, 8, BlockFaces.UP);
			model = new OBJBlockModel("/assets/bnb/models/block/fluffy_grass.obj", 16, 8, 0, 8, BlockFaces.UP);
			for (NetherPlantType plant: NetherPlantType.values()) {
				if (plant.isCross()) {
					BLOCK_MODELS.put(plant.getName(), cross.clone().setTextures(plant.getTexture(0)));
				}
				else {
					BLOCK_MODELS.put(plant.getName(), model.clone().setTextures(plant.getTexture(0)));
				}
			}
			for (SoulPlantType plant: SoulPlantType.values()) {
				if (plant.isCross()) {
					BLOCK_MODELS.put(plant.getName(), cross.clone().setTextures(plant.getTexture(0)));
				}
				else {
					BLOCK_MODELS.put(plant.getName(), model.clone().setTextures(plant.getTexture(0)));
				}
			}
			for (UmbraPlantType plant: UmbraPlantType.values()) {
				if (plant != UmbraPlantType.SMALL_DARKSHROOM) {
					BLOCK_MODELS.put(plant.getName(), cross.clone().setTextures(plant.getTexture(0)));
				}
			}
			
			model = new OBJBlockModel("/assets/bnb/models/block/tall_plant.obj", 16, 8, 0, 8, BlockFaces.UP);
			for (NetherDoublePlantType plant: NetherDoublePlantType.values()) {
				BLOCK_MODELS.put(plant.getName(), model.clone().setTextures(plant.getName() + "_top", plant.getName() + "_bottom"));
			}
			
			BLOCK_MODELS.put("flame_bamboo_sapling", cross.clone().setTextures("flame_bamboo_sapling"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/bamboo_stem.obj", 16, 8, 0, 8, null);
			BLOCK_MODELS.put("flame_bamboo_stem", model.setTextures("flame_bamboo_stem"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/bamboo_middle.obj", 16, 8, 0, 8, null);
			BLOCK_MODELS.put("flame_bamboo_middle", model.setTextures("flame_bamboo_stem", "flame_bamboo_leaves"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/bamboo_stem_1.obj", 16, 8, 0, 8, null);
			BLOCK_MODELS.put("flame_bamboo_stem_1", model.clone().setTextures("flame_bamboo_stem", "flame_bamboo_leaves_small"));
			BLOCK_MODELS.put("flame_bamboo_ladder", model.setTextures("flame_bamboo_stem", "flame_bamboo_ladder"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/bamboo_stem_2.obj", 16, 8, 0, 8, null);
			makeRotated("flame_bamboo_stem_2", model.setTextures("flame_bamboo_stem", "flame_bamboo_leaves_small"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/bamboo_top.obj", 16, 8, 0, 8, null);
			BLOCK_MODELS.put("flame_bamboo_top", model.setTextures("flame_bamboo_stem", "flame_bamboo_leaves"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/small_darkshroom.obj", 16, 8, 0, 8, null);
			BLOCK_MODELS.put("small_darkshroom", model.setTextures("small_darkshroom_side", "small_darkshroom_top"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/normal_crop.obj");
			for (NetherTreeFurType fur: NetherTreeFurType.values()) {
				BLOCK_MODELS.put(fur.getName(), model.clone().setTextures(fur.getTexture(0)));
				BLOCK_MODELS.put(fur.getName() + "_bottom", model.clone().setTextures(fur.getTexture(0) + "_bottom"));
			}
			
			model = new OBJBlockModel("/assets/bnb/models/block/fluffy_leaves.obj");
			for (NetherLeavesType leaves: NetherLeavesType.values()) {
				String fur = leaves.getName().substring(0, leaves.getName().indexOf('_')) + "_outer_leaves";
				BLOCK_MODELS.put(leaves.getName(), model.clone().setTextures(leaves.getTexture(0), fur));
			}
			
			for (int i = 0; i < 3; i++) {
				model = new OBJBlockModel("/assets/bnb/models/block/bulbine_stem_" + i + ".obj", 16, 8, 0, 8, BlockFaces.UP);
				makeRotated("bulbine_stem_" + i, model.setTextures("bulbine_stem", "bulbine_lanterns"));
			}
			model = new OBJBlockModel("/assets/bnb/models/block/bulbine_stem_top.obj", 16, 8, 0, 8, BlockFaces.UP);
			makeRotated("bulbine_stem_top", model.setTextures("bulbine_stem_top", "bulbine_lanterns"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/darkshroom_block_side.obj");
			BLOCK_MODELS.put("darkshroom_side", model.setTextures("darkshroom_top", "darkshroom_side", "darkshroom_bottom"));
			
			model = new OBJBlockModel("/assets/bnb/models/block/darkshroom_block_center.obj");
			BLOCK_MODELS.put("darkshroom_center", model.setTextures("darkshroom_top", "darkshroom_center_side", "darkshroom_bottom"));
			
			for (int i = 0; i < 3; i++) {
				model = new OBJBlockModel("/assets/bnb/models/block/wall_coral_" + i + ".obj", 16, 8, 0, 16, BlockFaces.UP);
				makeRotated("wall_coral_" + i, model.setTextures("wall_coral"));
			}
		}
	}
	
	private void makeRotated(String name, OBJBlockModel model) {
		for (int i = 0; i < 4; i++) {
			float angle = (float) Math.PI * 0.5F * i;
			BLOCK_MODELS.put(name + "_" + i, model.clone().rotateY(angle));
		}
	}
	
	public static CustomModel getBlockModel(String model) {
		return BLOCK_MODELS.get(model);
	}
	
	protected static void updateModels() {
		BlockUtil.setLightPass(false);
		for (CustomModel model: BLOCK_MODELS.values()) {
			if (model instanceof OBJBlockModel) {
				((OBJBlockModel) model).updateUVs();
			}
		}
	}
}
