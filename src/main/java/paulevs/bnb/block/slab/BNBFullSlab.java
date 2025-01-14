package paulevs.bnb.block.slab;

import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.state.StateManager.Builder;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockProperties;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BNBFullSlab extends BNBSlab {
	public static PlayerEntity player;
	private Block halfBlock;
	
	public BNBFullSlab(Identifier id, Block source) {
		super(id, source);
		Block.EMITTANCE[this.id] = Block.EMITTANCE[source.id];
		this.resistance = source.getHardness() * 5F;
		this.hardness = source.getHardness() * 0.5F;
		disableAutoItemRegistration();
	}
	
	@Override
	public void appendProperties(Builder<Block, BlockState> builder) {
		super.appendProperties(builder);
		builder.add(BNBBlockProperties.AXIS);
	}
	
	public void setHalfBlock(Block halfBlock) {
		this.halfBlock = halfBlock;
	}
	
	@Override
	public List<ItemStack> getDropList(Level level, int x, int y, int z, BlockState state, int meta) {
		return Collections.singletonList(new ItemStack(halfBlock, 2));
	}
	
	@Override
	public void doesBoxCollide(Level level, int x, int y, int z, Box box, ArrayList list) {
		this.setBoundingBox(0.0f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f);
		super.doesBoxCollide(level, x, y, z, box, list);
	}
}
