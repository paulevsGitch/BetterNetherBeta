package paulevs.bnb.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import paulevs.bnb.item.BNBItems;

public class ObsidianBoatEntity extends BoatEntity {
	public ObsidianBoatEntity(Level level) {
		super(level);
		immuneToFire = true;
	}
	
	@Override
	public boolean damage(Entity entity, int damage) {
		if (level.isRemote || removed) return true;
		damageAngle = -damageAngle;
		damageTicks = 10;
		this.damage += damage * 10;
		markToUpdateVelocity();
		if (this.damage > 100) {
			if (passenger != null) passenger.stopRiding(this);
			dropItem(new ItemStack(BNBItems.OBSIDIAN_BOAT), 0.0F);
			remove();
		}
		return true;
	}
	
	/*@Override
	public void updatePassenger() {
		if (passenger == null) return;
		passenger.setPosition(
			this.x,
			this.y + 2.0,
			this.z
		);
		passenger.setVelocity(velocityX, velocityY, velocityZ);
	}*/
	
	@Override
	public void accelerate(double x, double y, double z) {
		if (passenger != null) super.accelerate(x, y, z);
	}
}
