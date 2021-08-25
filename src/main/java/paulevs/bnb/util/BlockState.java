package paulevs.bnb.util;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import paulevs.bnb.block.MultiBlock;
import paulevs.bnb.interfaces.BlockEnum;

import java.util.Locale;

public class BlockState {
	private int tile;
	private int meta;
	
	public BlockState(int id) {
		this(id, 0);
	}
	
	public BlockState(BlockBase block) {
		this(block, 0);
	}
	
	public BlockState(int id, int meta) {
		this.setBlockID(id);
		this.setBlockMeta(meta);
	}
	
	public BlockState(BlockBase block, int meta) {
		this(block.id, meta);
	}
	
	public BlockState(MultiBlock block, BlockEnum variant) {
		this(block.id, variant.getMeta());
	}

	public int getBlockID() {
		return tile;
	}

	public void setBlockID(int blockID) {
		this.tile = blockID;
	}

	public int getMeta() {
		return meta;
	}

	public void setBlockMeta(int meta) {
		this.meta = meta;
	}
	
	public void setBlock(Level level, int x, int y, int z) {
		level.setTileWithMetadata(x, y, z, tile, meta);
	}
	
	public void setBlockAndUpdate(Level level, int x, int y, int z) {
		level.placeBlockWithMetaData(x, y, z, tile, meta);
	}
	
	public void setBlock(Chunk chunk, int x, int y, int z) {
		chunk.setTileWithMetadata(x, y, z, tile, meta);
	}
	
	public BlockBase getBlock() {
		return BlockUtil.blockByID(tile);
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) {
			return true;
		}
		BlockState state = (BlockState) obj;
		return state == null ? false : state.tile == this.tile && state.meta == this.meta;
	}
	
	@Override
	public String toString() {
		return String.format(Locale.ROOT, "[%d:%d]", tile, meta);
	}
	
	@Override
	public int hashCode() {
		return tile << 4 | meta;
	}
}
