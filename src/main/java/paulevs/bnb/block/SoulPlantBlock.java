package paulevs.bnb.block;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import paulevs.bnb.block.types.SoulPlantType;
import paulevs.bnb.util.BlockUtil;

public class SoulPlantBlock extends NetherPlantBlock {
	public SoulPlantBlock(String name, int id) {
		super(name, id, SoulPlantType.class, true);
	}
	
	@Override
	protected boolean canPlantOnTopOf(int id) {
		return BlockUtil.isSoulTerrain(id);
	}
	
	public void onEntityCollision(Level level, int x, int y, int z, EntityBase entity) {
		if (entity instanceof Living && level.getTileMeta(x, y, z) == SoulPlantType.BONE_PEAKS.getMeta()) {
			entity.damage(null, 3);
		}
	}
}
