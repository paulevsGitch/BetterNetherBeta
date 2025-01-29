package paulevs.bnb.particle;

import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.technical.ParticleEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.property.BNBBlockMaterials;

public class AcidParticleEntity extends ParticleEntity implements BNBParticle {
	private final float fullSize;
	private boolean isOnSurface;
	private float nextSpeedX;
	private float nextSpeedZ;
	private float preSpeedX;
	private float preSpeedZ;
	private float scale;
	
	public AcidParticleEntity(Level level, double x, double y, double z) {
		super(level, x, y, z, 0, 0, 0);
		textureIndex = 15 + level.random.nextInt(2);
		randomizeSpeed();
		velocityY = 0.02F;
		fullSize = level.random.nextFloat() * 1.75F + 1.75F;
		maxAge = level.random.nextInt(200) + 200;
		prevX = x;
		prevY = y;
		prevZ = z;
	}
	
	private void randomizeSpeed() {
		nextSpeedX = (float) level.random.nextGaussian() * 0.02F;
		nextSpeedZ = (float) level.random.nextGaussian() * 0.02F;
	}
	
	@Override
	public void tick() {
		if (age++ >= maxAge) {
			remove();
			return;
		}
		
		int tick = age & 63;
		if (tick == 0) {
			preSpeedX = nextSpeedX;
			preSpeedZ = nextSpeedZ;
			randomizeSpeed();
		}
		
		float delta = tick / 63F;
		velocityX = MathHelper.lerp(delta, preSpeedX, nextSpeedX);
		velocityZ = MathHelper.lerp(delta, preSpeedZ, nextSpeedZ);
		
		prevX = x;
		prevY = y;
		prevZ = z;
		
		if (!isOnSurface) {
			move(velocityX, velocityY, velocityZ);
			if (!isInFluid(BNBBlockMaterials.SULPHURIC_ACID)) {
				move(0.0F, -velocityY * 0.5F, 0.0F);
				isOnSurface = true;
				velocityY = 0.0F;
				maxAge = age + 8;
			}
		}
		else {
			if (scale < 1.0F && age < maxAge - 4) scale += 0.25F;
			if (textureIndex == 15 && age == maxAge - 2) textureIndex = 16;
			else if (age == maxAge - 4) textureIndex = 17;
		}
		
		if (age < 63) scale = delta;
		else if (isOnSurface) scale += 0.1F;
		else if (age > maxAge - 63) {
			scale = 1F - (age - maxAge + 63) / 63F;
		}
		else scale = 1.0F;
	}
	
	@Override
	public void render(Tessellator tessellator, float delta, float x, float y, float z, float width, float height) {
		size = fullSize * scale;
		super.render(tessellator, delta, x, y, z, width, height);
	}
	
	/*@Override
	public boolean isTranslucent() {
		return true;
	}*/
	
	@Override
	public float getBrightnessAtEyes(float delta) {
		return Math.max(super.getBrightnessAtEyes(delta) * 1.5F, 0.5F);
	}
}
