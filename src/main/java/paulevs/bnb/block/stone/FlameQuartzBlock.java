package paulevs.bnb.block.stone;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class FlameQuartzBlock extends TemplateBlock {
	//private final float brightness;
	
	public FlameQuartzBlock(Identifier identifier, float light) {
		super(identifier, Material.STONE);
		setSounds(GLASS_SOUNDS);
		setLightEmittance(light);
		//brightness = light;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public float getBrightness(BlockView blockView, int x, int y, int z) {
		return 1.5F;//Math.max(brightness, super.getBrightness(blockView, x, y, z)) * 1.5F;
		//return super.getBrightness(blockView, x, y, z) * 1.5F;
	}
}
