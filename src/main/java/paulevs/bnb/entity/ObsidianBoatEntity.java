package paulevs.bnb.entity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.server.entity.EntitySpawnDataProvider;
import net.modificationstation.stationapi.api.server.entity.HasTrackingParameters;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.item.BNBItems;

@HasTrackingParameters(trackingDistance = 50, updatePeriod = 1)
public class ObsidianBoatEntity extends BoatEntity implements EntitySpawnDataProvider {
	public static final Identifier ID = BNB.id("obsidian_boat");
	
	public ObsidianBoatEntity(Level level) {
		super(level);
		immuneToFire = true;
	}
	
	public ObsidianBoatEntity(Level level, double x, double y, double z) {
		this(level);
		setPosition(x, y, z);
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
	
	@Override
	public void accelerate(double x, double y, double z) {
		if (passenger != null) super.accelerate(x, y, z);
	}
	
	@Override
	public Identifier getHandlerIdentifier() {
		return ID;
	}
}
