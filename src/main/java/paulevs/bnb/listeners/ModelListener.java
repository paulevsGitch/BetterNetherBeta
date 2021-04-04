package paulevs.bnb.listeners;

import java.util.Map;

import com.google.common.collect.Maps;

import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.block.model.OBJBlockModel;

public class ModelListener implements ModelRegister {
	private static final Map<String, CustomModel> BLOCK_MODELS = Maps.newHashMap();
	
	@Override
	public void registerModels(Type type) {
		if (type == ModelRegister.Type.BLOCKS) {
			CustomModel test = new OBJBlockModel("/assets/bnb/models/block/warped_fungus.obj", 16, 8, 0, 8, "warped_fungus");
			BLOCK_MODELS.put("test", test);
		}
	}
	
	public static CustomModel getBlockModel(String model) {
		return BLOCK_MODELS.get(model);
	}
	
	protected static void updateModels() {
		for (CustomModel model: BLOCK_MODELS.values()) {
			if (model instanceof OBJBlockModel) {
				((OBJBlockModel) model).updateUVs();
			}
		}
	}
}
