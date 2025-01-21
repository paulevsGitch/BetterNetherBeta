package paulevs.bnb.item;

import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.util.Identifier;

public class NetherArmorTooltipItem extends NetherArmorItem implements CustomTooltipProvider {
	private final String[] tooltipKeys;
	private final String[] tooltipRes;
	
	public NetherArmorTooltipItem(Identifier identifier, int level, int protection, int slot, String... tooltipKeys) {
		super(identifier, level, protection, slot);
		this.tooltipKeys = new String[tooltipKeys.length + 1];
		System.arraycopy(tooltipKeys, 0, this.tooltipKeys, 1, tooltipKeys.length);
		this.tooltipKeys[0] = getTranslationKey() + ".name";
		tooltipRes = new String[this.tooltipKeys.length];
	}
	
	@Override
	public String[] getTooltip(ItemStack stack, String originalTooltip) {
		for (byte i = 0; i < tooltipRes.length; i++) {
			tooltipRes[i] = I18n.translate(tooltipKeys[i]);
		}
		return tooltipRes;
	}
}
