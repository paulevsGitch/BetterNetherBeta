package paulevs.bnb.particles;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.entity.ParticleBase;
import net.minecraft.level.Level;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.util.MHelper;

public class BiomeParticle extends ParticleBase {
	private static int textureID = -1;
	private float nextSpeedX;
	private float nextSpeedY;
	private float nextSpeedZ;
	private float preSpeedX;
	private float preSpeedY;
	private float preSpeedZ;
	private float alpha;
	private int maxAge;
	private int ticks;
	
	public BiomeParticle(Level level, double x, double y, double z, int index) {
		super(level, x, y, z, 0, 0, 0);
		this.setPositionAndAngles(x, y, z, 0, 0);
		maxAge = MHelper.randRange(100, 300, ClientUtil.getRandom());
		field_2640 = MHelper.randRange(0.5F, 1.5F, ClientUtil.getRandom()); // Particle size
		field_2635 = index; // Particle texture index
		randomizeSpeed();
		velocityX = 0;
		velocityY = 0;
		velocityZ = 0;
		ticks = 0;
	}
	
	private void randomizeSpeed() {
		nextSpeedX = (float) ClientUtil.getRandom().nextGaussian() * 0.02F;
		nextSpeedY = (float) ClientUtil.getRandom().nextGaussian() * 0.02F;
		nextSpeedZ = (float) ClientUtil.getRandom().nextGaussian() * 0.02F;
	}
	
	private void updateSpeed() {
		preSpeedX = nextSpeedX;
		preSpeedY = nextSpeedY;
		preSpeedZ = nextSpeedZ;
	}
	
	@Override
	public void tick() {
		ticks ++;
		
		if (ticks > maxAge) {
			remove();
			return;
		}
		
		int tick = ticks & 63;
		if (tick == 0) {
			updateSpeed();
			randomizeSpeed();
		}
		
		float delta = tick / 63F;
		velocityX = MHelper.lerp(preSpeedX, nextSpeedX, delta);
		velocityY = MHelper.lerp(preSpeedY, nextSpeedY, delta);
		velocityZ = MHelper.lerp(preSpeedZ, nextSpeedZ, delta);
		setVelocity(velocityX, velocityY, velocityZ);
		
		prevX = x;
		prevY = y;
		prevZ = z;
		
		move(velocityX, velocityY, velocityZ);
		
		if (ticks <= 63) {
			alpha = delta;
		}
		else if (ticks >= maxAge - 63) {
			alpha = 1F - (ticks - maxAge + 63) / 63F;
		}
	}
	
	@Override
	public float getBrightnessAtEyes(float f) {
		return 1F;
	}
	
	@Override
	public void method_2002(Tessellator tessellator, float f, float x, float y, float z, float f4, float f5) {
		TextureManager manager = ClientUtil.getMinecraft().textureManager;
		if (textureID < 0) {
			textureID = manager.getTextureId("/assets/" + BetterNetherBeta.MOD_ID + "/textures/particles.png");
		}
		manager.bindTexture(textureID);
		
		float u1 = (this.field_2635 & 15) / 16F;
		float u2 = u1 + 0.0624375F;
		float v1 = (this.field_2635 / 16) / 16.0F;
		float v2 = v1 + 0.0624375F;
		float scale = 0.1F * this.field_2640 * alpha;
		float var13 = (float)(this.prevX + (this.x - this.prevX) * f - field_2645);
		float var14 = (float)(this.prevY + (this.y - this.prevY) * f - field_2646);
		float var15 = (float)(this.prevZ + (this.z - this.prevZ) * f - field_2647);
		float light = this.getBrightnessAtEyes(f);
		tessellator.colour(this.field_2642 * light, this.field_2643 * light, this.field_2644 * light);
		tessellator.vertex((var13 - x * scale - f4 * scale), (var14 - y * scale), (var15 - z * scale - f5 * scale), u2, v2);
		tessellator.vertex((var13 - x * scale + f4 * scale), (var14 + y * scale), (var15 - z * scale + f5 * scale), u2, v1);
		tessellator.vertex((var13 + x * scale + f4 * scale), (var14 + y * scale), (var15 + z * scale + f5 * scale), u1, v1);
		tessellator.vertex((var13 + x * scale - f4 * scale), (var14 - y * scale), (var15 + z * scale - f5 * scale), u1, v2);
	}
}
