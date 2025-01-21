package paulevs.bnb.item;

import net.minecraft.item.Item;
import net.minecraft.item.material.ToolMaterial;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.item.tool.ToolLevel;
import net.modificationstation.stationapi.api.item.tool.ToolMaterialFactory;
import net.modificationstation.stationapi.api.template.item.TemplateAxeItem;
import net.modificationstation.stationapi.api.template.item.TemplateFoodItem;
import net.modificationstation.stationapi.api.template.item.TemplateHoeItem;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.template.item.TemplatePickaxeItem;
import net.modificationstation.stationapi.api.template.item.TemplateShearsItem;
import net.modificationstation.stationapi.api.template.item.TemplateShovelItem;
import net.modificationstation.stationapi.api.template.item.TemplateSwordItem;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.world.structure.BNBStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

@SuppressWarnings("unused")
public class BNBItems {
	public static final List<Item> ITEMS = new ArrayList<>();
	
	private static final ToolMaterial ORICHALCUM = ToolMaterialFactory.create("bnb_orichalcum", 2, 750, 6.0F, 2).toolLevel(ToolLevel.getNumeric(2));
	private static final ToolMaterial OBSIDIAN = ToolMaterialFactory.create("bnb_obsidian", 1, 786, 4.0F, 1).toolLevel(ToolLevel.getNumeric(1));
	private static final ToolMaterial NETHERRACK = ToolMaterialFactory.create("bnb_netherrack", 1, 100, 2.5F, 1).toolLevel(ToolLevel.getNumeric(1));
	
	public static final Item FALURIAN_TREE_PLACER = make(
		"falurian_tree_placer", () -> BNBStructures.FALURIAN_TREE, StructurePlacerItem::new
	);
	public static final Item PIROZEN_TREE_PLACER = make(
		"pirozen_tree_placer", () -> BNBStructures.PIROZEN_TREE, StructurePlacerItem::new
	);
	public static final Item CHLOROPHATE_TREE_PLACER = make(
		"chlorophate_tree_placer", () -> BNBStructures.CHLOROPHATE_TREE, StructurePlacerItem::new
	);
	public static final Item JALUMINE_TREE_PLACER = make(
		"jalumine_tree_placer", () -> BNBStructures.JALUMINE_TREE, StructurePlacerItem::new
	);
	
	public static final Item LARGE_FALURIAN_TREE_PLACER = make(
		"large_falurian_tree_placer", () -> BNBStructures.LARGE_FALURIAN_TREE, StructurePlacerItem::new
	);
	public static final Item LARGE_PIROZEN_TREE_PLACER = make(
		"large_pirozen_tree_placer", () -> BNBStructures.LARGE_PIROZEN_TREE, StructurePlacerItem::new
	);
	public static final Item LARGE_CHLOROPHATE_TREE_PLACER = make(
		"large_chlorophate_tree_placer", () -> BNBStructures.LARGE_CHLOROPHATE_TREE, StructurePlacerItem::new
	);
	
	public static final Item FALURIAN_VINE_BERRIES = makeFood("falurian_vine_berries", 1, false).setMaxStackSize(8);
	
	public static final Item ORICHALCUM_INGOT = make("orichalcum_ingot", TemplateItem::new);
	
	public static final Item ORICHALCUM_HELMET = makeArmor("orichalcum_helmet", 3, 2, 0);
	public static final Item ORICHALCUM_CHESTPLATE = makeArmor("orichalcum_chestplate", 3, 2, 1);
	public static final Item ORICHALCUM_LEGGINGS = makeArmor("orichalcum_leggings", 3, 2, 2);
	public static final Item ORICHALCUM_BOOTS = makeArmor("orichalcum_boots", 3, 2, 3);
	
	public static final Item ORICHALCUM_SWORD = makeTool("orichalcum_sword", TemplateSwordItem::new, ORICHALCUM);
	public static final Item ORICHALCUM_SHOVEL = makeTool("orichalcum_shovel", TemplateShovelItem::new, ORICHALCUM);
	public static final Item ORICHALCUM_PICKAXE = makeTool("orichalcum_pickaxe", TemplatePickaxeItem::new, ORICHALCUM);
	public static final Item ORICHALCUM_AXE = makeTool("orichalcum_axe", TemplateAxeItem::new, ORICHALCUM);
	public static final Item ORICHALCUM_HOE = makeTool("orichalcum_hoe", TemplateHoeItem::new, ORICHALCUM);
	public static final Item ORICHALCUM_SHEARS = makeShears("orichalcum_shears", ORICHALCUM);
	public static final Item ORICHALCUM_IGNITER = make("orichalcum_igniter", OrichalcumIgniter::new);
	
	public static final Item OBSIDIAN_BOAT = make("obsidian_boat", ObsidianBoatItem::new);
	public static final Item OBSIDIAN_SHARD = make("obsidian_shard", TemplateItem::new);
	
