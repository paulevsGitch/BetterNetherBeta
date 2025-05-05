package paulevs.bnb.block.crafting;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.FurnaceBlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.item.ItemPlacementContext;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.template.block.TemplateBlockWithEntity;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.Random;

public class BNBFurnaceBlock extends TemplateBlockWithEntity {
	private static boolean updating;
	public final String guiTranslationKey;
	public final int cookingTime;
	
	public BNBFurnaceBlock(Identifier identifier, int cookingTime, String guiTranslationKey) {
		super(identifier, Material.STONE);
		setHardness(1.0F);
		setDefaultState(getDefaultState().with(BNBBlockProperties.LIT, false));
		setLuminance(BNBFurnaceBlock::getLight);
		this.guiTranslationKey = guiTranslationKey;
		this.cookingTime = cookingTime;
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.DIRECTION_HORIZONTAL, BNBBlockProperties.LIT);
	}
	
	@Override
	public BlockState getPlacementState(ItemPlacementContext context) {
		Direction dir = context.getHorizontalPlayerFacing();
		return getDefaultState().with(BNBBlockProperties.DIRECTION_HORIZONTAL, dir);
	}
	
	@Override
	protected BlockEntity createBlockEntity() {
		return new FurnaceBlockEntity();
	}
	
	@Override
	public boolean canUse(Level level, int x, int y, int z, PlayerEntity player) {
		if (level.isRemote) return true;
		FurnaceBlockEntity entity = (FurnaceBlockEntity) level.getBlockEntity(x, y, z);
		if (entity == null) return false;
		player.openFurnaceScreen(entity);
		return true;
	}
	
	@Override
	@Environment(value= EnvType.CLIENT)
	public void onRandomClientTick(Level level, int x, int y, int z, Random random) {
		BlockState state = level.getBlockState(x, y, z);
		if (!state.get(BNBBlockProperties.LIT)) return;
		
		double px = x + 0.5;
		double py = y + random.nextFloat() * 0.375;
		double pz = z + 0.5;
		
		Direction dir = state.get(BNBBlockProperties.DIRECTION_HORIZONTAL);
		if (dir.getOffsetX() != 0) {
			px -= dir.getOffsetX() * 0.52;
			pz += random.nextFloat() * 0.75 - 0.375;
		}
		else {
			pz -= dir.getOffsetZ() * 0.52;
			px += random.nextFloat() * 0.75 - 0.375;
		}
		
		level.addParticle("smoke", px, py, pz, 0.0, 0.0, 0.0);
		level.addParticle("flame", px, py, pz, 0.0, 0.0, 0.0);
	}
	
	@Override
	public void onBlockRemoved(Level level, int x, int y, int z) {
		if (updating || level.isRemote) return;
		FurnaceBlockEntity entity = (FurnaceBlockEntity) level.getBlockEntity(x, y, z);
		if (entity == null) return;
		for (byte i = 0; i < entity.getInventorySize(); i++) {
			ItemStack stack = entity.getItem(i);
			if (stack == null) continue;
			drop(level, x, y, z, stack);
		}
		level.removeBlockEntity(x, y, z);
	}
	
	public static void updateState(boolean lit, Level level, int x, int y, int z) {
		updating = true;
		BlockEntity entity = level.getBlockEntity(x, y, z);
		level.setBlockState(x, y, z, level.getBlockState(x, y, z).with(BNBBlockProperties.LIT, lit));
		entity.validate();
		level.setBlockEntity(x, y, z, entity);
		updating = false;
	}
	
	private static int getLight(BlockState state) {
		return state.get(BNBBlockProperties.LIT) ? 15 : 0;
	}
}
