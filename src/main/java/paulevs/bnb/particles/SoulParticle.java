package paulevs.bnb.particles;

import net.minecraft.client.render.Tessellator;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.util.MHelper;

public class SoulParticle extends NetherParticle {
	private float nextSpeedX;
	private float nextSpeedZ;
	private float preSpeedX;
	private float preSpeedZ;
	private float scale;
	
	public SoulParticle(Level level, double x, double y, double z) {
		super(level, x, y, z, 0, 0, 0);
		setMaxAge(MHelper.randRange(20, 60, ClientUtil.getRandom()));
		setTextureIndex(224);
		velocityY = 0.04F;
		randomizeSpeed();
	}
	
	private void randomizeSpeed() {
		nextSpeedX = (float) ClientUtil.getRandom().nextGaussian() * 0.02F;
		nextSpeedZ = (float) ClientUtil.getRandom().nextGaussian() * 0.02F;
	}
	
	private void updateSpeed() {
		preSpeedX = nextSpeedX;
		preSpeedZ = nextSpeedZ;
	}
	
	@Override
	public float getBrightnessAtEyes(float f) {
		return 1F;
	}
	
	@Override
	public void tick() {
		incrementAge();
		if (toRemove()) {
			remove();
			return;
		}
		
		int tick = getAge() & 7;
		if (tick == 0) {
			updateSpeed();
			randomizeSpeed();
		}
		
		float delta = tick / 7F;
		velocityX = MHelper.lerp(preSpeedX, nextSpeedX, delta);
		velocityZ = MHelper.lerp(preSpeedZ, nextSpeedZ, delta);
		setVelocity(velocityX, velocityY, velocityZ);
		
		prevX = x;
		prevY = y;
		prevZ = z;
		
		move(velocityX, velocityY, velocityZ);
		
		delta = (float) getAge() / getMaxAge();
		setTextureIndex(224 + MathHelper.floor(delta * 3));
		scale = 1F - delta;
	}
	
	@Override
	public void method_2002(Tessellator tessellator, float f, float x, float y, float z, float f4, float f5) {
		float u1 = (this.field_2635 & 15) / 16F;
		float u2 = u1 + 0.0624375F;
		float v1 = (this.field_2635 / 16) / 16.0F;
		float v2 = v1 + 0.0624375F;
		float scale = 0.1F * this.field_2640 * this.scale;
		float posX = (float)(this.prevX + (this.x - this.prevX) * f - field_2645);
		float posY = (float)(this.prevY + (this.y - this.prevY) * f - field_2646);
		float posZ = (float)(this.prevZ + (this.z - this.prevZ) * f - field_2647);
		float light = this.getBrightnessAtEyes(f);
		tessellator.colour(this.field_2642 * light, this.field_2643 * light, this.field_2644 * light);
		tessellator.vertex((posX - x * scale - f4 * scale), (posY - y * scale), (posZ - z * scale - f5 * scale), u2, v2);
		tessellator.vertex((posX - x * scale + f4 * scale), (posY + y * scale), (posZ - z * scale + f5 * scale), u2, v1);
		tessellator.vertex((posX + x * scale + f4 * scale), (posY + y * scale), (posZ + z * scale + f5 * scale), u1, v1);
		tessellator.vertex((posX + x * scale - f4 * scale), (posY - y * scale), (posZ + z * scale - f5 * scale), u1, v2);
	}
}
