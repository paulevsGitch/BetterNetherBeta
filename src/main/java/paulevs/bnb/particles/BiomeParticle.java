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
	private int maxAge;
	private int ticks;
	
	public BiomeParticle(Level level, double x, double y, double z) {
		super(level, x, y, z, 0, 0, 0);
		this.setPositionAndAngles(x, y, z, 0, 0);
		maxAge = MHelper.randRange(100, 200, ClientUtil.getRandom());
		field_2640 = MHelper.randRange(1F, 2F, ClientUtil.getRandom()); // Particle size
		field_2635 = MHelper.randRange(0, 2, ClientUtil.getRandom()); // Particle texture index
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
	}
	
	@Override
	public float getBrightnessAtEyes(float f) {
		return 1F;
	}
	
	@Override
	public void method_2002(Tessellator tessellator, float f, float f1, float f2, float f3, float f4, float f5) {
		TextureManager manager = ClientUtil.getMinecraft().textureManager;
		if (textureID < 0) {
			textureID = manager.getTextureId("/assets/" + BetterNetherBeta.MOD_ID + "/textures/particles.png");
		}
		manager.bindTexture(textureID);
		super.method_2002(tessellator, f, f1, f2, f3, f4, f5);
	}
}
