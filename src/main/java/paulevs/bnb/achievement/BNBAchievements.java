package paulevs.bnb.achievement;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.achievement.Achievement;
import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.stat.RegisteringStat;
import net.minecraft.stat.Stat;
import net.modificationstation.stationapi.api.template.achievement.TemplateAchievement;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.item.BNBItems;

import java.util.ArrayList;
import java.util.List;

public class BNBAchievements {
	public static List<Achievement> ACHIEVEMENTS = new ArrayList<>();
	
	public static Achievement THE_WAYS = make("the_ways", Block.PORTAL, 0, 0, null);
	public static Achievement WARM_WELCOME = make("warm_welcome", Block.FIRE, 2, 0, THE_WAYS);
	public static Achievement RGB = make("rgb", BNBBlocks.PIROZEN_LOG, 1, 2, WARM_WELCOME);
	public static Achievement ALMOST_THE_SAME = make("almost_the_same", BNBBlocks.NETHERRACK_FURNACE, 3, 2, WARM_WELCOME);
	public static Achievement FIRE_UPGRADE = make("fire_upgrade", BNBBlocks.NETHERRACK_BRICK_FURNACE, 3, 4, ALMOST_THE_SAME);
	public static Achievement BRICK_BY_BRICK = make("brick_by_brick", BNBItems.NETHERRACK_BRICK, 3, -2, WARM_WELCOME);
	public static Achievement ARCHIMEDES_LAW = make("archimedes_law", BNBItems.OBSIDIAN_BOAT, 1, -2, WARM_WELCOME);
	public static Achievement ORICHALCUM = make("orichalcum", BNBItems.ORICHALCUM_INGOT, 4, 0, WARM_WELCOME);
	public static Achievement ARIADNES_STRING = make("ariadnes_string", BNBItems.PORTAL_COMPASS, 6, 1, ORICHALCUM);
	public static Achievement METEOROLOGY = make("meteorology", BNBItems.NETHER_HYGROMETER, 8, 1, ARIADNES_STRING);
	public static Achievement SPINNING_WHEEL = make("spinning_wheel", BNBBlocks.SPINNING_WHEEL, 6, -1, ORICHALCUM);
	public static Achievement FABRIC_YARN = make("fabric_yarn", BNBItems.NETHER_FIBER, 8, -1, SPINNING_WHEEL);
	
//	public static Stat COLLECT_FALURIAN_LOG = new RegisteringStat(9990, "stat.bnb:collectRedLog").register();
//	public static Stat COLLECT_PIROZEN_LOG = new RegisteringStat(9991, "stat.bnb:collectBlueLog").register();
//	public static Stat COLLECT_CHLOROPHATE_LOG = new RegisteringStat(9992, "stat.bnb:collectGreenLog").register();
	
	private static ItemStack[] RGB_ICONS = new ItemStack[] {
		new ItemStack(BNBBlocks.FALURIAN_LOG),
		new ItemStack(BNBBlocks.PIROZEN_LOG),
		new ItemStack(BNBBlocks.CHLOROPHATE_LOG)
	};
	
	private static Achievement make(String name, Block icon, int x, int y, Achievement parent) {
		Achievement achievement = new TemplateAchievement(BNB.NAMESPACE.id(name), "bnb." + name, x, y, icon, parent);
		ACHIEVEMENTS.add(achievement);
		return achievement;
	}
	
	private static Achievement make(String name, Item icon, int x, int y, Achievement parent) {
		Achievement achievement = new TemplateAchievement(BNB.NAMESPACE.id(name), "bnb." + name, x, y, icon, parent);
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
	
	public static void craftAchievement(PlayerEntity player, Item item) {
		if (item == BNBItems.OBSIDIAN_BOAT) player.incrementStat(ARCHIMEDES_LAW);
		else if (item == BNBItems.PORTAL_COMPASS) player.incrementStat(ARIADNES_STRING);
		else if (item == BNBItems.NETHER_HYGROMETER) player.incrementStat(METEOROLOGY);
		else if (item == BNBItems.NETHER_FIBER) player.incrementStat(FABRIC_YARN);
		else if (item == BNBItems.NETHERRACK_BRICK) player.incrementStat(BRICK_BY_BRICK);
		else if (item == BNBItems.ORICHALCUM_INGOT) player.incrementStat(ORICHALCUM);
		else if (item instanceof BlockItem blockItem) {
			Block block = blockItem.getBlock();
			if (block == BNBBlocks.NETHERRACK_FURNACE) player.incrementStat(ALMOST_THE_SAME);
			else if (block == BNBBlocks.NETHERRACK_BRICK_FURNACE) player.incrementStat(FIRE_UPGRADE);
			else if (block == BNBBlocks.SPINNING_WHEEL) player.incrementStat(SPINNING_WHEEL);
		}
	}
}
