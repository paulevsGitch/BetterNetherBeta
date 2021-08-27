package paulevs.bnb.util;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.block.SoulSoilBlock;

public class BlockUtil {
	private static boolean requireLight;
	private static boolean lightPass;
	private static int breakStage;
	
	public static boolean isTerrain(int id) {
		return id == BlockBase.NETHERRACK.id || blockByID(id) instanceof NetherTerrainBlock;
	}
	
	public static boolean isSoulTerrain(int id) {
		return id == BlockBase.SOUL_SAND.id || blockByID(id) instanceof SoulSoilBlock;
	}
	
	public static boolean isNonSolid(int tile) {
		return tile == 0 || blockByID(tile) == null || blockByID(tile).material.isReplaceable();
	}
	
	public static boolean isNonSolidNoLava(int tile) {
		return tile != BlockBase.STILL_LAVA.id && tile != BlockBase.FLOWING_LAVA.id && isNonSolid(tile);
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
}
