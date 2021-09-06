package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import paulevs.bnb.block.WallNetherPlantBlock;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.BlockUtil;

public class WallPlantPlaceableTileEntity extends PlaceableTileEntity {
	private BlockBase block;
	
	public WallPlantPlaceableTileEntity(int id, BlockBase block) {
		super(id);
		this.block = block;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getBlockTexture(block.getName().substring(block.getName().indexOf(':') + 1) + "_item");
	}
	
	@Override
	public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
		if (item.count == 0) {
			return false;
		}
		
		BlockDirection dir = BlockDirection.fromFacing(facing);
		if (dir.getY() != 0) {
			return false;
		}
		
		if (!((WallNetherPlantBlock) block).isWall(level.getTileId(x, y, z))) {
			return false;
		}
		
		x += dir.getX();
		z += dir.getZ();
		BlockBase wall = BlockUtil.getBlock(level, x, y, z);
		if (wall != null && !wall.material.isReplaceable()) {
			return false;
		}
		
		int meta = dir.invert().getFacing();
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
