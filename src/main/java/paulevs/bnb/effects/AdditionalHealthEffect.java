package paulevs.bnb.effects;

import net.minecraft.entity.player.PlayerBase;

public class AdditionalHealthEffect extends StatusEffect {
	private int additionalHealth = 10;
	
	public AdditionalHealthEffect() {
		super(600);
	}

	@Override
	public void onPlayerTick(PlayerBase player) {
		if (player.health < 20) {
			player.health ++;
			additionalHealth--;
		}
		player.field_1613 = player.field_1009 / 2;
	}

	@Override
	public String getName() {
		return "additional_health";
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
}
