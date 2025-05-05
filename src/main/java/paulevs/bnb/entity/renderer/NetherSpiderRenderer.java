package paulevs.bnb.entity.renderer;

import net.minecraft.client.render.entity.SpiderEyesRenderer;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.monster.SpiderEntity;
import net.minecraft.util.maths.Vec3I;
import net.minecraft.util.maths.VectorCache;
import org.lwjgl.opengl.GL11;
import paulevs.bnb.entity.NetherSpiderEntity;
import paulevs.bnb.mixin.client.VectorCacheAccessor;

public class NetherSpiderRenderer extends SpiderEyesRenderer {
	private final String texture;
	
	public NetherSpiderRenderer(String texture) {
		super();
		this.texture = "/assets/bnb/stationapi/textures/entity/" + texture + ".png";
	}
	
	@Override
	protected void setupTransform(LivingEntity arg, float pitch, float yaw, float roll) {
		super.setupTransform(arg, pitch, yaw, roll);
		GL11.glScalef(1.25F, 1.25F, 1.25F);
	}
	
	@Override
	protected boolean method_2021(SpiderEntity entity, int i, float f) {
		if (i != 0) return false;
		this.bindTexture(texture);
		GL11.glColor4f(2.0F, 2.0F, 2.0F, 1.0F);
		return true;
	}
	
	@Override
	protected void renderEntityName(LivingEntity entity, double x, double y, double z) {
		super.renderEntityName(entity, x, y, z);
		/*GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINES);
		GL11.glVertex3f(0.0F + (float) x, 0.0F + (float) y, 0.0F + (float) z);
		GL11.glVertex3f(0.0F + (float) x, 3.0F + (float) y, 0.0F + (float) z);
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);*/
		
		//double px = MathHelper.lerp()
		
		NetherSpiderEntity spider = (NetherSpiderEntity) entity;
		VectorCache path = spider.path;
		if (path == null) return;
		
		Vec3I[] data = ((VectorCacheAccessor) path).bnb_getData();
		GL11.glColor4f(1.0F, 0.0F, 1.0F, 1.0F);
		GL11.glDisable(GL11.GL_TEXTURE_2D);
		GL11.glBegin(GL11.GL_LINE_STRIP);
		for (Vec3I vec: data) {
			GL11.glVertex3f(
				(float) (vec.x - spider.x + x) + 0.5F,
				(float) (vec.y - spider.y + y) + 0.5F,
				(float) (vec.z - spider.z + z) + 0.5F
			);
		}
		GL11.glEnd();
		GL11.glEnable(GL11.GL_TEXTURE_2D);
	}
}
