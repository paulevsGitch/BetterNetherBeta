package paulevs.bnb.item;

import net.minecraft.item.Item;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.template.item.TemplateFoodItem;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.world.structures.BNBStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BNBItems {
	public static final List<Item> ITEMS = new ArrayList<>();
	
	public static final Item CRIMSON_TREE_PLACER = make(
		"crimson_tree_placer", () -> BNBStructures.CRIMSON_TREE, StructurePlacerItem::new
	);
	public static final Item WARPED_TREE_PLACER = make(
		"warped_tree_placer", () -> BNBStructures.WARPED_TREE, StructurePlacerItem::new
	);
	public static final Item POISON_TREE_PLACER = make(
		"poison_tree_placer", () -> BNBStructures.POISON_TREE, StructurePlacerItem::new
	);
	public static final Item CRIMSON_VINE_BERRIES = makeFood("crimson_vine_berries", 1, false).setMaxStackSize(8);
	
	private static Item makeFood(String name, int healAmount, boolean isWolfFood) {
		Identifier id = BNB.id(name);
		TemplateFoodItem item = new TemplateFoodItem(id, healAmount, isWolfFood);
		item.setTranslationKey(id.toString());
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
