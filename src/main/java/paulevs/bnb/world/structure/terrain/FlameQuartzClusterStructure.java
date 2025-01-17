package paulevs.bnb.world.structure.terrain;

import net.minecraft.level.Level;
import net.minecraft.level.structure.Structure;
import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.util.Matrix3F;
import paulevs.bnb.world.generator.decorator.BNBChunkStatus;
import paulevs.bnb.world.structure.BNBStructureStage;

import java.util.Random;

public class FlameQuartzClusterStructure extends Structure implements BNBStructureStage {
	private static final Vec3D POS = Vec3D.make(0.0, 0.0, 0.0);
	private static final Matrix3F TEMP = new Matrix3F();
	private static final Matrix3F TRANSFORM = new Matrix3F();
	private static final Vec3D UP = Vec3D.make(0.0, 1.0, 0.0);
	private static final Vec3D POS_X = Vec3D.make(1.0, 0.0, 0.0);
	private static final BlockState[] PALETTE = new BlockState[] {
		BNBBlocks.FLAME_QUARTZ_RED.getDefaultState(),
		BNBBlocks.FLAME_QUARTZ_ORANGE.getDefaultState(),
		BNBBlocks.FLAME_QUARTZ_YELLOW.getDefaultState()
	};
	private static final Vec3D[] HEXAGON_PLANES = new Vec3D[12];
	
	private final boolean isCeiling;
	
	public FlameQuartzClusterStructure(boolean isCeiling) {
		this.isCeiling = isCeiling;
	}
	
	@Override
	public BNBChunkStatus bnb_getTargetStatus() {
		return BNBChunkStatus.POPULATION_BIG;
	}
	
	@Override
	public boolean generate(Level level, Random random, int x, int y, int z) {
		if (isCeiling != y > 176) return false;
		
		int offset = random.nextInt(5) + 5;
		if (!isCeiling) offset = -offset;
		y += offset;
		if (!level.getBlockState(x, y, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		if (!level.getBlockState(x, y + offset, z).isIn(BNBBlockTags.NETHERRACK_TERRAIN)) return false;
		
		byte count = (byte) (4 + random.nextInt(6));
		float maxAngle = (float) Math.PI * 2.0F;
		float scale = 0.5F + (float) Math.pow(random.nextFloat(), 2.0F);
		float scaleR = MathHelper.lerp(0.25F, scale, 1.0F);
		
		for (byte i = 0; i < count; i++) {
			float delta = (float) i / count;
			float rotX = (delta + random.nextFloat() * 0.2F) * 1.5F;
			if (isCeiling) rotX = (float) Math.PI - rotX;
			float rotY = (random.nextFloat() * maxAngle);
			float height = MathHelper.lerp(delta, 20.0F, 5.0F) + random.nextFloat() * 2.5F;
			float radius = MathHelper.lerp(delta, 2.5F, 1.5F) + random.nextFloat() * 0.5F;
			makeCrystal(level, random, x, y, z, rotX, rotY, height * scale, radius * scaleR);
		}
		
		return false;
	}
	
	private void makeCrystal(Level level, Random random, int x, int y, int z, float rotationX, float rotationY, float height, float radius) {
		TRANSFORM.identity()
			.multiply(TEMP.rotation(UP, rotationY))
			.multiply(TEMP.rotation(POS_X, rotationX))
			.multiply(TEMP.scale(radius, height, radius));
		
		int minX = 0;
		int minY = 0;
		int minZ = 0;
		int maxX = 0;
		int maxY = 0;
		int maxZ = 0;
		
		for (byte i = 0; i < 8; i++) {
			POS.x = ((i & 1) << 1) - 1;
			POS.y = (((i >> 1) & 1) << 1) - 1;
			POS.z = (((i >> 2) & 1) << 1) - 1;
			TRANSFORM.transform(POS);
			minX = Math.min(minX, MCMath.floor(POS.x));
			minY = Math.min(minY, MCMath.floor(POS.y));
			minZ = Math.min(minZ, MCMath.floor(POS.z));
			maxX = Math.max(maxX, (int) Math.ceil(POS.x));
			maxY = Math.max(maxY, (int) Math.ceil(POS.y));
			maxZ = Math.max(maxZ, (int) Math.ceil(POS.z));
		}
		
		POS.x = 0.0;
		POS.z = 0.0;
		POS.y = 1.0;
		TRANSFORM.transform(POS);
		int px = x + (int) (POS.x);
		int py = y + (int) (POS.y);
		int pz = z + (int) (POS.z);
		
		TRANSFORM.invert();
		
		Vec3D point = Vec3D.make(0.0, random.nextFloat() * 0.1F + 0.9F, 0.0);
		Vec3D normal = Vec3D.make(random.nextFloat() - 0.5F, 1.0, random.nextFloat() - 0.5F);
		double length = 1.0 / normal.length();
		normal.x *= length;
		normal.y *= length;
		normal.z *= length;
		
		for (int dx = minX; dx <= maxX; dx++) {
			for (int dy = minY; dy <= maxY; dy++) {
				for (int dz = minZ; dz <= maxZ; dz++) {
					POS.x = dx;
					POS.y = dy;
					POS.z = dz;
					TRANSFORM.transform(POS);
					if (!isInHexagon()) continue;
					if (!planeTest(point, normal)) continue;
					int color = Math.round(MathHelper.lerp(0.3F, (float) POS.y * 0.8F + 0.55F, random.nextFloat()) * 2.0F);
					color = MathHelper.clamp(color, 0, 2);
					level.setBlockState(px + dx, py + dy, pz + dz, PALETTE[color]);
				}
			}
		}
	}
	
	private static boolean isInHexagon() {
		for (byte i = 0; i < 12; i += 2) {
			if (!planeTest(HEXAGON_PLANES[i], HEXAGON_PLANES[i | 1])) return false;
		}
		return true;
	}
	
	@SuppressWarnings("BooleanMethodIsAlwaysInverted")
	private static boolean planeTest(Vec3D point, Vec3D normal) {
		float dx = (float) (POS.x - point.x);
		float dy = (float) (POS.y - point.y);
		float dz = (float) (POS.z - point.z);
		float dot = dx * (float) normal.x + dy * (float) normal.y + dz * (float) normal.z;
		return dot < 0.0F;
	}
	
	static {
		float scale = (float) Math.PI * 2.0F / 6.0F;
		for (byte i = 0; i < 6; i++) {
			int index = i << 1;
			float angle = i * scale;
			double x = Math.sin(angle);
			double z = Math.cos(angle);
			HEXAGON_PLANES[index] = Vec3D.make(x, 0.0, z);
			HEXAGON_PLANES[index | 1] = Vec3D.make(x, 0.0, z);
		}
	}
}
