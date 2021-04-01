package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.TileView;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.block.types.NetherWood;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;

public class NetherWoodBlock extends MultiBlock implements BlockWithLight {
	public NetherWoodBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD, NetherWood.class);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		String name = variants[clampMeta(meta)].getTexture(side, meta);
		if (BlockUtil.isItemRender()) {
			return TextureListener.getBlockTexture(name + "_inventory", name);
		}
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
