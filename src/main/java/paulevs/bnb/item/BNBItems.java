package paulevs.bnb.item;

import net.minecraft.item.BaseItem;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.registry.Identifier;
import paulevs.bnb.BNB;
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
	
	public static void init() {}
}
