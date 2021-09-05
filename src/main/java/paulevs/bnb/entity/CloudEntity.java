package paulevs.bnb.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import paulevs.bnb.effects.StatusEffect;
import paulevs.bnb.effects.StatusEffects;
import paulevs.bnb.interfaces.StatusEffectable;
import paulevs.bnb.util.DyeColors;

public class CloudEntity extends EntityBase {
	private CompoundTag statusEffect;
	
	public CloudEntity(Level level) {
		super(level);
		this.setSize(4.0F, 1.0F);
	}
	
	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(16, 200);
		this.dataTracker.startTracking(17, DyeColors.WHITE.getColor());
		this.dataTracker.startTracking(18, "");
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {
		if (tag.containsKey("statusEffect")) {
			statusEffect = tag.getCompoundTag("statusEffect");
		}
	}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {
		if (statusEffect != null) {
			tag.put("statusEffect", statusEffect);
		}
	}
	
	@Override
	public void tick() {
		super.tick();
		if (getAge() > getMaxAge()) {
			this.remove();
		}
	}
	
	@Override
	public void onPlayerCollision(PlayerBase player) {
		if (statusEffect != null && (getAge() & 3) == 0) {
			StatusEffectable effectable = (StatusEffectable) player;
			StatusEffect effect = effectable.getEffect(statusEffect.getString("name"));
			if (effect != null) {
				effect.resetTime();
			}
			else {
				effectable.addEffect(StatusEffects.getEffect(statusEffect));
			}
		}
	}
	
	public int getAge() {
		return field_1645;
	}
	
	public void setMaxAge(int maxAge) {
		this.dataTracker.setInt(16, maxAge);
	}
	
	public int getMaxAge() {
		return this.dataTracker.getInt(16);
	}
	
	public void setColor(int color) {
		this.dataTracker.setInt(17, color);
	}
	
	public int getColor() {
		return this.dataTracker.getInt(17);
	}
	
	public void setStatusEffect(StatusEffect statusEffect) {
		this.statusEffect = statusEffect.toTag();
	}
}
