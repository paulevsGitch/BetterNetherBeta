package paulevs.bnb.block;

import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.client.model.BlockModelProvider;
import net.modificationstation.stationloader.api.client.model.CustomModel;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.block.tileentity.CocoonSpawner;
import paulevs.bnb.listeners.ModelListener;
import paulevs.bnb.util.MHelper;

public class SpiderCocoonBlock extends BlockWithEntity implements BlockModelProvider, BlockItemProvider {
	private final static String[] TEXTURES = new String[] { "crimson", "warped", "poison" };
	
	public SpiderCocoonBlock(String registryName, int id) {
		super(id, Material.ORGANIC);
		this.setName(registryName);
		this.setHardness(LEAVES.getHardness());
		this.sounds(GRASS_SOUNDS);
		this.setBoundingBox(0.125F, 0F, 0.125F, 0.875F, 0.875F, 0.875F);
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int id) {
		return new PlaceableTileEntityWithMeta(id);
	}
	
	@Override
	protected int droppedMeta(int meta) {
		return clampMeta(meta);
	}
	
	protected TileEntityBase createTileEntity() {
		return new CocoonSpawner();
	}

	@Override
	public CustomModel getCustomInventoryModel(int meta) {
		return null;
	}

	@Override
	public CustomModel getCustomWorldModel(Level level, int x, int y, int z, int meta) {
		int state = MHelper.getRandomHash(y, x, z) & 3;
		String texture = TEXTURES[clampMeta(meta)];
		return ModelListener.getBlockModel("cocoon_" + texture + "_" + state);
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
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	private int clampMeta(int meta) {
		return meta % 3;
	}
}
