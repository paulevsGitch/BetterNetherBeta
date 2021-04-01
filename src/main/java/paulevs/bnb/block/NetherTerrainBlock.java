package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.block.sound.NetherBlockSounds;
import paulevs.bnb.block.types.NetherTerrain;

public class NetherTerrainBlock extends MultiBlock {
	public NetherTerrainBlock(String name, int id) {
		super(name, id, Material.STONE, NetherTerrain.class);
		this.setHardness(NETHERRACK.getHardness());
		this.sounds(NetherBlockSounds.NYLIUM);
	}
}
