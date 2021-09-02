package paulevs.bnb.listeners;

import com.google.common.collect.Maps;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import paulevs.bnb.block.model.OBJBlockModel;
import paulevs.bnb.block.types.NetherLeavesType;
import paulevs.bnb.block.types.NetherPlantType;
import paulevs.bnb.block.types.NetherTreeFurType;
import paulevs.bnb.block.types.SoulPlantType;
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
			
			OBJBlockModel cross = new OBJBlockModel("/assets/bnb/models/block/cross_no_dist.obj", 16, 8, 0, 8, BlockFaces.UP);
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
