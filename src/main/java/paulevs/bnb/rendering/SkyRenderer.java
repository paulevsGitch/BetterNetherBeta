package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Tessellator;
import net.minecraft.client.texture.TextureManager;
import net.minecraft.util.maths.MathHelper;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL14;

@Environment(EnvType.CLIENT)
public class SkyRenderer {
	private static final int GRADIENT = makeSkyGradient();
	private static int gradientTexture;
	
	public static void init(TextureManager textureManager) {
		if (gradientTexture != 0) return;
		gradientTexture = textureManager.getTextureId("/assets/bnb/stationapi/textures/environment/gradient.png");
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gradientTexture);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_CLAMP);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_LINEAR);
		GL11.glTexParameteri(GL11.GL_TEXTURE_2D, GL14.GL_GENERATE_MIPMAP, GL11.GL_FALSE);
	}
	
	public static void renderSky() {
		GL11.glEnable(GL11.GL_TEXTURE_2D);
		
		GL11.glEnable(GL11.GL_BLEND);
		GL11.glAlphaFunc(GL11.GL_GREATER, 0.0F);
		GL11.glDepthMask(false);
		
		GL11.glColor4f(
			FogInfo.COLOR[0] * 0.75F,
			FogInfo.COLOR[1] * 0.75F,
			FogInfo.COLOR[2] * 0.75F,
			1F
		);
		
		GL11.glBindTexture(GL11.GL_TEXTURE_2D, gradientTexture);
		GL11.glCallList(GRADIENT);
		
		GL11.glDepthMask(true);
		GL11.glDisable(GL11.GL_BLEND);
	}
	
	private static int makeSkyGradient() {
		float pi2 = (float) (Math.PI * 2);
		
		int list = GL11.glGenLists(1);
		GL11.glNewList(list, GL11.GL_COMPILE);
		Tessellator tessellator = Tessellator.INSTANCE;
		tessellator.start();
		
		final int count = 16;
		float sin1 = MathHelper.sin(0) * 100;
		float cos1 = MathHelper.cos(0) * 100;
		
		for (byte i = 1; i <= count; i++) {
			float angle = (float) i / count * pi2;
			float sin2 = MathHelper.sin(angle) * 100;
			float cos2 = MathHelper.cos(angle) * 100;
			
			tessellator.vertex(sin1, -50, cos1, 0, 0);
			tessellator.vertex(sin1,  50, cos1, 0, 1);
			tessellator.vertex(sin2,  50, cos2, 1, 1);
			tessellator.vertex(sin2, -50, cos2, 1, 0);
			
			sin1 = sin2;
			cos1 = cos2;
		}
		
		tessellator.draw();
		GL11.glEndList();
		
		return list;
	}
}
