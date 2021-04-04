package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.util.MHelper;

public class TestBlock extends NetherBlock implements BlockModelProvider, BlockWithLight {
	public TestBlock(String registryName, int id) {
		super(registryName, id, Material.STONE);
	}

	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return null;
	}

	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		int state = MHelper.getSeed(y, x, z) & 3;
		return ModelListener.getBlockModel("warped_fungus_" + state);
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}

	@Override
	public float getEmissionIntensity() {
		return 3F;
	}
}
