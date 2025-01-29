package paulevs.bnb.block.fluid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateStillLiquidBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.particle.AcidParticleEntity;

import java.util.Random;

public class SulphuricAcidStillBlock extends TemplateStillLiquidBlock {
	protected static final int TICK_RATE = 8;
	public static int texture;
	public Block flowingFluid;
	
	public SulphuricAcidStillBlock(Identifier identifier) {
		super(identifier, BNBBlockMaterials.SULPHURIC_ACID);
		setTicksRandomly(true);
		disableAutoItemRegistration();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return Math.max(super.getBrightness(blockView, x, y, z) * 1.5F, 0.25F);
	}
	
	@Override
	public int getTexture(int side) {
		return side < 2 ? texture : flowingFluid.getTexture(side);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		level.stopPhysics = true;
		Chunk chunk = level.getChunk(x, z);
		int cx = x & 15;
		int cz = z & 15;
		if (chunk.getBlockState(cx, y, cz).isOf(this)) {
			int meta = chunk.getMeta(cx, y, cz);
			chunk.setBlockStateWithMetadata(cx, y, cz, flowingFluid.getDefaultState(), meta);
			level.scheduleTick(x, y, z, flowingFluid.id, this.getTickrate());
			level.updateArea(x, y, z, x, y, z);
		}
		level.stopPhysics = false;
	}
	
	@Override
	public int getRenderPass() {
		return 1;
	}
	
	@Environment(value=EnvType.CLIENT)
	public void onRandomClientTick(Level level, int x, int y, int z, Random random) {
		acidClientTick(level, x, y, z, random);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		acidCommonTick(level, x, y, z, random);
	}
	
	@Override
	public int getTickrate() {
		return TICK_RATE;
	}
	
	@Environment(EnvType.CLIENT)
	protected static void acidClientTick(Level level, int x, int y, int z, Random random) {
		if (random.nextInt(16) == 0 && level.isAir(x, y + 1, z)) {
			BNBClient.getMinecraft().particleManager.addParticle(new AcidParticleEntity(
				level,
				x + random.nextFloat(),
				y + 0.1F,
				z + random.nextFloat()
			));
		}
		if (random.nextInt(1500) == 0) {
			BNBClient.getMinecraft().particleManager.addParticle(new AcidParticleEntity(
				level,
				x + random.nextFloat(),
				y + random.nextFloat(),
				z + random.nextFloat()
			));
		}
	}
	
	protected static void acidCommonTick(Level level, int x, int y, int z, Random random) {
		if (level.isRemote) return;
		for (byte i = 0; i < 3; i++) {
			Direction dir = Direction.byId(random.nextInt(6));
			int px = x + dir.getOffsetX();
			int py = y + dir.getOffsetY();
			int pz = z + dir.getOffsetZ();
			BlockState side = level.getBlockState(px, py, pz);
			if (side.isOf(NETHERRACK)) {
				level.setBlockState(px, py, pz, BNBBlocks.SULPHURIFIED_NETHERRACK.getDefaultState());
			}
			else if (side.isOf(BNBBlocks.SULPHURIFIED_NETHERRACK)) {
				level.setBlockState(px, py, pz, BNBBlocks.SULPHURIC_NETHERRACK.getDefaultState());
			}
		}
	}
}
