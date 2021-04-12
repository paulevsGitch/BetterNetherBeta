package paulevs.bnb.item;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import paulevs.bnb.item.material.NetherToolMaterial;

public class NetherHoeItem extends NetherToolItem {
	public NetherHoeItem(String name, int id, NetherToolMaterial material) {
		super(name, id, material);
	}

	@Override
	public boolean isEffectiveOn(BlockBase tile) {
		return isEffectiveOn(ironHoe, tile);
	}

	@Override
	public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
		int tile = level.getTileId(x, y, z);
		int above = level.getTileId(x, y + 1, z);
		if ((facing == 0 || above != 0 || tile != BlockBase.GRASS.id) && tile != BlockBase.DIRT.id) {
			return false;
		}
		else {
			BlockBase block = BlockBase.FARMLAND;
			level.playSound(x + 0.5, y + 0.5, z + 0.5, block.sounds.getWalkSound(), (block.sounds.getVolume() + 1.0F) / 2.0F, block.sounds.getPitch() * 0.8F);
			if (level.isClient) {
				return true;
			}
			else {
				level.setTile(x, y, z, block.id);
				item.applyDamage(1, player);
				return true;
			}
		}
	}
}
