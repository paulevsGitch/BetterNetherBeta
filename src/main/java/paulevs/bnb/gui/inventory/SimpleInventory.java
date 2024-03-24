package paulevs.bnb.gui.inventory;

import net.minecraft.container.Container;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;

public class SimpleInventory implements Inventory {
	private final Container container;
	private final ItemStack[] items;
	
	public SimpleInventory(ItemStack[] items, Container container) {
		this.container = container;
		this.items = items;
	}
	
	public SimpleInventory(int size, Container container) {
		this(new ItemStack[size], container);
	}
	
	@Override
	public int getInventorySize() {
		return items.length;
	}
	
	@Override
	public ItemStack getItem(int index) {
		return items[index];
	}
	
	@Override
	public ItemStack takeItem(int index, int count) {
		if (items[index] != null) {
			if (items[index].count <= count) {
				ItemStack itemStack = items[index];
				items[index] = null;
				container.onContentsChanged(this);
				return itemStack;
			}
			ItemStack itemStack = items[index].split(count);
			if (items[index].count == 0) {
				items[index] = null;
				container.onContentsChanged(this);
			}
			return itemStack;
		}
		return null;
	}
	
	@Override
	public void setItem(int index, ItemStack stack) {
		items[index] = stack;
		container.onContentsChanged(this);
	}
	
	@Override
	public String getInventoryName() {
		return "Simple";
	}
	
	@Override
	public int getMaxStackSize() {
		return 64;
	}
	
	@Override
	public void markDirty() {}
	
	@Override
	public boolean canPlayerUse(PlayerEntity arg) {
		return true;
	}
	
	public ItemStack addItem(ItemStack stack) {
		for (ItemStack stored : items) {
			if (stored == null || !stored.isDamageAndIDIdentical(stack)) continue;
			if (stored.count == stored.getMaxStackSize()) continue;
			int add = Math.min(stack.count, stored.getMaxStackSize() - stored.count);
			stack.count -= add;
			stored.count += add;
			if (stack.count < 1) return stack;
		}
		
		for (int i = 0; i < items.length; i++) {
			if (items[i] != null) continue;
			items[i] = stack.copy();
			stack.count = 0;
			return stack;
		}
		
		return stack;
	}
}
