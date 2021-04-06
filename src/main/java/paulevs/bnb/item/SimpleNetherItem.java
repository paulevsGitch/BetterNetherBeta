package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import paulevs.bnb.listeners.TextureListener;

public class SimpleNetherItem extends NetherItem {
	private final String texture;
	
	public SimpleNetherItem(String name, int id) {
		super(name, id);
		this.texture = name;
	}

	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture(texture);
	}
}
