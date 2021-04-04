package paulevs.bnb.listeners;

import java.util.Map;

import com.google.common.collect.Maps;

import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.common.util.BlockFaces;
import paulevs.bnb.block.model.OBJBlockModel;
import paulevs.bnb.block.types.NetherPlants;
import paulevs.bnb.util.BlockUtil;

public class ModelListener implements ModelRegister {
	private static final Map<String, CustomModel> BLOCK_MODELS = Maps.newHashMap();
	
	@Override
	public void registerModels(Type type) {
		if (type == ModelRegister.Type.BLOCKS) {
			BLOCK_MODELS.put("test", new OBJBlockModel("/assets/bnb/models/block/warped_fungus.obj", 16, 8, 0, 8, null).setTextures("warped_fungus", "warped_fungus_bottom"));
			
			OBJBlockModel fungus = new OBJBlockModel("/assets/bnb/models/block/warped_fungus.obj", 16, 8, 0, 8, null).setTextures("warped_fungus", "warped_fungus_bottom");
			for (int i = 0; i < 4; i++) {
				float angle = (float) Math.PI * 0.5F * i;
				BLOCK_MODELS.put("warped_fungus_" + i, fungus.clone().rotateY(angle));
			}
			
			OBJBlockModel fluffyGrass = new OBJBlockModel("/assets/bnb/models/block/fluffy_grass.obj", 16, 8, 0, 8, BlockFaces.UP);
			for (NetherPlants plant: NetherPlants.values()) {
				BLOCK_MODELS.put(plant.getName(), fluffyGrass.clone().setTextures(plant.getTexture(0)));
			}
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
