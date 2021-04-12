package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import paulevs.bnb.interfaces.RenderTypePerMeta;
import paulevs.bnb.item.SlabPlaceableTileEntity;
import paulevs.bnb.util.BlockDirection;

public class NetherSlabBlock extends NetherBlock implements BlockItemProvider, RenderTypePerMeta {
	private final BlockBase source;
	private final int sourceMeta;
	
	public NetherSlabBlock(String name, int id, BlockBase source, int sourceMeta) {
		super(name, id, source.material);
		this.setBlastResistance(source.getBlastResistance(null) * 0.5F);
		this.setHardness(source.getHardness() * 0.5F);
		this.sounds(source.sounds);
		this.source = source;
		this.sourceMeta = sourceMeta;
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return source.getTextureForSide(side, sourceMeta);
	}
	
	@Environment(EnvType.CLIENT)
	public void method_1605() {
		this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}
	
	@Override
	public void method_1616(TileView world, int x, int y, int z) {
		int meta = world.getTileMeta(x, y, z);
		if ((meta & 7) == 6) {
			this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			return;
		}
		BlockDirection dir = BlockDirection.fromFacing(meta);
		float x1 = dir.getX() < 1 ? 0.0F : 0.5F;
		float y1 = dir.getY() < 1 ? 0.0F : 0.5F;
		float z1 = dir.getZ() < 1 ? 0.0F : 0.5F;
		float x2 = dir.getX() > -1 ? 1.0F : 0.5F;
		float y2 = dir.getY() > -1 ? 1.0F : 0.5F;
		float z2 = dir.getZ() > -1 ? 1.0F : 0.5F;
		this.setBoundingBox(x1, y1, z1, x2, y2, z2);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		this.method_1616(level, x, y, z);
		return super.getCollisionShape(level, x, y, z); 
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int id) {
		return new SlabPlaceableTileEntity(id, this);
	}
	
	@Override
	public int droppedMeta(int meta) {
		return 0;
	}

	@Override
	public int getTypeByMeta(int meta) {
		return 0;
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
	@Environment(EnvType.CLIENT)
	public boolean method_1618(TileView world, int x, int y, int z, int side) {
		return true;
	}
	
	@Override
	public void beforeDestroyedByExplosion(Level level, int x, int y, int z, int meta, float dropChance) {
		if (!level.isClient) {
			int count = meta == 6 ? 2 : 1;
			for(int i = 0; i < count; ++i) {
				if (!(level.rand.nextFloat() > dropChance)) {
					this.drop(level, x, y, z, new ItemInstance(id, 1, 0));
				}
			}
		}
	}
}
