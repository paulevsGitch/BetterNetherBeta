package paulevs.bnb.entity;

import net.minecraft.client.render.entity.SpiderEyesRenderer;
import net.minecraft.client.render.entity.model.SpiderModel;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.monster.SpiderEntity;
import org.lwjgl.opengl.GL11;

public class NetherSpiderRenderer extends SpiderEyesRenderer {
	public static boolean upscaleModel;
	private final String texture;
	
	public NetherSpiderRenderer(String texture) {
		super();
		this.texture = "/assets/bnb/stationapi/textures/entity/" + texture + ".png";
		upscaleModel = true;
		this.setModel(new SpiderModel());
		upscaleModel = false;
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
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		return true;
	}
}
