package paulevs.bnb.block.plant;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.BlockPos;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.state.property.BooleanProperty;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.sound.BNBSounds;

import java.util.ArrayList;

public class MossCoverBlock extends BNBPlantBlock {
	private final BlockState fullState;
	private HitResult hit;
	
	public MossCoverBlock(Identifier id) {
		super(id, BNBBlockMaterials.NETHER_PLANT_REPLACEABLE, true);
		setSounds(BNBSounds.MOSS_BLOCK);
		BlockState state = getDefaultState();
		BlockState fullState = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			state = state.with(BNBBlockProperties.FACES[i], false);
			fullState = fullState.with(BNBBlockProperties.FACES[i], true);
		}
		setDefaultState(state);
		this.fullState = fullState;
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.FACES);
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		return state.isOf(this) || (state.getMaterial() != Material.LAVA && state.getMaterial().isReplaceable());
	}
	
	@Override
	public void doesBoxCollide(Level level, int x, int y, int z, Box box, ArrayList list) {}
	
	@Override
	public Item asItem() {
		return BNBItems.FALURIAN_MOSS_COVER;
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		BlockState state = level.getBlockState(x, y, z);
		BlockState newState = updateState(level, x, y, z, state);
		if (newState != state) {
			level.setBlockState(x, y, z, newState);
		}
	}
	
	@Override
	protected boolean canStay(Level level, int x, int y, int z) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return false;
		state = updateState(level, x, y, z, state);
		return !state.isAir();
	}
	
	@Override
	public HitResult getHitResult(Level level, int x, int y, int z, Vec3D start, Vec3D end) {
		BlockState state = level.getBlockState(x, y, z);
		
		final float min = 2.0F / 16.0F;
		final float max = 1.0F - min;
		
		HitResult result = null;
		
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			if (!state.get(BNBBlockProperties.getByDir(dir))) continue;
			
			float x1 = 0.0F;
			float y1 = 0.0F;
			float z1 = 0.0F;
			float x2 = 1.0F;
			float y2 = 1.0F;
			float z2 = 1.0F;
			
			if (dir.getOffsetX() > 0) x1 = max;
			else if (dir.getOffsetX() < 0) x2 = min;
			else if (dir.getOffsetY() > 0) y1 = max;
			else if (dir.getOffsetY() < 0) y2 = min;
			else if (dir.getOffsetZ() > 0) z1 = max;
			else if (dir.getOffsetZ() < 0) z2 = min;
			
			setBoundingBox(x1, y1, z1, x2, y2, z2);
			HitResult hit = super.getHitResult(level, x, y, z, start, end);
			
			if (hit == null) continue;
			if (result == null || hit.pos.distanceSqr(start) < result.pos.distanceSqr(start)) {
				result = hit;
			}
		}
		
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 0.0f, 0.0f, 0.0f);
		this.hit = result;
		return result;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public Box getOutlineShape(Level level, int x, int y, int z) {
		Minecraft minecraft = BNBClient.getMinecraft();
		if (minecraft == null || minecraft.hitResult == null) {
			return super.getOutlineShape(level, x, y, z);
		}
		
		final float min = 2.0F / 16.0F;
		final float max = 1.0F - min;
		
		float x1 = 0.0F;
		float y1 = 0.0F;
		float z1 = 0.0F;
		float x2 = 1.0F;
		float y2 = 1.0F;
		float z2 = 1.0F;
		
		Direction dir = Direction.byId(minecraft.hitResult.facing).getOpposite();
		
		if (dir.getOffsetX() > 0) x1 = max;
		else if (dir.getOffsetX() < 0) x2 = min;
		else if (dir.getOffsetY() > 0) y1 = max;
		else if (dir.getOffsetY() < 0) y2 = min;
		else if (dir.getOffsetZ() > 0) z1 = max;
		else if (dir.getOffsetZ() < 0) z2 = min;
		
		return Box.createAndCache(x + x1, y + y1, z + z1, x + x2, y + y2, z + z2);
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, BlockState state, int meta) {
		if (level.isRemote) return;
		
		if (!state.isOf(this) || hit == null) return;
		Direction dir = Direction.byId(hit.facing).getOpposite();
		BooleanProperty property = BNBBlockProperties.getByDir(dir);
		BlockState newState = state.with(property, false);
		if (newState == state) return;
		
		byte count = 0;
		for (byte i = 0; i < 6; i++) {
			if (newState.get(BNBBlockProperties.FACES[i])) count++;
		}
		if (count == 0) newState = States.AIR.get();
		
		level.setBlockStateWithNotify(x, y, z, newState);
		super.afterBreak(level, player, x, y, z, meta);
	}
	
	public BlockState getStateForItem(Level level, BlockPos pos, Direction side) {
		BooleanProperty property = BNBBlockProperties.getByDir(side);
		BlockState worldState = level.getBlockState(pos);
		if (worldState.isOf(this) && worldState.get(property)) return null;
		if (isSupport(level.getBlockState(pos.offset(side)))) {
			BlockState state = worldState.isOf(this) ? worldState : getDefaultState();
			return state.with(BNBBlockProperties.getByDir(side), true);
		}
		return null;
	}
	
	private boolean isSupport(BlockState state) {
		if (state.isOf(GLASS) || state.isOf(GLOWSTONE)) return true;
		return (state.getMaterial() == Material.LEAVES || state.isOpaque()) && state.getBlock().isFullCube();
	}
	
	private BlockState updateState(Level level, int x, int y, int z, BlockState state) {
		int sides = 6;
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			BooleanProperty property = BNBBlockProperties.getByDir(dir);
			if (!state.get(property)) {
				sides--;
				continue;
			}
			BlockState side = level.getBlockState(
				x + dir.getOffsetX(),
				y + dir.getOffsetY(),
				z + dir.getOffsetZ()
			);
			if (isSupport(side)) continue;
			state = state.with(property, false);
			sides--;
		}
		return sides == 0 ? States.AIR.get() : state;
	}
	
	public BlockState getStructureState(Level level, int x, int y, int z) {
		BlockState state = updateState(level, x, y, z, fullState);
		return state.isAir() ? null : state;
	}
}
