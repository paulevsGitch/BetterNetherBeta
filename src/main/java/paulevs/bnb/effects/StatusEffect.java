package paulevs.bnb.effects;

import net.minecraft.entity.player.PlayerBase;
import paulevs.bnb.interfaces.StatusEffectable;

public abstract class StatusEffect {
	private final int maxTime;
	private int time = 0;
	
	public StatusEffect(int maxTime) {
		this.maxTime = maxTime;
	}
	
	public void tick(PlayerBase player) {
		if (time > maxTime) {
			StatusEffectable effect = (StatusEffectable) player;
			effect.removeEffect(getName());
			return;
		}
		onPlayerTick(player);
		time++;
	}
	
	public abstract void onPlayerTick(PlayerBase player);
	
	public abstract String getName();
}
