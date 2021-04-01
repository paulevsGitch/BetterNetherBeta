package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import paulevs.bnb.block.types.GlowingFur;

public class GlowingFurBlock extends NetherCeilPlantBlock {
	public GlowingFurBlock(String name, int id) {
		super(name, id, GlowingFur.class);
		this.setLightEmittance(1F);
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 6;
	}
}
