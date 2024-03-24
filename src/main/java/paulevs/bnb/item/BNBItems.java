package paulevs.bnb.item;

import net.minecraft.item.Item;
import net.minecraft.item.material.ToolMaterial;
import net.minecraft.level.structure.Structure;
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

public class BNBItems {
	public static final List<Item> ITEMS = new ArrayList<>();
	
	private static final ToolMaterial ORICHALCUM = ToolMaterialFactory.create("bnb_orichalcum", 2, 750, 6.0F, 2);
	
	public static final Item CRIMSON_TREE_PLACER = make(
		"crimson_tree_placer", () -> BNBStructures.CRIMSON_TREE, StructurePlacerItem::new
	);
	public static final Item WARPED_TREE_PLACER = make(
		"warped_tree_placer", () -> BNBStructures.WARPED_TREE, StructurePlacerItem::new
	);
	public static final Item POISON_TREE_PLACER = make(
		"poison_tree_placer", () -> BNBStructures.POISON_TREE, StructurePlacerItem::new
	);
	
	public static final Item LARGE_CRIMSON_TREE_PLACER = make(
		"large_crimson_tree_placer", () -> BNBStructures.LARGE_CRIMSON_TREE, StructurePlacerItem::new
	);
	public static final Item LARGE_WARPED_TREE_PLACER = make(
		"large_warped_tree_placer", () -> BNBStructures.LARGE_WARPED_TREE, StructurePlacerItem::new
	);
	public static final Item LARGE_POISON_TREE_PLACER = make(
		"large_poison_tree_placer", () -> BNBStructures.LARGE_POISON_TREE, StructurePlacerItem::new
	);
	
	public static final Item CRIMSON_VINE_BERRIES = makeFood("crimson_vine_berries", 1, false).setMaxStackSize(8);
	
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
	
	public static final Item OBSIDIAN_BOAT = make("obsidian_boat", ObsidianBoatItem::new);
	
	public static final Item NETHER_FIBER = make("nether_fiber", TemplateItem::new);
	
	public static final Item FIBER_HELMET = makeArmor("fiber_helmet", 0, 1, 0).setDurability(2000);
	public static final Item FIBER_CHESTPLATE = makeArmor("fiber_chestplate", 0, 1, 1).setDurability(2000);
	public static final Item FIBER_LEGGINGS = makeArmor("fiber_leggings", 0, 1, 2).setDurability(2000);
	public static final Item FIBER_BOOTS = makeArmor("fiber_boots", 0, 1, 3).setDurability(2000);
	
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
		item.setTranslationKey(id.toString());
		ITEMS.add(item);
		return item;
	}
	
	private static Item make(String name, Supplier<Structure> structure, BiFunction<Identifier, Supplier<Structure>, Item> constructor) {
		Identifier id = BNB.id(name);
		Item item = constructor.apply(id, structure);
		item.setTranslationKey(id.toString());
		ITEMS.add(item);
		return item;
	}
	
	public static void init() {
		BNBBlocks.CRIMSON_VINE_WITH_BERRIES.setCollectableItem(CRIMSON_VINE_BERRIES);
	}
}
