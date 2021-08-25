package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.tool.Shears;
import net.minecraft.level.Level;
import paulevs.bnb.block.types.NetherLeaves;
import paulevs.bnb.item.NetherShearsItem;
import paulevs.bnb.util.ItemUtil;

public class NetherLeavesBlock extends MultiBlock {
	//private static final Set<Vec3i> POSITIONS = Sets.newHashSet();
	//private static final Set<Vec3i> NEW_ENDS = Sets.newHashSet();
	//private static final Set<Vec3i> ENDS = Sets.newHashSet();
	
	public NetherLeavesBlock(String name, int id) {
		super(name, id, Material.LEAVES, NetherLeaves.class);
		this.setHardness(LEAVES.getHardness());
		this.sounds(GRASS_SOUNDS);
		this.setTicksRandomly(true);
		this.disableNotifyOnMetaDataChange();
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
	
	/*@Override
	protected int clampMeta(int meta) {
		return meta & 7;
	}
	
	@Override
	public void afterPlaced(Level level, int x, int y, int z, Living entity) {
		int meta = level.getTileMeta(x, y, z);
		meta = clampMeta(meta) | 8;
		level.setTileMeta(x, y, z, meta);
	}*/
	
	/*@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		super.onScheduledTick(level, x, y, z, rand);
		if (rand.nextInt(16) > 0) {
			return;
		}
		int meta = level.getTileMeta(x, y, z);
		if (meta > 3) {
			return;
		}
		int dist = getDistance(level, x, y, z, 8, BlockListener.getBlockID("nether_wood"));
		System.out.println("Tick: " + dist);
		if (dist == 8) {
			this.beforeDestroyedByExplosion(level, x, y, z, meta, 1);
			level.placeBlockWithMetaData(x, y, z, 0, 0);
			//level.setTile(x, y, z, 0);
		}
		else {
			//level.setTileMeta(x, y, z, clampMeta(meta) | 4);
			level.placeBlockWithMetaData(x, y, z, id, clampMeta(meta) | 4);
		}
	}*/
	
	/*@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		int meta = level.getTileMeta(x, y, z);
		if (meta > 3 && meta < 8) {
			int dist = getDistance(level, x, y, z, 8, BlockListener.getBlockID("nether_wood"));
			System.out.println("Update: " + dist);
			if (dist == 8) {
				level.setTileMeta(x, y, z, clampMeta(meta));
				//level.placeBlockWithMetaData(x, y, z, id, clampMeta(meta));
			}
		}
	}
	
	private int getDistance(Level level, int x, int y, int z, int depth, int trunkID) {
		POSITIONS.clear();
		NEW_ENDS.clear();
		ENDS.clear();
		ENDS.add(new Vec3i(x, y, z));
		for (int i = 0; i < depth; i++) {
			for (Vec3i pos: ENDS) {
				POSITIONS.add(pos);
				for (BlockDirection dir: BlockDirection.VALUES) {
					Vec3i side = dir.add(pos);
					if (POSITIONS.contains(side)) {
						continue;
					}
					int id = level.getTileId(x, y, z);
					if (id == trunkID) {
						return i;
					}
					else if (id == this.id) {
						NEW_ENDS.add(side);
					}
				}
			}
			POSITIONS.addAll(ENDS);
			ENDS.clear();
			ENDS.addAll(NEW_ENDS);
			NEW_ENDS.clear();
		}
		return depth;
	}*/
}
