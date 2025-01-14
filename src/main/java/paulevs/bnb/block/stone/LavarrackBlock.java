package paulevs.bnb.block.stone;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.Random;

public class LavarrackBlock extends TemplateBlock {
	public LavarrackBlock(Identifier identifier) {
		super(identifier, Material.STONE);
		setHardness(NETHERRACK.getHardness());
		setLightEmittance(0.75F);
		setTicksRandomly(true);
	}
	
	@Override
	public void onScheduledTick(Level level, int x, int y, int z, Random random) {
		if (level.isRemote || random.nextInt(100) > 0 || level.getBlockState(x, y + 1, z).isAir()) return;
		level.setBlockState(x, y + 1, z, FIRE.getDefaultState());
	}
}
