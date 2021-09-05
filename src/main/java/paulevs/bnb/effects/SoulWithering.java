package paulevs.bnb.effects;

import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;

public class SoulWithering extends StatusEffect {
	public static final String NAME = "soul_withering";
	
	public SoulWithering() {
		super(120, 243);
	}
	
	@Override
	public void onPlayerTick(PlayerBase player) {
		if ((getTime() & 15) == 0) {
			player.damage(null, 1);
		}
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String getDescription() {
		return null;
	}
	
	@Override
	public void writeCustomData(CompoundTag tag) {}
	
	@Override
	public void readCustomData(CompoundTag tag) {}
}
