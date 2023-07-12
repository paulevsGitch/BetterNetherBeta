package paulevs.bnb.item;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.item.TemplateItemBase;
import net.modificationstation.stationapi.api.util.math.Direction;

import java.util.function.Supplier;

public class StructurePlacerItem extends TemplateItemBase {
	private final Supplier<Structure> structure;
	
	public StructurePlacerItem(Identifier id, Supplier<Structure> structure) {
		super(id);
		this.structure = structure;
	}
	
	@Override
	public boolean useOnBlock(ItemStack item, PlayerBase player, Level level, int x, int y, int z, int face) {
		Direction d = Direction.byId(face);
		boolean result = structure.get().generate(
			level,
			level.random,
			x + d.getOffsetX(),
			y + d.getOffsetY(),
			z + d.getOffsetZ()
		);
		if (result) level.callAreaEvents(x - 16, y - 16, z - 16, x + 16, y + 16, z + 16);
		return result;
	}
}