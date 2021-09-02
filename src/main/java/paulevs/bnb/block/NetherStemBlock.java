package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherStemType;

public class NetherStemBlock extends MultiBlock {
	public NetherStemBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherStemType.class);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
	}
	
	@Environment(EnvType.CLIENT)
	public void method_1605() {
		this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}
	
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		this.setBoundingBox(0.25F, 0.0F, 0.25F, 0.75F, 1.0F, 0.75F);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		this.method_1616(level, x, y, z);
		return super.getCollisionShape(level, x, y, z);
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
