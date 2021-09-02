package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.DarkshroomType;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.ModelListener;

public class DarkshroomBlock extends MultiBlock implements BlockWithLight, BlockModelProvider {
	public DarkshroomBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, DarkshroomType.class);
		this.setHardness(WOOD.getHardness());
		this.setLightEmittance(1F);
		this.sounds(WOOD_SOUNDS);
	}
	
	@Environment(EnvType.CLIENT)
	public void method_1605() {
		this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		int meta = world.getTileMeta(x, y, z);
		this.setBoundingBox(0.0F, meta * 0.5F, 0.0F, 1.0F, 1.0F, 1.0F);
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
	
	@Override
	public float getEmissionIntensity() {
		return 3;
	}
	
	@Override
	public CustomModel getCustomWorldModel(Level world, int x, int y, int z, int meta) {
		return meta == 0 ? ModelListener.getBlockModel("darkshroom_center") : ModelListener.getBlockModel("darkshroom_side");
	}
	
	@Override
	public CustomModel getCustomInventoryModel(int i) {
		return ModelListener.getBlockModel("darkshroom_side");
	}
}
