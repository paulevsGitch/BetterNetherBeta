package paulevs.bnb.block.falling;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.particle.RedstoneParticle;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.mixin.client.ParticleEntityAccessor;

import javax.swing.plaf.nimbus.State;
import java.util.List;
import java.util.Random;

public class AshBlock extends BNBFallingBlock {
	public AshBlock(Identifier id) {
		super(id);
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		return List.of(new ItemStack(BNBItems.ASH, 4));
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void onRandomClientTick(Level level, int x, int y, int z, Random random) {
		if (level.random.nextInt(32) > 0) return;
		updateBoundingBox(level, x, y, z);
		RedstoneParticle particle = new RedstoneParticle(
			level, x + random.nextFloat(), y + maxY + 0.01, z + random.nextFloat(), 0.392F, 0.372F, 0.458F
		);
		particle.velocityX = level.random.nextFloat() * 0.04F - 0.02F;
		particle.velocityZ = level.random.nextFloat() * 0.04F - 0.02F;
		particle.velocityY = 0.02F + level.random.nextFloat() * 0.02F;
		((ParticleEntityAccessor) particle).bnb_setAge(40 + random.nextInt(40));
		BNBClient.getMinecraft().particleManager.addParticle(particle);
	}
	
	@Override
	public void onBlockPlaced(Level level, int x, int y, int z) {
		super.onBlockPlaced(level, x, y, z);
		processFall(level, x, y, z);
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int id) {
		super.onAdjacentBlockUpdate(level, x, y, z, id);
		processFall(level, x, y, z);
	}
	
	@Override
	public boolean canReplace(BlockState state, ItemPlacementContext context) {
		return state.isOf(BNBBlocks.ASH_LAYER) || super.canReplace(state, context);
	}
	
	protected void processFall(Level level, int x, int y, int z) {
		BlockState self = level.getBlockState(x, y, z);
		if (!self.isOf(this)) return;
		
		BlockState below = level.getBlockState(x, y - 1, z);
		if (!below.isOf(BNBBlocks.ASH_LAYER)) return;
		
		level.setBlockState(x, y - 1, z, self);
		level.setBlockState(x, y, z, below);
	}
}
