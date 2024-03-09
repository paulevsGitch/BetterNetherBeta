package paulevs.bnb.entity.renderer;

import net.minecraft.client.render.entity.BoatRenderer;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;

public class ObsidianBoatRenderer extends BoatRenderer {
	public ObsidianBoatRenderer() {
		super();
	}
	
	@Override
	public void method_595(BoatEntity entity, double x, double y, double z, float g, float h) {
		GL11.glPushMatrix();
		GL11.glTranslated(x, y, z);
		GL11.glRotatef(180.0f - g, 0.0f, 1.0f, 0.0f);
		
		float f2 = (float) entity.damageTicks - h;
		float f3 = Math.max((float) entity.damage - h, 0.0F);
		
		if (f2 > 0.0F) {
			GL11.glRotatef(MathHelper.sin(f2) * f2 * f3 / 10.0F * (float) entity.damageAngle, 1.0F, 0.0F, 0.0F);
		}
		
		GL11.glScalef(-1.0F, -1.0F, 1.0f);
		
		this.bindTexture("/assets/bnb/stationapi/textures/entity/obsidian_boat.png");
		this.model.render(0.0F, 0.0F, -0.1F, 0.0F, 0.0F, 0.0625F);
		
		GL11.glPopMatrix();
	}
}
