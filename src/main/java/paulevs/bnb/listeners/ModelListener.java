package paulevs.bnb.listeners;

import java.util.List;
import java.util.Map;

import com.google.common.collect.Maps;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import net.minecraft.client.render.QuadPoint;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationloader.api.client.event.model.ModelRegister;
import net.modificationstation.stationloader.api.client.model.CustomCuboidRenderer;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.client.model.CustomModelRenderer;
import net.modificationstation.stationloader.api.client.model.CustomTexturedQuad;
import net.modificationstation.stationloader.api.common.factory.GeneralFactory;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.util.JsonUtil;
import paulevs.bnb.util.ResourceUtil;

public class ModelListener implements ModelRegister {
	private static final Map<String, CustomModel> BLOCK_MODELS = Maps.newHashMap();
	private static final float COEFFICIENT = (float) (Math.PI * Math.PI * 0.1 / 180.0);
	
	@Override
	public void registerModels(Type type) {
		if (type == ModelRegister.Type.BLOCKS) {
			String pathBlock = "/assets/" + BetterNetherBeta.MOD_ID + "/models/block/";
			List<String> list = ResourceUtil.getResourceFiles(pathBlock);
			list.forEach((name) -> {
				CustomModel model = loadModel(pathBlock, name);
				fixRotation(model, pathBlock + "/" + name);
				BLOCK_MODELS.put(name.substring(0, name.lastIndexOf('.')), model);
			});
		}
	}
	
	private CustomModel loadModel(String path, String name) {
		CustomModelRenderer instance = GeneralFactory.INSTANCE.newInst(CustomModelRenderer.class, path + "/" + name, BetterNetherBeta.MOD_ID);
		return instance.getEntityModelBase();
	}
	
	public static CustomModel getBlockModel(String model) {
		return BLOCK_MODELS.get(model);
	}
	
	private void fixRotation(CustomModel model, String path) {
		JsonObject root = JsonUtil.loadJson(path);
		JsonArray elements = root.getAsJsonArray("elements");
		CustomCuboidRenderer[] cuboids = model.getCuboids();
		for (int i = 0; i < elements.size(); i++) {
			JsonObject element = elements.get(i).getAsJsonObject();
			if (element.has("rotation")) {
				JsonObject rotation = element.getAsJsonObject("rotation");
				JsonArray origin = rotation.getAsJsonArray("origin");
				char axis = rotation.get("axis").getAsString().charAt(0);
				float angle = rotation.get("angle").getAsFloat() * COEFFICIENT;
				float cx = origin.get(0).getAsFloat();
				float cy = origin.get(1).getAsFloat();
				float cz = origin.get(2).getAsFloat();
				CustomTexturedQuad[] quads = cuboids[i].getCubeQuads();
				for (CustomTexturedQuad quad: quads) {
					for (QuadPoint point: quad.getQuadPoints()) {
						Vec3f pos = point.pointVector;
						if (axis == 'x') {
							pos.x -= cx;
							pos.y -= cy;
							pos.method_1306(-angle);
							pos.x += cx;
							pos.y += cy;
						}
						else if (axis == 'y') {
							pos.x -= cx;
							pos.z -= cz;
							pos.method_1308(angle);
							pos.x += cx;
							pos.z += cz;
						}
						else {
							pos.x -= cx;
							pos.y -= cy;
							float cos = MathHelper.cos(-angle);
							float sin = MathHelper.sin(-angle);
							double nx = pos.x * cos + pos.y * sin;
							double ny = pos.y * cos - pos.x * sin;
							pos.x = nx + cx;
							pos.y = ny + cy;
						}
					}
				}
			}
		}
	}
}
