package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.TileView;
import paulevs.bnb.block.types.NetherLeaves;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

public class NetherLeavesBlock extends MultiBlock implements BlockWithLight {
	public NetherLeavesBlock(String name, int id) {
		super(name, id, Material.LEAVES, NetherLeaves.class);
		this.setHardness(LEAVES.getHardness());
		this.sounds(GRASS_SOUNDS);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		String name = variants[clampMeta(meta)].getTexture(side, meta);
		if (BlockUtil.isLightPass()) {
			return TextureListener.getBlockTexture(name + "_light", name);
		}
		else {
			return TextureListener.getBlockTexture(name);
		}
	}
	
	@Environment(EnvType.CLIENT)
	public float method_1604(TileView arg, int i, int j, int k) {
		return BlockUtil.isLightPass() ? 1F : super.method_1604(arg, i, j, k);
	}
}
