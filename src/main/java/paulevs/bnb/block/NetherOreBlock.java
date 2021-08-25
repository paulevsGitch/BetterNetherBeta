package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.types.NetherOre;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class NetherOreBlock extends MultiBlock {
	public NetherOreBlock(String name, int id) {
		super(name, id, Material.STONE, NetherOre.class);
		this.setHardness(NETHERRACK.getHardness());
	}
	
	@Override
	public int getDropId(int meta, Random rand) {
		NetherOre ore = (NetherOre) getVariant(meta);
		return ore.getDropID();
	}
	
	@Override
	public int getDropCount(Random random) {
		return MHelper.randRange(1, 2, random);
	}
}
