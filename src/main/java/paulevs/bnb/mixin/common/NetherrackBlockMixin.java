package paulevs.bnb.mixin.common;

import net.minecraft.block.Block;
import net.minecraft.block.NetherrackBlock;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import paulevs.bnb.block.NetherTerrainBlock;

@Mixin(NetherrackBlock.class)
public abstract class NetherrackBlockMixin extends Block {
	@Unique private static final Direction[] BNB_DIRECTIONS = new Direction[] {
		Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST
	};
	
	public NetherrackBlockMixin(int i, Material arg) {
		super(i, arg);
	}
	
	@Override
	public boolean onBonemealUse(Level level, int x, int y, int z, BlockState state) {
		if (level.isRemote) return true;
		
		for (byte i = 0; i < 4; i++) {
			byte i2 = (byte) level.random.nextInt(4);
			Direction d = BNB_DIRECTIONS[i2];
			BNB_DIRECTIONS[i2] = BNB_DIRECTIONS[i];
			BNB_DIRECTIONS[i] = d;
		}
		
		for (Direction dir : BNB_DIRECTIONS) {
			int px = x + dir.getOffsetX();
			int pz = z + dir.getOffsetZ();
			BlockState neighbour = level.getBlockState(px, y, pz);
			if (neighbour.getBlock() instanceof NetherTerrainBlock) {
				level.setBlockState(x, y, z, neighbour);
				level.updateBlock(x, y, z);
				return true;
			}
		}
		
		return false;
	}
}
