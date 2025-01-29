package paulevs.bnb.block.fluid;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.level.BlockView;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.template.block.TemplateFlowingLiquidBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.property.BNBBlockMaterials;

import java.util.Random;

public class SulphuricAcidFlowingBlock extends TemplateFlowingLiquidBlock {
	public static int texture;
	public Block stillFluid;
	
	public SulphuricAcidFlowingBlock(Identifier identifier) {
		super(identifier, BNBBlockMaterials.SULPHURIC_ACID);
		disableAutoItemRegistration();
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return Math.max(super.getBrightness(blockView, x, y, z) * 1.5F, 0.25F);
	}
	
	@Override
	public int getTexture(int side) {
		return side > 1 ? texture : stillFluid.getTexture(side);
	}
	
	@Override
	public int getRenderPass() {
		return 1;
	}
	
	@Environment(value=EnvType.CLIENT)
	public void onRandomClientTick(Level level, int x, int y, int z, Random random) {
		SulphuricAcidStillBlock.acidClientTick(level, x, y, z, random);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		SulphuricAcidStillBlock.acidCommonTick(level, x, y, z, random);
		super.onScheduledTick(level, x, y, z, random);
	}
	
	@Override
	public int getTickrate() {
		return SulphuricAcidStillBlock.TICK_RATE;
	}
}