	public static final Item OBSIDIAN_SWORD = makeTool("obsidian_sword", TemplateSwordItem::new, OBSIDIAN);
	public static final Item OBSIDIAN_SHOVEL = makeTool("obsidian_shovel", ObsidianShovelItem::new, OBSIDIAN);
	public static final Item OBSIDIAN_PICKAXE = makeTool("obsidian_pickaxe", ObsidianPickaxeItem::new, OBSIDIAN);
	public static final Item OBSIDIAN_AXE = makeTool("obsidian_axe", TemplateAxeItem::new, OBSIDIAN);
	public static final Item OBSIDIAN_HOE = makeTool("obsidian_hoe", TemplateHoeItem::new, OBSIDIAN);
	
	public static final Item NETHERRACK_SWORD = makeTool("netherrack_sword", TemplateSwordItem::new, NETHERRACK);
	public static final Item NETHERRACK_SHOVEL = makeTool("netherrack_shovel", TemplateShovelItem::new, NETHERRACK);
	public static final Item NETHERRACK_PICKAXE = makeTool("netherrack_pickaxe", TemplatePickaxeItem::new, NETHERRACK);
	public static final Item NETHERRACK_AXE = makeTool("netherrack_axe", TemplateAxeItem::new, NETHERRACK);
	public static final Item NETHERRACK_HOE = makeTool("netherrack_hoe", TemplateHoeItem::new, NETHERRACK);
	
	public static final Item NETHER_FIBER = make("nether_fiber", TemplateItem::new);
	
	public static final Item FIBER_HELMET = makeArmor(
		"fiber_helmet", 0, 1, 0, "tooltip.bnb.fiber_cloth_1", "tooltip.bnb.fiber_cloth_2"
	).setDurability(2000);
	public static final Item FIBER_CHESTPLATE = makeArmor(
		"fiber_chestplate", 0, 1, 1, "tooltip.bnb.fiber_cloth_1", "tooltip.bnb.fiber_cloth_2"
	).setDurability(2000);
	public static final Item FIBER_LEGGINGS = makeArmor(
		"fiber_leggings", 0, 1, 2, "tooltip.bnb.fiber_cloth_1", "tooltip.bnb.fiber_cloth_2"
	).setDurability(2000);
	public static final Item FIBER_BOOTS = makeArmor(
		"fiber_boots", 0, 1, 3, "tooltip.bnb.fiber_cloth_1", "tooltip.bnb.fiber_cloth_2"
	).setDurability(2000);
	
	public static final Item PORTAL_COMPASS = make("portal_compass", PortalCompassItem::new);
	public static final Item NETHER_HYGROMETER = make("nether_hygrometer", NetherHygrometerItem::new);
	
	public static final Item NETHERRACK_BRICK = make("netherrack_brick", TemplateItem::new);
	
	public static final Item FALURIAN_MOSS_COVER = make("nether_moss_cover", MossCoverItem::new);
	public static final Item AMETRINE_SHARD = make("ametrine_shard", TemplateItem::new);
	public static final Item ASH = make("ash", TemplateItem::new);
	
	private static Item makeFood(String name, int healAmount, boolean isWolfFood) {
		Identifier id = BNB.id(name);
		Item item = new TemplateFoodItem(id, healAmount, isWolfFood);
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	private static Item makeArmor(String name, int level, int protection, int slot) {
		Identifier id = BNB.id(name);
		Item item = new NetherArmorItem(id, level, protection, slot);
		ITEMS.add(item);
		return item;
	}
	
	private static Item makeArmor(String name, int level, int protection, int slot, String... tooltip) {
		Identifier id = BNB.id(name);
		Item item = new NetherArmorTooltipItem(id, level, protection, slot, tooltip);
		ITEMS.add(item);
		return item;
	}
	
	private static Item makeTool(String name, BiFunction<Identifier, ToolMaterial, Item> constructor, ToolMaterial material) {
		Identifier id = BNB.id(name);
		Item item = constructor.apply(id, material);
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	private static Item makeShears(String name, ToolMaterial material) {
		Identifier id = BNB.id(name);
		TemplateShearsItem item = new TemplateShearsItem(id);
		item.setDurability(material.getDurability());
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	private static Item make(String name, Function<Identifier, Item> constructor) {
		Identifier id = BNB.id(name);
		Item item = constructor.apply(id);
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	private static Item make(String name, Supplier<Structure> structure, BiFunction<Identifier, Supplier<Structure>, Item> constructor) {
		Identifier id = BNB.id(name);
		Item item = constructor.apply(id, structure);
		item.setTranslationKey(id);
		ITEMS.add(item);
		return item;
	}
	
	public static void init() {
		BNBBlocks.FALURIAN_VINE.setCollectableItem(FALURIAN_VINE_BERRIES);
	}
}
