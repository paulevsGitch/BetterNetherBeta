package paulevs.bnb.block.stone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BeforeBlockRemoved;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class NetherrackStalactite extends TemplateBlock implements BeforeBlockRemoved {
	private static boolean inverted;
	
	public NetherrackStalactite(Identifier identifier) {
		super(identifier, Material.STONE);
		setLightOpacity(0);
		setDefaultState(getDefaultState().with(BNBBlockProperties.THICKNESS, 0));
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		builder.add(BNBBlockProperties.THICKNESS, BNBBlockProperties.INVERTED);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Box getOutlineShape(Level level, int x, int y, int z) {
		float thickness = MathHelper.lerp(level.getBlockState(x, y, z).get(BNBBlockProperties.THICKNESS) / 7.0F, 0.1F, 0.4F);
		float min = 0.5F - thickness;
		float max = 0.5F + thickness;
		setBoundingBox(min, 0.0F, min, max, 1.0F, max);
		return super.getOutlineShape(level, x, y, z);
	}
	
	@Override
	public Box getCollisionShape(Level level, int x, int y, int z) {
		return null;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public void onBlockPlaced(Level level, int x, int y, int z) {
		for (int i = 1; i < 8; i++) {
			y += inverted ? 1 : -1;
			BlockState state = level.getBlockState(x, y, z);
			if (!state.isOf(this)) break;
			level.setBlockStateWithoutNotifyingNeighbors(x, y, z, state.with(BNBBlockProperties.THICKNESS, i));
		}
	}
	
	@Override
	public void beforeBlockRemoved(Level level, int x, int y, int z) {
		inverted = level.getBlockState(x, y, z).get(BNBBlockProperties.INVERTED);
	}
	
	@Override
	public void onBlockRemoved(Level level, int x, int y, int z) {
		level.scheduleTick(x, y + (inverted ? -1 : 1), z, this.id, 1);
		for (int i = 0; i < 8; i++) {
			y += inverted ? 1 : -1;
			BlockState state = level.getBlockState(x, y, z);
			if (!state.isOf(this)) break;
			level.setBlockStateWithoutNotifyingNeighbors(x, y, z, state.with(BNBBlockProperties.THICKNESS, i));
		}
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z, int side) {
		if (side > 1) return false;
		Direction dir = Direction.byId(side).getOpposite();
		for (int i = 0; i < 8; i++) {
			y += dir.getOffsetY();
			BlockState state = level.getBlockState(x, y, z);
			if (!state.isOf(this)) return state.getBlock().isFullCube() && state.getBlock().isFullOpaque() && state.getBlock().material.blocksMovement();
		}
		return false;
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		inverted = context.getSide() == Direction.DOWN;
		return getDefaultState().with(BNBBlockProperties.INVERTED, inverted);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		boolean inverted = level.getBlockState(x, y, z).get(BNBBlockProperties.INVERTED);
		BlockState state = level.getBlockState(x, y + (inverted ? 1 : -1), z);
		if (state.isOf(this) || (state.getBlock().isFullCube() && state.getBlock().isFullOpaque() && state.getBlock().material.blocksMovement())) {
			return;
		}
		level.setBlockState(x, y, z, States.AIR.get());
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			level.playSound(
				x + 0.5,
				y + 0.5,
				z + 0.5,
				sounds.getBreakSound(),
				sounds.getVolume() * 0.5F,
				sounds.getPitch()
			);
		}
		level.updateBlock(x, y, z);
		level.scheduleTick(x, y + (inverted ? -1 : 1), z, this.id, 1);
	}
}
