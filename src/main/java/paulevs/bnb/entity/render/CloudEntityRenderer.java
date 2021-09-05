package paulevs.bnb.entity.render;

import net.minecraft.client.render.Tessellator;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.entity.EntityBase;
import net.minecraft.util.maths.Vec3f;
import org.lwjgl.opengl.GL11;
import paulevs.bnb.entity.CloudEntity;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class CloudEntityRenderer extends EntityRenderer {
	private final Vec3f[][] trajectories = new Vec3f[16][32];
	private final Vec3f[][] middlePoints = new Vec3f[16][32];
	private final float step = 8.0F / 128.0F;
	
	public CloudEntityRenderer() {
		Random random = new Random(0);
		
		for (int i = 0; i < trajectories.length; i++) {
			for (int j = 0; j < trajectories[0].length; j++) {
				float py = MHelper.randRange(-0.5F, 0.5F, random);
				float px = MHelper.randRange(-2F, 2F, random);
				float pz = MHelper.randRange(-2F, 2F, random);
				trajectories[i][j] = Vec3f.method_1293(px, py, pz);
			}
		}
		
		for (int i = 0; i < trajectories.length; i++) {
			for (int j = 0; j < trajectories[0].length; j++) {
				Vec3f a = trajectories[i][(j - 1) & 31];
				Vec3f b = trajectories[i][j];
				Vec3f c = trajectories[i][(j + 1) & 31];
				Vec3f d = trajectories[i][(j + 2) & 31];
				
				a = Vec3f.method_1293(b.x - a.x, b.y - a.y, b.z - a.z);
				d = Vec3f.method_1293(d.x - c.x, d.y - c.y, d.z - c.z);
				a.x = a.x * 1.5F + b.x;
				a.y = a.y * 1.5F + b.y;
				a.z = a.z * 1.5F + b.z;
				d.x = d.x * 1.5F + c.x;
				d.y = d.y * 1.5F + c.y;
				d.z = d.z * 1.5F + c.z;
				
				middlePoints[i][j] = Vec3f.method_1293((a.x + d.x) * 0.5F, (a.y + d.y) * 0.5F, (a.z + d.z) * 0.5F);
			}
		}
	}
	
	@Override
	public void render(EntityBase entity, double x, double y, double z, float f, float delta) {
		CloudEntity cloud = (CloudEntity) entity;
		int maxAge = cloud.getMaxAge();
		int color = cloud.getColor();
		
		float cr = ((color >> 16) & 255) / 255F;
		float cg = ((color >> 8) & 255) / 255F;
		float cb = (color & 255) / 255F;
		
		float age = cloud.getAge() + delta;
		float scale = age < 40 ? age / 40.0F : age > cloud.getMaxAge() - 40 ? (cloud.getMaxAge() - age) / 40.0F : 1.0F;
		int index = (int) (scale * 7);
		int layer = (int) (age / 130F);
		delta = (age / 130F) - layer;
		layer = layer % trajectories.length;
		
		GL11.glEnable(32826);
		GL11.glEnable(GL11.GL_BLEND);
		this.bindTexture("/particles.png");
		for (int i = 0; i < trajectories[0].length; i++) {
			Vec3f a = trajectories[layer][i];
			Vec3f b = middlePoints[layer][i];
			Vec3f c = trajectories[(layer + 1) % trajectories.length][i];
			float offX = MHelper.lerp((float) a.x, (float) b.x, delta);
			float offY = MHelper.lerp((float) a.y, (float) b.y, delta);
			float offZ = MHelper.lerp((float) a.z, (float) b.z, delta);
			offX = MHelper.lerp(offX, (float) c.x, delta);
			offY = MHelper.lerp(offY, (float) c.y, delta);
			offZ = MHelper.lerp(offZ, (float) c.z, delta);
			renderQuad(x + offX, y + offY, z + offZ, scale, index, cr, cg, cb);
		}
		GL11.glDisable(32826);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private void renderQuad(double x, double y, double z, float scale, int index, float r, float g, float b) {
		int tx = (index & 15) << 3;
		int ty = (index >> 4) << 3;
		float u1 = tx / 128.0F;
		float v1 = ty / 128.0F;
		float u2 = u1 + step;
		float v2 = v1 + step;
		
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glScalef(scale, scale, scale);
		Tessellator tessellator = Tessellator.INSTANCE;
		GL11.glRotatef(180.0F - this.dispatcher.field_2497, 0.0F, 1.0F, 0.0F);
		GL11.glRotatef(-this.dispatcher.field_2498, 1.0F, 0.0F, 0.0F);
		tessellator.start();
		tessellator.colour(r, g, b, 0.7F * scale);
		tessellator.vertex(-0.5F, -0.5F, 0.0D, u1, v2);
		tessellator.vertex(0.5F, -0.5F, 0.0D, u2, v2);
		tessellator.vertex(0.5F, 0.5F, 0.0D, u2, v1);
		tessellator.vertex(-0.5F, 0.5F, 0.0D, u1, v1);
		tessellator.draw();
		GL11.glPopMatrix();
	}
}
