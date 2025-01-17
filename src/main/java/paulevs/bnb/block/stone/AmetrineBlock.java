package paulevs.bnb.block.stone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.world.BlockStateView;
import paulevs.bnb.noise.FractalNoise;
import paulevs.bnb.noise.PerlinNoise;

public class AmetrineBlock extends TemplateBlock {
	private static final FractalNoise NOISE = new FractalNoise(PerlinNoise::new);
	public static final int[] TEXTURES = new int[16];
	private final boolean transparent;
	
	public AmetrineBlock(Identifier identifier, float light, boolean transparent) {
		super(identifier, Material.STONE);
		setSounds(GLASS_SOUNDS);
		setLightEmittance(light);
		this.transparent = transparent;
		setLightOpacity(transparent ? 0 : 255);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return 1.5F;
	}
	
	@Override
	public int getRenderPass() {
		return transparent ? 1 : 0;
	}
	
	@Override
	public boolean isSideRendered(BlockView blockView, int x, int y, int z, int side) {
		if (transparent) {
			BlockState state = ((BlockStateView) blockView).getBlockState(x, y, z);
			if (state.isOf(this)) return false;
		}
		return super.isSideRendered(blockView, x, y, z, side);
	}
	
	@Override
	public boolean isFullOpaque() {
		return !transparent;
	}
	
	@Override
	public int getTexture(BlockView blockView, int x, int y, int z, int side) {
		int value = Math.round(NOISE.get(x * 0.05, y * 0.05, z * 0.05) * 15.0F);
		return TEXTURES[value];
	}
	
	static {
		NOISE.setOctaves(2);
	}
}
