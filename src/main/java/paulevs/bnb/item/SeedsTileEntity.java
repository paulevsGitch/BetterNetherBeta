package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import paulevs.bnb.util.BlockDirection;

public class SeedsTileEntity extends SimpleNetherItem {
	private BlockBase block;
	
	public SeedsTileEntity(String name, int id, BlockBase block) {
		super(name, id);
		this.block = block;
	}
	
	@Override
	public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
		BlockDirection dir = BlockDirection.fromFacing(facing);
		x += dir.getX();
		y += dir.getY();
		z += dir.getZ();
		if (block.canPlaceAt(level, x, y, z)) {
			if (level.placeBlockWithMetaData(x, y, z, block.id, 0)) {
				place(item, player, level, x, y, z, 0);
				return true;
			}
		}
		return false;
	}
	
	private void place(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int meta) {
		block.onBlockPlaced(level, x, y, z, meta);
		block.afterPlaced(level, x, y, z, player);
		level.playSound(x + 0.5, y + 0.5, z + 0.5, block.sounds.getWalkSound(), (block.sounds.getVolume() + 1.0F) / 2.0F, block.sounds.getPitch() * 0.8F);
		item.count--;
	}
}
