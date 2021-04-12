package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import paulevs.bnb.util.BlockDirection;

public class SlabPlaceableTileEntity extends PlaceableTileEntity {
	private BlockBase block;
	
	public SlabPlaceableTileEntity(int id, BlockBase block) {
		super(id);
		this.block = block;
	}
	
	@Override
	public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
		if (item.count == 0) {
			return false;
		}
		
		BlockDirection dir = BlockDirection.fromFacing(facing);
		
		int meta = dir.invert().getFacing();
		if (level.getTileId(x, y, z) == block.id && level.getTileMeta(x, y, z) == meta) {
			level.placeBlockWithMetaData(x, y, z, block.id, 6);
			place(item, player, level, x, y, z, 1);
			return true;
		}
		
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		
		if (!level.canPlaceTile(block.id, x, y, z, false, facing)) {
			return false;
		}
		
		if (level.placeBlockWithMetaData(x, y, z, block.id, meta)) {
			place(item, player, level, x, y, z, meta);
		}

		return true;
	}
	
	private void place(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int meta) {
		block.onBlockPlaced(level, x, y, z, meta);
		block.afterPlaced(level, x, y, z, player);
		level.playSound(x + 0.5, y + 0.5, z + 0.5, block.sounds.getWalkSound(), (block.sounds.getVolume() + 1.0F) / 2.0F, block.sounds.getPitch() * 0.8F);
		item.count--;
	}
}
