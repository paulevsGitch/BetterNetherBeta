package paulevs.bnb.math;

import net.modificationstation.stationapi.api.util.math.MathHelper;

import java.awt.Color;

public class ColorUtil {
	private static final float[] HSV = new float[3];
	private static final int ALPHA = 0xFF000000;
	
	public static int getR(int color) {
		return (color >> 16) & 255;
	}
	
	public static int getG(int color) {
		return (color >> 8) & 255;
	}
	
	public static int getB(int color) {
		return color & 255;
	}
	
	public static int getARGB(int a, int r, int g, int b) {
		return a << 24 | r << 16 | g << 8 | b;
	}
	
	public static int getRGB(int r, int g, int b) {
		return ALPHA | r << 16 | g << 8 | b;
	}
	
	public static int multiply(int color, float r, float g, float b) {
		int ir = MathHelper.clamp(Math.round(getR(color) * r), 0, 255);
		int ig = MathHelper.clamp(Math.round(getG(color) * g), 0, 255);
		int ib = MathHelper.clamp(Math.round(getB(color) * b), 0, 255);
		return getRGB(ir, ig, ib);
	}
	
	public static int blend(int colorA, int colorB, float delta) {
		int r = MathHelper.lerp(delta, getR(colorA), getR(colorB));
		int g = MathHelper.lerp(delta, getG(colorA), getG(colorB));
		int b = MathHelper.lerp(delta, getB(colorA), getB(colorB));
		return getRGB(r, g, b);
	}
	
	public static float[] toHSV(int color) {
		return Color.RGBtoHSB(
			getR(color),
			getG(color),
			getB(color),
			HSV
		);
	}
	
	public static int fromHSV(float[] hsv) {
		return Color.HSBtoRGB(hsv[0], hsv[1], hsv[2]);
	}
}
