package paulevs.bnb.effects;

import net.minecraft.entity.monster.ZombiePigman;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;

import java.util.List;

public class SoulProtectionEffect extends StatusEffect {
	public static final String NAME = "soul_protection";
	private Box box = Box.create(0, 0, 0, 0, 0, 0);
	
	public SoulProtectionEffect() {
		super(600, 242);
	}
	
	@Override
	public void onPlayerTick(PlayerBase player) {
		box.method_99(player.x - 16, player.y - 16, player.z - 16, player.x + 16, player.y + 16, player.z + 16);
		List entities = player.level.getEntities(ZombiePigman.class, box);
		entities.forEach(entity -> ((ZombiePigman) entity).setTarget(null));
	}
	
	@Override
	public String getName() {
		return NAME;
	}
	
	@Override
	public String getDescription() {
		return "Soul Protection";
	}
	
	@Override
	public void writeCustomData(CompoundTag tag) {}
	
	@Override
	public void readCustomData(CompoundTag tag) {}
}
