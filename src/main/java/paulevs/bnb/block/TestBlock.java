package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.listeners.ModelListener;

public class TestBlock extends NetherBlock implements BlockModelProvider {
	public TestBlock(String registryName, int id) {
		super(registryName, id, Material.STONE);
	}

	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return ModelListener.getBlockModel("test");
	}

	@Override
	public CustomModel getCustomWorldModel(Level level, int i, int j, int k, int i1) {
		return ModelListener.getBlockModel("test");
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}
}
