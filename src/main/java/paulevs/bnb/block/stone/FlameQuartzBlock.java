package paulevs.bnb.block.stone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.BlockStateView;

public class FlameQuartzBlock extends TemplateBlock {
	//private final boolean transparent;
	
	public FlameQuartzBlock(Identifier identifier, float density) {
		super(identifier, Material.STONE);
		setSounds(GLASS_SOUNDS);
		setLightEmittance(0.75F);
		//transparent = density < 1.0F;
	}
	
	@Override
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return 1.5F;
	}
	
	/*@Override
	public int getRenderPass() {
		return transparent ? 1 : 0;
	}
	
	@Override
	public boolean isFullOpaque() {
		return !transparent;
	}
	
	@Override
	@Environment(value= EnvType.CLIENT)
	public boolean isSideRendered(BlockView blockView, int x, int y, int z, int side) {
		//if (!transparent) return super.isSideRendered(blockView, x, y, z, side);
		return true;
		//BlockState state = ((BlockStateView) blockView).getBlockState(x, y, z);
		
		//return !state.isOf(this) && (!state.isOpaque() || !state.getBlock().isFullOpaque());
	}*/
}
