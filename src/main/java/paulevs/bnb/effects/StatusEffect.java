package paulevs.bnb.effects;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;

import java.util.List;

public abstract class StatusEffect {
	private int maxTime;
	private int iconID;
	private int time;
	
	public StatusEffect(int maxTime, int iconID) {
		this.maxTime = maxTime;
		this.iconID = iconID;
	}
	
	public void tick(PlayerBase player, List<String> toRemove) {
		if (time > maxTime) {
			toRemove.add(getName());
			return;
		}
		onPlayerTick(player);
		time++;
	}
	
	public abstract void onPlayerTick(PlayerBase player);
	
	public abstract String getName();
	
	public abstract String getDescription();
	
	public abstract void writeCustomData(CompoundTag tag);
	
	public abstract void readCustomData(CompoundTag tag);
	
	public double getDelta() {
		return (double) time / maxTime;
	}
	
	public CompoundTag toTag() {
		CompoundTag tag = new CompoundTag();
		tag.put("name", getName());
		tag.put("maxTime", maxTime);
		tag.put("time", time);
		writeCustomData(tag);
		return tag;
	}
	
	public void fromTag(CompoundTag tag) {
		maxTime = tag.getInt("maxTime");
		time = tag.getInt("time");
		readCustomData(tag);
	}
	
	public int getIconID() {
		return iconID;
	}
	
	public int getRemainingTicks() {
		return maxTime - time;
	}
	
	public void resetTime() {
		time = 0;
	}
}
