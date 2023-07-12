package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BaseBlock;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.BaseItem;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import net.modificationstation.stationapi.api.util.math.BlockPos;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.Direction.Axis;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.CreativeUtil;
import paulevs.bnb.block.properties.BNBBlockProperties;
import paulevs.bnb.block.properties.BNBBlockProperties.SlabShape;

import java.util.ArrayList;
import java.util.List;

public class SlabBlock extends TemplateBlockBase {
	private final BaseBlock source;
	
	public SlabBlock(Identifier identifier, BaseBlock source) {
		super(identifier, source.material);
		setHardness(source.getHardness());
		setSounds(source.sounds);
		this.source = source;
	}
	
	@Override
	public void appendProperties(Builder<BaseBlock, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.SLAB);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Level level = context.getWorld();
		BlockPos pos = context.getBlockPos();
		Direction face = context.getSide().getOpposite();
		BlockState state = level.getBlockState(pos.offset(face));
		if (state.isOf(this)) {
			SlabShape slab = state.get(BNBBlockProperties.SLAB);
			if (slab != SlabShape.FULL && slab.getDirection().getAxis() != face.getAxis()) {
				PlayerBase player = context.getPlayer();
				if (player != null && !player.isChild()) {
					return state;
				}
			}
		}
		return getDefaultState().with(BNBBlockProperties.SLAB, SlabShape.fromDirection(face));
	}
	
	@Override
	public void updateBoundingBox(BlockView view, int x, int y, int z) {
		if (!(view instanceof BlockStateView bsView)) {
			this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		
		BlockState state = bsView.getBlockState(x, y, z);
		SlabShape shape = state.get(BNBBlockProperties.SLAB);
		if (shape == SlabShape.FULL) {
			this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		
		Direction dir = shape.getDirection();
		if (dir == null) {
			this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
			return;
		}
		
		int dx = dir.getOffsetX();
		int dy = dir.getOffsetY();
		int dz = dir.getOffsetZ();
		
		float minX = dx == 0 ? 0 : dx > 0 ? 0.5F : 0;
		float minY = dy == 0 ? 0 : dy > 0 ? 0.5F : 0;
		float minZ = dz == 0 ? 0 : dz > 0 ? 0.5F : 0;
		float maxX = dx == 0 ? 1 : dx > 0 ? 1 : 0.5F;
		float maxY = dy == 0 ? 1 : dy > 0 ? 1 : 0.5F;
		float maxZ = dz == 0 ? 1 : dz > 0 ? 1 : 0.5F;
		
		this.setBoundingBox(minX, minY, minZ, maxX, maxY, maxZ);
	}
	
	@Override
	public void doesBoxCollide(Level level, int x, int y, int z, Box box, ArrayList list) {
		updateBoundingBox(level, x, y, z);
		super.doesBoxCollide(level, x, y, z, box, list);
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
	}
	
	@Override
	public int getTextureForSide(BlockView view, int x, int y, int z, int side) {
		return source.getTextureForSide(view, x, y, z, side);
	}
	
	@Override
	public int getTextureForSide(int i, int j) {
		return source.getTextureForSide(i, j);
	}
	
	@Override
	public int getTextureForSide(int side) {
		return source.getTextureForSide(side);
	}
	
	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		int count = state.get(BNBBlockProperties.SLAB) == SlabShape.FULL ? 2 : 1;
		return List.of(new ItemStack(this, count));
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerBase player) {
		ItemStack stack = player.getHeldItem();
		if (stack == null) return false;
		
		BaseItem item = stack.getType();
		if (!(item instanceof BlockItem blockItem)) return false;
		
		if (blockItem.id != this.id) return false;
		
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return false;
		
		SlabShape slab = state.get(BNBBlockProperties.SLAB);
		if (slab == SlabShape.FULL) return false;
		
		HitResult hit = getHit(level, player);
		if (hit == null || hit.type != HitType.BLOCK) return false;
		
		double dx = hit.pos.x - x;
		double dy = hit.pos.y - y;
		double dz = hit.pos.z - z;
		
		if (dx < 0 || dx > 1 || dy < 0 || dy > 1 || dz < 0 || dz > 1) return false;
		
		Axis axis = slab.getDirection().getAxis();
		
		if (axis == Axis.X && Math.abs(dx - 0.5) > 0.0001) return false;
		if (axis == Axis.Y && Math.abs(dy - 0.5) > 0.0001) return false;
		if (axis == Axis.Z && Math.abs(dz - 0.5) > 0.0001) return false;
		
		level.setBlockState(x, y, z, state.with(BNBBlockProperties.SLAB, SlabShape.FULL));
		level.playSound(x + 0.5, y + 0.5, z + 0.5, this.sounds.getWalkSound(), 1.0F, 1.0F);
		level.callAreaEvents(x, y, z);
		
		if (!CreativeUtil.isCreative(player)) {
			stack.count--;
		}
		
		return true;
	}
	
	@Override
	@Environment(value = EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		if (!(view instanceof BlockStateView bsView)) {
			return super.isSideRendered(view, x, y, z, side);
		}
		
		Direction face = Direction.byId(side);
		BlockState state1 = bsView.getBlockState(x - face.getOffsetX(), y - face.getOffsetY(), z - face.getOffsetZ());
		BlockState state2 = bsView.getBlockState(x, y, z);
		
		if (state1.getBlock() instanceof SlabBlock && state2.getBlock() instanceof SlabBlock) {
			SlabShape slab2 = state2.get(BNBBlockProperties.SLAB);
			if (slab2 == SlabShape.FULL) return false;
			SlabShape slab1 = state1.get(BNBBlockProperties.SLAB);
			return slab1 != slab2;
		}
		
		return super.isSideRendered(view, x, y, z, side);
	}
	
	// Item Bounding Box
	@Override
	@Environment(EnvType.CLIENT)
	public void updateRenderBounds() {
		this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
	}
	
	private HitResult getHit(Level level, PlayerBase player) {
		double dist = 5.0;
		float toRadians = (float) Math.PI / 180;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch);
		
		double x = player.prevX + (player.x - player.prevX);
		double y = player.prevY + (player.y - player.prevY) + 1.62 - (double) player.standingEyeHeight;
		double z = player.prevZ + (player.z - player.prevZ);
		Vec3f pos = Vec3f.getFromCacheAndSet(x, y, z);
		
		float yaw = player.prevYaw + (player.yaw - player.prevYaw);
		yaw = -yaw * toRadians - (float) Math.PI;
		float cosYaw = MathHelper.cos(yaw);
		float sinYaw = MathHelper.sin(yaw);
		float cosPitch = -MathHelper.cos(-pitch * toRadians);
		
		Vec3f dir = pos.add(
			sinYaw * cosPitch * dist,
			(MathHelper.sin(-pitch * ((float) Math.PI / 180))) * dist,
			cosYaw * cosPitch * dist
		);
		
		return level.getHitResult(pos, dir, false);
	}
}
