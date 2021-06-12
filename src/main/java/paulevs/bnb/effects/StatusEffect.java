package paulevs.bnb.effects;

import java.util.List;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;

public abstract class StatusEffect {
	private int maxTime;
	private int time;
	
	public StatusEffect(int maxTime) {
		this.maxTime = maxTime;
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
}
