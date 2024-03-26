package paulevs.bnb.achievement;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.RegisteringStat;
import net.minecraft.stat.Stat;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.item.BNBItems;

import java.util.ArrayList;
import java.util.List;

public class BNBAchievements {
	public static final List<Achievement> ACHIEVEMENTS = new ArrayList<>();
	private static int achievementID = 10000;
	
	public static final Achievement THE_WAYS = make("the_ways", Block.PORTAL, 0, 0, null);
	public static final Achievement WARM_WELCOME = make("warm_welcome", Block.FIRE, 2, 0, THE_WAYS);
	public static final Achievement RGB = make("rgb", BNBBlocks.WARPED_LOG, 1, 2, WARM_WELCOME);
	public static final Achievement ALMOST_THE_SAME = make("almost_the_same", BNBBlocks.NETHERRACK_FURNACE, 3, 2, WARM_WELCOME);
	public static final Achievement ARCHIMEDES_LAW = make("archimedes_law", BNBItems.OBSIDIAN_BOAT, 1, -2, WARM_WELCOME);
	public static final Achievement ORICHALCUM = make("orichalcum", BNBItems.ORICHALCUM_INGOT, 5, 1, WARM_WELCOME);
	public static final Achievement ARIADNES_STRING = make("ariadnes_string", BNBItems.PORTAL_COMPASS, 4, 4, ORICHALCUM);
	
	public static final Stat COLLECT_FALUN_LOG = new RegisteringStat(9990, "stat.bnb:collectRedLog").register();
	public static final Stat COLLECT_WARPED_LOG = new RegisteringStat(9991, "stat.bnb:collectRedLog").register();
	public static final Stat COLLECT_POISON_LOG = new RegisteringStat(9992, "stat.bnb:collectRedLog").register();
	
	private static final ItemStack[] RGB_ICONS = new ItemStack[] {
		new ItemStack(BNBBlocks.FALUN_LOG),
		new ItemStack(BNBBlocks.WARPED_LOG),
		new ItemStack(BNBBlocks.POISON_LOG)
	};
	
	private static Achievement make(String name, Block icon, int x, int y, Achievement parent) {
		Achievement achievement = new Achievement(achievementID++, "bnb:" + name, x, y, icon, parent);
		ACHIEVEMENTS.add(achievement);
		return achievement;
	}
	
	private static Achievement make(String name, Item icon, int x, int y, Achievement parent) {
		Achievement achievement = new Achievement(achievementID++, "bnb:" + name, x, y, icon, parent);
		ACHIEVEMENTS.add(achievement);
		return achievement;
	}
	
	public static int readStat(Stat stat) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			return getStatClient(stat);
		}
		// No way to read from server, not handling
		return 0;
	}
	
	@SuppressWarnings("unchecked")
	private static int getStatClient(Stat stat) {
		return (int) BNBClient.getMinecraft().statFileWriter.getHashMapOfStats().getOrDefault(stat, 0);
	}
	
	public static ItemStack getRGBIcon() {
		int index = (int) (((System.currentTimeMillis()) / 500) % 3);
		return RGB_ICONS[index];
	}
}
