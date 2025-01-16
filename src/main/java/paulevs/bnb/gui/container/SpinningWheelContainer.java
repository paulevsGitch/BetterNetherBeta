package paulevs.bnb.gui.container;

import net.minecraft.container.Container;
import net.minecraft.container.slot.Slot;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.PlayerInventory;
import net.minecraft.item.ItemStack;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.entity.SpinningWheelBlockEntity;
import paulevs.bnb.item.BNBItemTags;

public class SpinningWheelContainer extends Container {
	public final SpinningWheelBlockEntity entity;
	
	public SpinningWheelContainer(PlayerInventory playerInventory, SpinningWheelBlockEntity entity) {
		this.entity = entity;
		
		// Input
		for (byte i = 0; i < 4; ++i) {
			int x = (i & 1) * 18 + 25;
			int y = (i >> 1) * 18 + 24;
			addSlot(new Slot(entity, i, x, y));
		}
		
		// Output
		for (byte i = 0; i < 4; ++i) {
			int x = (i & 1) * 18 + 116;
			int y = (i >> 1) * 18 + 24;
			addSlot(new Slot(entity, i + 4, x, y));
		}
		
		// Player inventory
		for (byte row = 0; row < 3; ++row) {
			for (byte column = 0; column < 9; ++column) {
				addSlot(new Slot(playerInventory, column + row * 9 + 9, 8 + column * 18, 84 + row * 18));
			}
		}
		
		// Player hotbar
		for (byte i = 0; i < 9; ++i) {
			addSlot(new Slot(playerInventory, i, 8 + i * 18, 142));
		}
	}
	
	@Override
	public boolean canUse(PlayerEntity arg) {
		return true;
	}
	
	@Override
	public ItemStack transferSlot(int slotIndex) {
		ItemStack result = null;
		Slot source = (Slot) slots.get(slotIndex);
		
		if (source != null && source.hasItem()) {
			ItemStack stored = source.getItem();
			result = stored.copy();
			
			if (slotIndex > 7 && stored.isIn(BNBItemTags.NETHER_FIBER_SOURCE)) insertItem(stored, 0, 4, false);
			else if (slotIndex < 8) insertItem(stored, 8, slots.size(), false);
			
			if (stored.count == 0) source.setStack(null);
			else source.markDirty();
			
			if (stored.count == result.count) {
				return null;
			}
			
			source.onCrafted(stored);
		}
		
		return result;
	}
	
	@Override
	public ItemStack clickSlot(int slotIndex, int clickType, boolean shift, PlayerEntity player) {
		if (slotIndex > 3 && slotIndex < 8) {
			ItemStack stored = ((Slot) slots.get(slotIndex)).getItem();
			if (stored != null) BNBAchievements.craftAchievement(player, stored.getType());
		}
		return super.clickSlot(slotIndex, clickType, shift, player);
	}
	
	/*@Override
	@Environment(EnvType.SERVER)
	public void addPlayer(PlayerEntity player, boolean remove) {
		super.addPlayer(player, remove);
		System.out.println(player.name + " " + remove);
		if (!remove) {
			System.out.println("Sending to " + player.name);
			PacketHelper.sendTo(player, new SpinningWheelPacket(entity.x, entity.y, entity.z, entity.getProcess()));
		}
	}*/
	
	/*@Override
	public boolean canOpen(PlayerEntity player) {
		System.out.println("Test " + player.name);
		return super.canOpen(player);
	}*/
}
