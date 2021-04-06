package paulevs.bnb.util;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import paulevs.bnb.block.NetherTerrainBlock;

public class BlockUtil {
	private static boolean requireLight;
	private static boolean lightPass;
	private static int breakStage;
	
	public static boolean isTerrain(int id) {
		return id == BlockBase.NETHERRACK.id || blockByID(id) instanceof NetherTerrainBlock;
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
}
