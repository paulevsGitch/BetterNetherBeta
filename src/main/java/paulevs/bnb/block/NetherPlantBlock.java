package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;

public abstract class NetherPlantBlock extends TemplateBlockBase {
	public NetherPlantBlock(Identifier id, Material material) {
		super(id, material);
		this.disableNotifyOnMetaDataChange();
		this.setSounds(GRASS_SOUNDS);
		this.disableStat();
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return canStay(level, x, y, z);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		tick(level, x, y, z);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		tick(level, x, y, z);
	}
	
	@Override
	public boolean canGrow(Level level, int x, int y, int z) {
		return canPlaceAt(level, x, y, z);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	protected abstract boolean canStay(Level level, int x, int y, int z);
	
	protected void tick(Level level, int x, int y, int z) {
		if (!this.canStay(level, x, y, z)) {
			this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			level.setTile(x, y, z, 0);
		}
	}
}
