package paulevs.bnb.block.falling;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BeforeBlockRemoved;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.item.BNBItems;
import paulevs.bnb.util.WorldUtil;

import java.util.ArrayList;
import java.util.List;

public class AshLayerBlock extends AshBlock implements BeforeBlockRemoved {
	private static BlockState beforeRemove;
	
	public AshLayerBlock(Identifier id) {
		super(id);
		setDefaultState(getDefaultState().with(BNBBlockProperties.LAYER, 0));
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.LAYER);
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		if (!beforeRemove.isOf(this)) return List.of(new ItemStack(BNBItems.ASH));
		return List.of(new ItemStack(BNBItems.ASH, beforeRemove.get(BNBBlockProperties.LAYER) + 1));
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
	public void updateBoundingBox(BlockView view, int x, int y, int z) {
		BlockState state = ((BlockStateView) view).getBlockState(x, y, z);
		if (!state.isOf(this)) return;
		float h = state.get(BNBBlockProperties.LAYER) * 0.25F + 0.25F;
		setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, h, 1.0F);
	}
	
	@Override
	public void doesBoxCollide(Level level, int x, int y, int z, Box box, ArrayList list) {
		updateBoundingBox(level, x, y, z);
		super.doesBoxCollide(level, x, y, z, box, list);
		this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerEntity player) {
		ItemStack stack = player.getHeldItem();
		if (stack == null) return false;
		
		Item item = stack.getType();
		if (!(item instanceof BlockItem blockItem)) return false;
		
		if (blockItem.getBlock() != this) return false;
		
		BlockState state = level.getBlockState(x, y, z);
		if (!state.isOf(this)) return false;
		
		HitResult hit = WorldUtil.raycast(level, player);
		if (hit == null || hit.type != HitType.BLOCK) return false;
		
		float dx = (float) (hit.pos.x - x);
		float dy = (float) (hit.pos.y - y);
		float dz = (float) (hit.pos.z - z);
		
		if (dx <= 0.0F || dx >= 1.0F || dy <= 0.0F || dy >= 1.0F || dz <= 0.0F || dz >= 1.0F) return false;
		
		int h = MCMath.floor(dy * 4.0F);
		state = h > 2 ? BNBBlocks.ASH_BLOCK.getDefaultState() : state.with(BNBBlockProperties.LAYER, h);
		
		level.setBlockState(x, y, z, state);
		level.playSound(x + 0.5, y + 0.5, z + 0.5, sounds.getWalkSound(), 1.0F, 1.0F);
		level.updateBlock(x, y, z);
		
		if (!BNB.isCreative(player)) stack.count--;
		
		return true;
	}
	
	@Override
	@Environment(value = EnvType.CLIENT)
	public boolean isSideRendered(BlockView view, int x, int y, int z, int side) {
		if (!(view instanceof BlockStateView bsView) || side < 2) {
			return super.isSideRendered(view, x, y, z, side);
		}
		
		Direction face = Direction.byId(side);
		BlockState selfState = bsView.getBlockState(x, y, z);
		BlockState sideState = bsView.getBlockState(x - face.getOffsetX(), y - face.getOffsetY(), z - face.getOffsetZ());
		
		if (sideState.getBlock() instanceof AshLayerBlock && selfState.getBlock() instanceof AshLayerBlock) {
			int slab2 = selfState.get(BNBBlockProperties.LAYER);
			int slab1 = sideState.get(BNBBlockProperties.LAYER);
			return slab1 > slab2;
		}
		
		return super.isSideRendered(view, x, y, z, side);
	}
	
	/*@Override
	protected boolean processReplace(Level level, int x, int y, int z, BlockState replaceState) {
		//BlockState state = level.getBlockState(x, y, z);
		return false;
	}*/
	
	@Override
	protected void processFall(Level level, int x, int y, int z) {
		BlockState self = level.getBlockState(x, y, z);
		if (!self.isOf(this)) return;
		
		BlockState below = level.getBlockState(x, y - 1, z);
		if (!below.isOf(this)) return;
		
		int selfLayer = self.get(BNBBlockProperties.LAYER);
		int belowLayer = below.get(BNBBlockProperties.LAYER);
		int newBelowLayer = Math.min(belowLayer + selfLayer + 1, 3);
		int newSelfLayer = selfLayer - newBelowLayer + belowLayer;
		
		if (newBelowLayer != belowLayer) {
			below = newBelowLayer == 3 ? BNBBlocks.ASH_BLOCK.getDefaultState() : below.with(BNBBlockProperties.LAYER, newBelowLayer);
			level.setBlockStateWithNotify(x, y - 1, z, below);
		}
		
		if (newSelfLayer != selfLayer) {
			self = newSelfLayer < 0 ? States.AIR.get() : self.with(BNBBlockProperties.LAYER, newSelfLayer);
			level.setBlockStateWithNotify(x, y, z, self);
		}
	}
	
	@Override
	public void beforeBlockRemoved(Level level, int x, int y, int z) {
		beforeRemove = level.getBlockState(x, y, z);
	}
}
