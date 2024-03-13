package paulevs.bnb.math;

import net.minecraft.util.maths.Vec3D;

public class Matrix3F {
	private final float[] data = new float[9];
	
	public void identity() {
		for (int i = 0; i < data.length; i++) {
			data[i] = (i & 3) == 0 ? 1.0F : 0.0F;
		}
	}
	
	public void rotation(Vec3D axis, float angle) {
		float cos = (float) Math.cos(angle);
		float sin = (float) Math.sin(angle);
		float invCos = 1.0F - cos;
		
		float xx = (float) axis.x * (float) axis.x * invCos;
		float yy = (float) axis.y * (float) axis.y * invCos;
		float zz = (float) axis.z * (float) axis.z * invCos;
		
		float xy = (float) axis.x * (float) axis.y * invCos;
		float xz = (float) axis.x * (float) axis.z * invCos;
		float yz = (float) axis.y * (float) axis.z * invCos;
		
		data[0] = xx + cos;
		data[1] = xy - (float) axis.z * sin;
		data[2] = xz + (float) axis.y * sin;
		
		data[3] = xy + (float) axis.z * sin;
		data[4] = yy + cos;
		data[5] = yz - (float) axis.x * sin;
		
		data[6] = xz - (float) axis.y * sin;
		data[7] = yz + (float) axis.x * sin;
		data[8] = zz + cos;
	}
	
	public void transform(Vec3D v) {
		double x = v.x * data[0] + v.y * data[1] + v.z * data[2];
		double y = v.x * data[3] + v.y * data[4] + v.z * data[5];
		double z = v.x * data[6] + v.y * data[7] + v.z * data[8];
		v.x = x;
		v.y = y;
		v.z = z;
	}
}
