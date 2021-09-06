package paulevs.bnb.block;

import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.MHelper;

public class WallCoralBlock extends WallNetherPlantBlock {
	public <T extends BlockEnum> WallCoralBlock(String name, int id) {
		super(name, id);
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		int model = MHelper.getRandomHash(y, x, z) % 3;
		return ModelListener.getBlockModel("wall_coral_" + model + "_" + metaToRotation(level.getTileMeta(x, y, z)));
	}
	
	private int metaToRotation(int meta) {
		BlockDirection dir = BlockDirection.fromFacing(meta);
		switch (dir) {
			case NEG_X: return 1;
			case POS_X: return 3;
			case NEG_Z: return 2;
			case POS_Z: return 0;
		}
		return -1;
	}
}
