package paulevs.bnb.item;

import net.minecraft.item.BaseItem;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.food.TemplateFoodBase;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.world.structures.BNBStructures;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

public class BNBItems {
	public static final List<BaseItem> ITEMS = new ArrayList<>();
	
	public static final BaseItem CRIMSON_TREE_PLACER = make(
		"crimson_tree_placer", () -> BNBStructures.CRIMSON_TREE, StructurePlacerItem::new
	);
	public static final BaseItem CRIMSON_VINE_BERRIES = makeFood("crimson_vine_berries", 1, false).setMaxStackSize(8);
	
	private static BaseItem makeFood(String name, int healAmount, boolean isWolfFood) {
		Identifier id = BNB.id(name);
		TemplateFoodBase item = new TemplateFoodBase(id, healAmount, isWolfFood);
		item.setTranslationKey(id.toString());
		ITEMS.add(item);
		return item;
	}
	
	private static BaseItem make(String name, Function<Identifier, BaseItem> constructor) {
		Identifier id = BNB.id(name);
		BaseItem item = constructor.apply(id);
		item.setTranslationKey(id.toString());
		ITEMS.add(item);
		return item;
	}
	
	private static BaseItem make(String name, Supplier<Structure> structure, BiFunction<Identifier, Supplier<Structure>, BaseItem> constructor) {
		Identifier id = BNB.id(name);
		BaseItem item = constructor.apply(id, structure);
		item.setTranslationKey(id.toString());
		ITEMS.add(item);
		return item;
	}
	
	public static void init() {
		BNBBlocks.CRIMSON_VINE_WITH_BERRIES.setCollectableItem(CRIMSON_VINE_BERRIES);
	}
}
