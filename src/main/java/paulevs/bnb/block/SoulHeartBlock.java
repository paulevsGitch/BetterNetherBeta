package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ItemListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class SoulHeartBlock extends NetherCropBlock implements BlockWithLight {
	public SoulHeartBlock(String name, int id) {
		super(name, id, 3);
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return BlockUtil.isSoulTerrain(id);
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 1;
	}

	@Override
	public float getEmissionIntensity() {
		return 3;
	}
	
	@Override
	public int getDropId(int meta, Random rand) {
		return ItemListener.getItem("soul_heart_seeds").id;
	}
	
	@Override
	public int droppedMeta(int meta) {
		return 0;
	}

	@Override
	public int getDropCount(Random rand) {
		return 1;
	}
	
	@Override
	public void beforeDestroyedByExplosion(Level level, int x, int y, int z, int meta, float dropChance) {
		if (!level.isClient) {
			if (meta > 2) {
				this.drop(level, x, y, z, new ItemInstance(ItemListener.getItem("heart_fruit").id, 1, 0));
				int count = MHelper.randRange(1, 3, MHelper.getRandom());
				this.drop(level, x, y, z, new ItemInstance(ItemListener.getItem("soul_heart_seeds").id, count, 0));
			}
			else {
				this.drop(level, x, y, z, new ItemInstance(ItemListener.getItem("soul_heart_seeds").id, 1, 0));
			}
		}
	}
}
