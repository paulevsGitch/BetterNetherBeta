package paulevs.bnb.gui.container;

import net.minecraft.container.Container;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.PlayerInventory;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.gui.inventory.SimpleInventory;

public class SpinningWheelContainer extends Container {
	private final SimpleInventory input;
	private final SimpleInventory output;
	
	public SpinningWheelContainer(PlayerInventory inventory, SpinningWheelBlockEntity entity) {
		input = new SimpleInventory(entity.input, this);
		output = new SimpleInventory(entity.output, this);
		
		for (byte i = 0; i < 4; ++i) {
			int x = (i & 1) * 18 + 25;
			int y = (i >> 1) * 18 + 24;
			addSlot(new Slot(input, i, x, y));
		}
		
		for (byte i = 0; i < 4; ++i) {
			int x = (i & 1) * 18 + 116;
			int y = (i >> 1) * 18 + 24;
			addSlot(new Slot(output, i, x, y));
		}
		
		for (byte row = 0; row < 3; ++row) {
			for (byte column = 0; column < 9; ++column) {
				addSlot(new Slot(inventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
			}
		}
		
		for (byte i = 0; i < 9; ++i) {
			addSlot(new Slot(inventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity arg) {
		return true;
	}
	
	/*@Override
	public ItemStack transferSlot(int index) {
		ItemStack stack = super.transferSlot(index);
		return input.addItem(stack);
	}*/
}
