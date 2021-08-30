package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import paulevs.bnb.interfaces.BlockEnum;

public class LayeredBlock extends MultiBlock {
	public <T extends BlockEnum> LayeredBlock(String name, int id, Material material, Class<T> type) {
		super(name, id, material, type);
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerBase player) {
		ItemInstance itemInstance = player.getHeldItem();
		if (itemInstance != null) {
			int meta = level.getTileMeta(x, y, z);
			if ((meta & 3) == 3) {
				return false;
			}
			int dropID = this.getDropId(meta, level.rand);
			if (itemInstance.itemId == dropID && clampMeta(itemInstance.getDamage()) == clampMeta(meta)) {
				itemInstance.count--;
				level.setTileMeta(x, y, z, meta + 1);
				level.method_202(x, y, z, x, y, z);
				return true;
			}
		}
		return false;
	}
	
	// Item Bounding Box
	@Environment(EnvType.CLIENT)
	public void method_1605() {
		this.setBoundingBox(0.0F, 0.25F, 0.0F, 1.0F, 0.5F, 1.0F);
	}
	
	// World Bounding Box
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		int meta = world.getTileMeta(x, y, z) & 3;
		this.setBoundingBox(0, 0, 0, 1, meta / 4F + 0.25F, 1);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		this.method_1616(level, x, y, z);
		return super.getCollisionShape(level, x, y, z);
	}
	
	@Override
	protected int clampMeta(int meta) {
		return meta >> 2;
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
	public void beforeDestroyedByExplosion(Level level, int x, int y, int z, int meta, float dropChance) {
		if (!level.isClient) {
			this.drop(level, x, y, z, new ItemInstance(getDropId(meta, level.rand), (meta & 3) + 1, getMeta(clampMeta(meta))));
		}
	}
}
