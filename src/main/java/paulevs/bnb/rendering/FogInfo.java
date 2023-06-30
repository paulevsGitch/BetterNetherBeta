package paulevs.bnb.rendering;

import net.minecraft.util.maths.Vec3f;

public class FogInfo {
	public static final float[] COLOR = new float[3];
	
	public static void setColor(float r, float g, float b) {
		COLOR[0] = r;
		COLOR[1] = g;
		COLOR[2] = b;
	}
	
	public static Vec3f getVector() {
		return Vec3f.from(COLOR[0], COLOR[1], COLOR[2]);
	}
}
