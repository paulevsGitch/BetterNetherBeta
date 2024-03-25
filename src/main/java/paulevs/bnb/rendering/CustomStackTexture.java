package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.item.ItemStack;

public interface CustomStackTexture {
	@Environment(EnvType.CLIENT)
	int getTexture(ItemStack stack);
}
