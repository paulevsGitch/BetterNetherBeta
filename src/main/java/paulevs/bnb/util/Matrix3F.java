package paulevs.bnb.util;

import net.minecraft.util.maths.Vec3D;

public class Matrix3F {
	private final float[] data = new float[9];
	
	public Matrix3F identity() {
		for (int i = 0; i < data.length; i++) {
			data[i] = (i & 3) == 0 ? 1.0F : 0.0F;
		}
		return this;
	}
	
	public Matrix3F rotation(Vec3D axis, float angle) {
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
		
		return this;
	}
	
	public Matrix3F scale(float x, float y, float z) {
		data[0] = x;
		data[1] = 0.0F;
		data[2] = 0.0F;
		data[3] = 0.0F;
		data[4] = y;
		data[5] = 0.0F;
		data[6] = 0.0F;
		data[7] = 0.0F;
		data[8] = z;
		return this;
	}
	
	public Matrix3F invert() {
		float a = data[0], b = data[1], c = data[2];
		float d = data[3], e = data[4], f = data[5];
		float g = data[6], h = data[7], i = data[8];
		
		float invDet = 1.0F / (a * (e * i - f * h) - b * (d * i - f * g) + c * (d * h - e * g));
		
		data[0] = (e * i - f * h) * invDet;
		data[1] = (c * h - b * i) * invDet;
		data[2] = (b * f - c * e) * invDet;
		data[3] = (f * g - d * i) * invDet;
		data[4] = (a * i - c * g) * invDet;
		data[5] = (c * d - a * f) * invDet;
		data[6] = (d * h - e * g) * invDet;
		data[7] = (b * g - a * h) * invDet;
		data[8] = (a * e - b * d) * invDet;
		
		return this;
	}
	
	public Matrix3F multiply(Matrix3F matrix) {
		float[] temp = new float[9];
		for (byte i = 0; i < 9; i++) {
			byte col = (byte) (i % 3);
			byte row = (byte) (i / 3);
			for (byte j = 0; j < 3; j++) {
				temp[i] += data[row * 3 + j] * matrix.data[j * 3 + col];
			}
		}
		System.arraycopy(temp, 0, data, 0, 9);
		return this;
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
