package paulevs.bnb.particle;

import net.minecraft.client.render.Tessellator;
import net.minecraft.entity.technical.ParticleEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.util.math.MathHelper;

public class BiomeParticleEntity extends ParticleEntity implements BNBParticle {
	private final boolean emissive;
	private final float fullSize;
	private float nextSpeedX;
	private float nextSpeedY;
	private float nextSpeedZ;
	private float preSpeedX;
	private float preSpeedY;
	private float preSpeedZ;
	private float scale;
	
	public BiomeParticleEntity(Level level, double x, double y, double z, int textureIndex, boolean emissive) {
		super(level, x, y, z, 0, 0, 0);
		this.textureIndex = textureIndex;
		this.emissive = emissive;
		prevX = x;
		prevY = y;
		prevZ = z;
		maxAge = level.random.nextInt(200) + 100;
		fullSize = level.random.nextFloat() * 1.75F + 1.75F;
		randomizeSpeed();
		velocityX = 0;
		velocityY = 0;
		velocityZ = 0;
	}
	
	private void randomizeSpeed() {
		nextSpeedX = (float) level.random.nextGaussian() * 0.02F;
		nextSpeedY = (float) level.random.nextGaussian() * 0.02F;
		nextSpeedZ = (float) level.random.nextGaussian() * 0.02F;
	}
	
	private void updateSpeed() {
		preSpeedX = nextSpeedX;
		preSpeedY = nextSpeedY;
		preSpeedZ = nextSpeedZ;
	}
	
	@Override
	public void tick() {
		if (age++ >= maxAge) {
			remove();
			return;
		}
		
		int tick = age & 63;
		if (tick == 0) {
			updateSpeed();
			randomizeSpeed();
		}
		
		float delta = tick / 63F;
		velocityX = MathHelper.lerp(delta, preSpeedX, nextSpeedX);
		velocityY = MathHelper.lerp(delta, preSpeedY, nextSpeedY);
		velocityZ = MathHelper.lerp(delta, preSpeedZ, nextSpeedZ);
		
		prevX = x;
		prevY = y;
		prevZ = z;
		
		move(velocityX, velocityY, velocityZ);
		
		if (age < 63) scale = delta;
		else if (age > maxAge - 63) {
			scale = 1F - (age - maxAge + 63) / 63F;
		}
		else scale = 1.0F;
	}
	
	@Override
	public float getBrightnessAtEyes(float delta) {
		return emissive ? 1F : super.getBrightnessAtEyes(delta);
	}
	
	@Override
	public void render(Tessellator tessellator, float delta, float x, float y, float z, float width, float height) {
		size = fullSize * scale;
		super.render(tessellator, delta, x, y, z, width, height);
	}
}
