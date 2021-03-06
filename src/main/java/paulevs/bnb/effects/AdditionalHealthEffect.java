package paulevs.bnb.effects;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;

public class AdditionalHealthEffect extends StatusEffect {
	public static final String NAME = "additional_health";
	private int additionalHealth = 10;
	
	public AdditionalHealthEffect() {
		super(600, 240);
	}

	@Override
	public void onPlayerTick(PlayerBase player) {
		if (additionalHealth > 1 && player.health < 20) {
			int diff = 20 - player.health;
			if (diff > additionalHealth) {
				diff = additionalHealth;
			}
			player.health += diff;
			additionalHealth -= diff;
		}
	}

	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String getDescription() {
		return "Additional Health";
	}
	
	public int getHealth() {
		return additionalHealth;
	}
	
	public void addHealth(int health) {
		additionalHealth += health;
		if (additionalHealth > 10) {
			additionalHealth = 10;
		}
	}

	@Override
	public void writeCustomData(CompoundTag tag) {
		tag.put("health", additionalHealth);
	}

	@Override
	public void readCustomData(CompoundTag tag) {
		additionalHealth = tag.getInt("health");
	}
}
