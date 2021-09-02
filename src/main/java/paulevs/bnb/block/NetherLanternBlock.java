package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.TileView;
import paulevs.bnb.block.types.NetherLanternType;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.util.BlockUtil;

public class NetherLanternBlock extends MultiBlock implements BlockWithLight {
	public NetherLanternBlock(String registryName, int id) {
		super(registryName, id, Material.ORGANIC, NetherLanternType.class);
		this.setLightEmittance(1F);
		this.sounds(WOOL_SOUNDS);
		this.setHardness(0.5F);
	}
	
	@Override
	public float getEmissionIntensity() {
		return 3;
	}
	
	public float method_1604(TileView world, int x, int y, int z) {
		return BlockUtil.isLightPass() ? getEmissionIntensity() : world.method_1784(x, y, z, EMITTANCE[1]);
	}
}
