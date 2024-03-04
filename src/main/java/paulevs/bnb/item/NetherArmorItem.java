package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.template.item.TemplateArmorItem;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherArmorItem extends TemplateArmorItem /*implements ArmorTextureProvider*/ {
	private final String armourTexture1;
	private final String armourTexture2;
	//private final Identifier textureID;
	
	public NetherArmorItem(Identifier identifier, int level, int protection, int slot) {
		super(identifier, level, protection, slot);
		this.setTranslationKey(identifier);
		String prefix = identifier.path.substring(0, identifier.path.indexOf('_'));
		//textureID = BNB.id(identifier.path.substring(0, identifier.path.indexOf('_')));
		this.armourTexture1 = "/assets/bnb/stationapi/textures/armor/" + prefix + "_1.png";
		this.armourTexture2 = "/assets/bnb/stationapi/textures/armor/" + prefix + "_2.png";
	}
	
	@Environment(EnvType.CLIENT)
	public String getArmourTexture(int type) {
		return type == 2 ? armourTexture2 : armourTexture1;
	}
	
	/*@Override
	public Identifier getTexture(ArmorItem armorItem) {
		return textureID;
	}*/
}
