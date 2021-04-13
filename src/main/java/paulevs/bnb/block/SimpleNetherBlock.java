package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import paulevs.bnb.listeners.TextureListener;

public class SimpleNetherBlock extends NetherBlock {
	private final String texture;
	
	public SimpleNetherBlock(String name, int id, Material material) {
		super(name, id, material);
		this.texture = name;
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return TextureListener.getBlockTexture(texture);
	}
}
