package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Shears;
import net.minecraft.level.Level;
import paulevs.bnb.block.types.NetherLeavesType;
import paulevs.bnb.item.NetherShearsItem;
import paulevs.bnb.util.ItemUtil;

public class NetherLeavesBlock extends MultiBlock {
	//private static final Set<Vec3i> POSITIONS = Sets.newHashSet();
	//private static final Set<Vec3i> NEW_ENDS = Sets.newHashSet();
	//private static final Set<Vec3i> ENDS = Sets.newHashSet();
	
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
}
