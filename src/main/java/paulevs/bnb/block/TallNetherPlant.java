package paulevs.bnb.block;

import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.block.types.TallGlowNetherPlants;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class TallNetherPlant extends NetherPlantBlock implements BlockWithLight {
	public <T extends BlockEnum> TallNetherPlant(String name, int id, Class<T> plantClass) {
		super(name, id, plantClass, false);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
	}
	
	public <T extends BlockEnum> TallNetherPlant(String name, int id, Class<T> plantClass, float light) {
		super(name, id, plantClass, false);
		this.disableNotifyOnMetaDataChange();
		this.setLightEmittance(light);
		this.disableStat();
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return id == this.id || BlockUtil.isTerrain(id);
	}

	@Override
	public float getEmissionIntensity() {
		return 4F;
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		if (meta == TallGlowNetherPlants.BULBINE.getMeta()) {
			if (level.getTileId(x, y + 1, z) != this.id) {
				int rotation = MHelper.getRandomHash(y, x, z) & 3;
				return ModelListener.getBlockModel("bulbine_stem_top_" + rotation);
			}
			else {
				Random random = MHelper.getRandom();
				random.setSeed(MHelper.getRandomHash(y, x, z));
				int state = random.nextInt(3);
				int rotation = random.nextInt(4);
				return ModelListener.getBlockModel("bulbine_stem_" + state + "_" + rotation);
			}
		}
		return super.getCustomWorldModel(level, x, y, z, meta);
	}
}
