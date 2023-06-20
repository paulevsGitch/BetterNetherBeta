package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

import java.util.Random;

public class NetherCeilPlantBlock extends TemplateBlockBase {
	public NetherCeilPlantBlock(Identifier id) {
		super(id, Material.PLANT);
		this.setBoundingBox(0.125F, 0.25F, 0.125F, 0.875F, 1.0F, 0.875F);
		this.disableNotifyOnMetaDataChange();
		this.setSounds(GRASS_SOUNDS);
		this.disableStat();
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		return isCeil(level.getBlockState(x, y + 1, z));
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
	
	protected boolean isCeil(BlockState state) {
		BlockBase block = state.getBlock();
		return (block.isFullCube() && block.isFullOpaque()) || block instanceof NetherLeavesBlock;
	}
	
	protected void tick(Level level, int x, int y, int z) {
		if (!this.canPlaceAt(level, x, y, z)) {
			this.drop(level, x, y, z, level.getTileMeta(x, y, z));
			level.setTile(x, y, z, 0);
		}
	}
}
