package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.armour.Armour;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.listeners.TextureListener;

public class NetherArmourItem extends Armour {
	private final String armourTexture1;
	private final String armourTexture2;
	private final String texture;
	
	public NetherArmourItem(String name, int id, int level, int protection, int slot) {
		super(id, level, protection, slot);
		this.setName(name);
		this.texture = name;
		this.armourTexture1 = BetterNetherBeta.getTexturePath("armour", name.substring(0, name.indexOf('_')) + "_1");
		this.armourTexture2 = BetterNetherBeta.getTexturePath("armour", name.substring(0, name.indexOf('_')) + "_2");
	}

	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture(texture);
	}
	
	public String getArmourTexture(int type) {
		return type == 2 ? armourTexture2 : armourTexture1;
	}
}
