package paulevs.bnb.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.inventory.Inventory;
import net.minecraft.item.ItemStack;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import paulevs.bnb.item.BNBItemTags;
import paulevs.bnb.item.BNBItems;

public class SpinningWheelBlockEntity extends BlockEntity implements Inventory {
	private static final int PROCESS_TICKS = 4 * 20;
	private static final int LAST_TICK = PROCESS_TICKS - 1;
	private final ItemStack[] storage = new ItemStack[8];
	private int process;
	
	@Override
	public int getInventorySize() {
		return storage.length;
	}
	
	@Override
	public ItemStack getItem(int slot) {
		markDirty();
		return storage[slot];
	}
	
	@Override
	public ItemStack takeItem(int slot, int count) {
		markDirty();
		return storage[slot].split(count);
	}
	
	@Override
	public void setItem(int slot, ItemStack stack) {
		storage[slot] = stack;
		markDirty();
	}
	
	@Override
	public int getMaxStackSize() {
		return 64;
	}
	
	@Override
	public boolean canPlayerUse(PlayerEntity player) {
		return true;
	}
	
	public String getInventoryName() {
		return I18n.translate("gui.bnb.spinning_wheel");
	}
	
	@Override
	public void tick() {
		if (process > 0) {
			if (process == 1 && !addStack(new ItemStack(BNBItems.NETHER_FIBER))) {
				return;
			}
			process--;
			return;
		}
		
		for (byte i = 0; i < 4; i++) {
			ItemStack stack = getItem(i);
			if (stack == null || stack.count < 1 || !stack.isIn(BNBItemTags.NETHER_FIBER_SOURCE)) continue;
			stack.count--;
			if (stack.count < 1) setItem(i, null);
			process = PROCESS_TICKS;
			return;
		}
	}
	
	@Override
	public void readIdentifyingData(CompoundTag tag) {
		super.readIdentifyingData(tag);
		tagToArray(tag.getListTag("storage"), storage);
		process = tag.getByte("process");
	}
	
	@Override
	public void writeIdentifyingData(CompoundTag tag) {
		super.writeIdentifyingData(tag);
		tag.put("storage", arrayToTag(storage));
		tag.put("process", (byte) process);
	}
	
	@Environment(EnvType.CLIENT)
	public float getProcess() {
		if (process == 0) return 0;
		return 1.0F - (float) process / PROCESS_TICKS;
	}
	
	private ListTag arrayToTag(ItemStack[] items) {
		ListTag tag = new ListTag();
		for (int index = 0; index < items.length; index++) {
			if (items[index] == null || items[index].count < 1) continue;
			CompoundTag compoundTag = new CompoundTag();
			compoundTag.put("Slot", (byte) index);
			items[index].toTag(compoundTag);
			tag.add(compoundTag);
		}
		return tag;
	}
	
	private void tagToArray(ListTag tag, ItemStack[] items) {
		for (int i = 0; i < tag.size(); ++i) {
			CompoundTag itemTag = (CompoundTag) tag.get(i);
			int slot = itemTag.getByte("Slot");
			items[slot] = new ItemStack(itemTag);
		}
	}
	
	private boolean addStack(ItemStack item) {
		for (byte i = 0; i < 4; i++) {
			byte slot = (byte) (i + 4);
			ItemStack stored = getItem(slot);
			if (stored == null || stored.count >= stored.getMaxStackSize()) continue;
			if (stored.isDamageAndIDIdentical(item)) {
				int canStore = stored.getMaxStackSize() - stored.count;
				int store = Math.min(item.count, canStore);
				stored.count += store;
				item.count -= store;
				if (item.count <= 0) return true;
			}
		}
		
		if (item.count <= 0) return true;
		
		for (byte i = 0; i < 4; i++) {
			byte slot = (byte) (i + 4);
			ItemStack stored = getItem(slot);
			if (stored != null) continue;
			setItem(slot, item);
			return true;
		}
		
		return false;
	}
}
