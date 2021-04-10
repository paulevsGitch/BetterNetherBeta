package paulevs.bnb.block;

import java.util.Random;

import net.minecraft.block.BlockBase;
import net.minecraft.block.Stairs;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import paulevs.bnb.listeners.TextureListener;

public class NetherStairsBlock extends Stairs {
	private final String texture;
	
	public NetherStairsBlock(String name, int id, BlockBase source) {
		super(id, source);
		this.setName(name);
		texture = name.replace("stairs_", "");
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return TextureListener.getBlockTexture(texture);
	}
	
	@Override
	public int getTextureForSide(int side) {
		return TextureListener.getBlockTexture(texture);
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
