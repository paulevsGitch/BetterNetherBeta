package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.sound.NetherBlockSounds;
import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.BonemealUtil;
import paulevs.bnb.util.MHelper;

public class NetherTerrainBlock extends MultiBlock {
	public NetherTerrainBlock(String name, int id) {
		super(name, id, Material.STONE, NetherTerrain.class);
		this.setHardness(NETHERRACK.getHardness());
		this.sounds(NetherBlockSounds.NYLIUM);
	}
	
	public boolean growGrass(Level level, int x, int y, int z, int meta) {
		NetherTerrain terrain = (NetherTerrain) getVariant(meta);
		BlockState[] grasses = BonemealUtil.getGrasses(terrain);
		if (grasses == null) {
			return false;
		}
		BlockState grass = grasses[level.rand.nextInt(grasses.length)];
		grass.setBlock(level, x, y + 1, z);
		for (int i = 0; i < 30; i++) {
			int px = MathHelper.floor(x + MHelper.getRandom().nextGaussian() * 2 + 0.5);
			int pz = MathHelper.floor(z + MHelper.getRandom().nextGaussian() * 2 + 0.5);
			for (int j = -1; j <= 1; j++) {
				if (BlockUtil.getBlock(level, px, y - j, pz) instanceof NetherTerrainBlock && level.getTileId(px, y - j + 1, pz) == 0) {
					grass = grasses[level.rand.nextInt(grasses.length)];
					grass.setBlockAndUpdate(level, px, y - j + 1, pz);
				}
			}
		}
		return true;
	}
}
