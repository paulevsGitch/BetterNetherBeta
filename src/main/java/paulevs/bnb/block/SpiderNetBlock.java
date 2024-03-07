package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.monster.SpiderEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.block.States;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.block.property.BNBBlockProperties;
import paulevs.bnb.sound.BNBSounds;
import paulevs.vbe.utils.CreativeUtil;

public class SpiderNetBlock extends TemplateBlock {
	public SpiderNetBlock(Identifier id) {
		super(id, BNBBlockMaterials.SPIDER_NET);
		setSounds(BNBSounds.MOSS_BLOCK);
		BlockState state = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			state = state.with(BNBBlockProperties.FACES[i], false);
		}
		setDefaultState(state);
		setHardness(0.2F);
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.FACES);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Level level = context.getWorld();
		BlockPos pos = context.getBlockPos();
		return getFacingState(level, pos.getX(), pos.getY(), pos.getZ());
	}
	
	@Override
	public void onAdjacentBlockUpdate(Level level, int x, int y, int z, int l) {
		BlockState state = level.getBlockState(x, y, z);
		BlockState facing = getFacingState(level, x, y, z);
		
		if (isEmpty(facing)) {
			drop(level, x, y, z, l);
			level.setBlockState(x, y, z, States.AIR.get());
			return;
		}
		
		if (facing != state) {
			level.setBlockState(x, y, z, facing);
		}
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		BlockState facing = getFacingState(level, x, y, z);
		return !isEmpty(facing);
	}
	
	@Override
	public void updateBoundingBox(BlockView view, int x, int y, int z) {
		if (view instanceof Level) {
			BlockState state = ((Level) view).getBlockState(x, y, z);
			float min = 2F / 16F;
			float max = 1 - min;
			float x1 = 0;
			float y1 = 0;
			float z1 = 0;
			float x2 = 1;
			float y2 = 1;
			float z2 = 1;
			
			Direction dir = null;
			for (byte i = 0; i < 6; i++) {
				if (!state.get(BNBBlockProperties.FACES[i])) continue;
				if (dir != null) {
					dir = null;
					break;
				}
				dir = Direction.byId(i);
			}
			
			if (dir != null) {
				if (dir.getOffsetX() > 0) x1 = max;
				else if (dir.getOffsetX() < 0) x2 = min;
				else if (dir.getOffsetY() > 0) y1 = max;
				else if (dir.getOffsetY() < 0) y2 = min;
				else if (dir.getOffsetZ() > 0) z1 = max;
				else if (dir.getOffsetZ() < 0) z2 = min;
			}
			
			this.setBoundingBox(x1, y1, z1, x2, y2, z2);
			return;
		}
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
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
	public void onEntityCollision(Level level, int x, int y, int z, Entity entity) {
		if (entity instanceof SpiderEntity) return;
		if (entity instanceof PlayerEntity player && CreativeUtil.isCreative(player)) return;
		entity.inCobweb = true;
	}
	
	@Override
	public void afterBreak(Level level, PlayerEntity player, int x, int y, int z, int meta) {
		if (level.isRemote) super.afterBreak(level, player, x, y, z, meta);
		ItemStack heldItem = player.getHeldItem();
		if (heldItem == null || heldItem.getType() != Item.shears) {
			if (level.random.nextBoolean()) {
				int count = level.random.nextInt(2) + 1;
				drop(level, x, y, z, new ItemStack(Item.string, count));
			}
			return;
		}
		drop(level, x, y, z, new ItemStack(this));
		if (!CreativeUtil.isCreative(player)) heldItem.applyDamage(1, player);
	}
	
	private boolean isSupport(BlockState state) {
		return (state.getMaterial() == Material.LEAVES || state.isOpaque()) && state.getBlock().isFullCube();
	}
	
	public BlockState getFacingState(Level level, int x, int y, int z) {
		MutableBlockPos pos = new MutableBlockPos();
		BlockState self = getDefaultState();
		for (byte i = 0; i < 6; i++) {
			Direction dir = Direction.byId(i);
			pos.set(x, y, z).move(dir);
			BlockState state = level.getBlockState(pos);
			self = self.with(BNBBlockProperties.FACES[i], isSupport(state));
		}
		return self;
	}
	
	public static boolean isEmpty(BlockState state) {
		for (byte i = 0; i < 6; i++) {
			if (state.get(BNBBlockProperties.FACES[i])) return false;
		}
		return true;
	}
}
