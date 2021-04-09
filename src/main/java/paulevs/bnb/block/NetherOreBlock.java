package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.sound.NetherBlockSounds;
import paulevs.bnb.block.types.NetherOre;

public class NetherOreBlock extends MultiBlock {
	public NetherOreBlock(String name, int id) {
		super(name, id, Material.STONE, NetherOre.class);
		this.setHardness(NETHERRACK.getHardness());
		this.sounds(NetherBlockSounds.NYLIUM);
	}
}
