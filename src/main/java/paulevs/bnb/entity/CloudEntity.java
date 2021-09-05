package paulevs.bnb.entity;

import net.minecraft.entity.EntityBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import paulevs.bnb.util.DyeColors;

public class CloudEntity extends EntityBase {
	public CloudEntity(Level level) {
		super(level);
		this.setSize(4.0F, 1.0F);
	}
	
	@Override
	protected void initDataTracker() {
		this.dataTracker.startTracking(16, 200);
		this.dataTracker.startTracking(17, DyeColors.WHITE.getColor());
	}
	
	@Override
	protected void readCustomDataFromTag(CompoundTag tag) {}
	
	@Override
	protected void writeCustomDataToTag(CompoundTag tag) {}
	
	public void tick() {
		super.tick();
		if (getAge() > getMaxAge()) {
			this.remove();
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
}
