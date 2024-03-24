package paulevs.bnb.block.entity;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import paulevs.bnb.item.BNBItemTags;
import paulevs.bnb.item.BNBItems;

public class SpinningWheelBlockEntity extends BlockEntity {
	public final ItemStack[] input = new ItemStack[4];
	public final ItemStack[] output = new ItemStack[4];
	private int process;
	
	public String getInventoryName() {
		return I18n.translate("gui.bnb:spinning_wheel");
	}
	
	@Override
	public void tick() {
		process = Math.max(process - 1, 0);
		
		if (process > 0) {
			if (process == 1) {
				for (byte i = 0; i < output.length; i++) {
					ItemStack out = output[i];
					if (out == null) {
						output[i] = new ItemStack(BNBItems.NETHER_FIBER, 1);
						break;
					}
					else if (out.getType() == BNBItems.NETHER_FIBER && out.count < out.getMaxStackSize()) {
						out.count++;
						break;
					}
				}
			}
			return;
		}
		
		for (byte i = 0; i < input.length; i++) {
			ItemStack in = input[i];
			if (in == null || in.count < 1 || !in.isIn(BNBItemTags.NETHER_FIBER_SOURCE)) continue;
			in.count--;
			if (in.count < 1) input[i] = null;
			process = 50;
			return;
		}
	}
	
	@Override
	public void readIdentifyingData(CompoundTag tag) {
		super.readIdentifyingData(tag);
		tagToArray(tag.getListTag("input"), input);
		tagToArray(tag.getListTag("output"), output);
		process = tag.getByte("process");
	}
	
	@Override
	public void writeIdentifyingData(CompoundTag tag) {
		super.writeIdentifyingData(tag);
		tag.put("input", arrayToTag(input));
		tag.put("output", arrayToTag(output));
		tag.put("process", (byte) process);
	}
	
	@Environment(EnvType.CLIENT)
	public float getProcess() {
		if (process == 0) return 0;
		return 1.0F - process / 50.0F;
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
}
