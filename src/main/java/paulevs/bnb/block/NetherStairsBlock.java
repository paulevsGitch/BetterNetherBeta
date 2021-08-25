package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.Stairs;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;

import java.util.Random;

public class NetherStairsBlock extends Stairs {
	private final BlockBase source;
	private final int sourceMeta;
	
	public NetherStairsBlock(String name, int id, BlockBase source, int sourceMeta) {
		super(id, source);
		this.setName(name);
		this.source = source;
		this.sourceMeta = sourceMeta;
	}
	
	@Override
	public int getTextureForSide(int side) {
		return getTextureForSide(side, sourceMeta);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return source.getTextureForSide(side, sourceMeta);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		return this.getTextureForSide(side, 0);
	}
	
	@Override
	public int getDropId(int meta, Random rand) {
		return this.id;
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
			int var7 = this.getDropCount(level.rand);
			for(int var8 = 0; var8 < var7; ++var8) {
				if (!(level.rand.nextFloat() > dropChance)) {
					int var9 = this.getDropId(meta, level.rand);
					if (var9 > 0) {
						this.drop(level, x, y, z, new ItemInstance(var9, 1, this.droppedMeta(meta)));
					}
				}
			}
		}
	}
}
