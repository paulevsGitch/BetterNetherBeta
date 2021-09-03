package paulevs.bnb.particles;

import net.minecraft.entity.ParticleBase;
import net.minecraft.level.Level;

public abstract class NetherParticle extends ParticleBase {
	public NetherParticle(Level level, double x, double y, double z, double dx, double dy, double dz) {
		super(level, x, y, z, dx, dy, dz);
	}
	
	protected void setTextureIndex(int index) {
		field_2635 = index;
	}
	
	protected int getMaxAge() {
		return field_2639;
	}
	
	protected void setMaxAge(int maxAge) {
		field_2639 = maxAge;
	}
	
	protected int getAge() {
		return field_2638;
	}
	
	protected void incrementAge() {
		field_2638++;
	}
	
	protected boolean toRemove() {
		return getAge() >= getMaxAge();
	}
}
