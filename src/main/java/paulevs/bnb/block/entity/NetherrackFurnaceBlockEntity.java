package paulevs.bnb.block.entity;

import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.client.resource.language.I18n;

public class NetherrackFurnaceBlockEntity extends FurnaceBlockEntity {
	public String getInventoryName() {
		return I18n.translate("gui.bnb:netherrack_furnace");
	}
}
