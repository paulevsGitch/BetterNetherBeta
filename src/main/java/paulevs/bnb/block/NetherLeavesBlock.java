package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Shears;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import paulevs.bnb.block.types.NetherLeavesType;
import paulevs.bnb.item.NetherShearsItem;
import paulevs.bnb.util.ItemUtil;

public class NetherLeavesBlock extends MultiBlock {
	public NetherLeavesBlock(String name, int id) {
		super(name, id, Material.LEAVES, NetherLeavesType.class);
		this.setHardness(LEAVES.getHardness());
		this.disableNotifyOnMetaDataChange();
		this.sounds(GRASS_SOUNDS);
		this.disableStat();
	}
	
	@Override
	public void afterBreak(Level level, PlayerBase player, int x, int y, int z, int meta) {
		ItemBase item = player.getHeldItem() == null ? null : ItemUtil.itemByID(player.getHeldItem().itemId);
		if (!level.isClient && item != null && (item instanceof Shears || item instanceof NetherShearsItem)) {
			this.drop(level, x, y, z, new ItemInstance(this.id, 1, 0));
		}
		else {
			super.afterBreak(level, player, x, y, z, meta);
		}
	}
	
	public boolean isFullOpaque() {
		return false;
	}
	
	@Environment(EnvType.CLIENT)
	public boolean method_1618(TileView world, int x, int y, int z, int side) {
		if (world.getTileMeta(x, y, z) != NetherLeavesType.EMBER_LEAVES.getMeta()) {
			return super.method_1618(world, x, y, z, side);
		}
		return world.getTileId(x, y, z) == this.id ? true : super.method_1618(world, x, y, z, side);
	}
}
