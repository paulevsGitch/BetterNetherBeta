package paulevs.bnb.util;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import paulevs.bnb.block.NetherStoneBlock;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.SoulTerrainBlock;

public class BlockUtil {
	private static boolean requireLight;
	private static boolean lightPass;
	private static int breakStage;
	
	public static boolean isTerrain(int id) {
		if (id == BlockBase.NETHERRACK.id || id == BlockBase.GRAVEL.id || isSoulTerrain(id)) {
			return true;
		}
		BlockBase block = blockByID(id);
		return block instanceof NetherStoneBlock || block instanceof NetherTerrainBlock;
	}
	
	public static boolean isSoulTerrain(int id) {
		return id == BlockBase.SOUL_SAND.id || blockByID(id) instanceof SoulTerrainBlock;
	}
	
	public static boolean isNonSolid(int tile) {
		if (tile == 0) {
			return true;
		}
		BlockBase block = blockByID(tile);
		if (block == null || block.material.isReplaceable() || !block.material.blocksMovement() || block.material == Material.PLANT) {
			return true;
		}
		return false;
	}
	
	public static boolean isNonSolidNoLava(int tile) {
		if (tile == BlockBase.BROWN_MUSHROOM.id || tile == BlockBase.RED_MUSHROOM.id) {
			return true;
		}
		return !isLava(tile) && isNonSolid(tile);
	}
	
	public static boolean isLava(int tile) {
		return tile == BlockBase.STILL_LAVA.id || tile == BlockBase.FLOWING_LAVA.id;
	}
	
	public static void setLightPass(boolean value) {
		lightPass = value;
	}
	
	public static boolean isLightPass() {
		return lightPass;
	}
	
	public static void setRequireLight(boolean value) {
		requireLight = value;
	}
	
	public static boolean requireLight() {
		return requireLight;
	}
	
	public static void setBreakStage(int value) {
		breakStage = value;
	}
	
	public static int getBreakStage() {
		return breakStage;
	}
	
	public static BlockBase blockByID(int id) {
		return BlockBase.BY_ID[id];
	}
	
	public static BlockBase getBlock(Level level, int x, int y, int z) {
		return blockByID(level.getTileId(x, y, z));
	}
	
	public static int getMaxID() {
		return BlockBase.BY_ID.length;
	}
	
	public static boolean isOpaque(int id) {
		return id > 0 && blockByID(id).isFullOpaque();
	}
	
	public static boolean isTopSide(int side) {
		return side == 1;
	}
	
	public static boolean isBottomSide(int side) {
		return side == 0;
	}
	
	public static boolean isHorizontalSide(int side) {
		return side > 1;
	}
	
	public static boolean isSideY(int side) {
		return side < 2;
	}
	
	public static boolean isSideX(int side) {
		return side == 2 || side == 3;
	}
	
	public static boolean isSideZ(int side) {
		return side == 4 || side == 5;
	}
	
	public static void fastTilePlace(Level level, int x, int y, int z, int id, int meta) {
		if (y >= 0 && y < 128) {
			Chunk chunk = level.getChunk(x, z);
			if (chunk != null) {
				fastTilePlace(chunk, x & 15, y, z & 15, id, meta);
			}
		}
	}
	
	public static void fastTilePlace(Chunk chunk, int x, int y, int z, int id, int meta) {
		chunk.tiles[x << 11 | z << 7 | y] = (byte) id;
		chunk.field_957.method_1704(x, y, z, meta);
	}
	
	public static void fastTilePlace(Chunk chunk, int x, int y, int z, int id) {
		chunk.tiles[x << 11 | z << 7 | y] = (byte) id;
	}
	
	public static void updateTile(Level level, int x, int y, int z) {
		level.method_202(x, y, z, x, y, z);
	}
	
	public static void updateArea(Level level, int x1, int y1, int z1, int x2, int y2, int z2) {
		level.method_202(x1, y1, z1, x2, y2, z2);
	}
}
