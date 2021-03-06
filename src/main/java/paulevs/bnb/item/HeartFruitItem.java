package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.food.FoodBase;
import net.minecraft.level.Level;
import paulevs.bnb.effects.StatusEffects;
import paulevs.bnb.listeners.TextureListener;

public class HeartFruitItem extends FoodBase {
	private final String texture;
	
	public HeartFruitItem(String name, int id) {
		super(id, 4, false);
		this.setName(name);
		this.texture = name;
	}
	
	@Override
	public ItemInstance use(ItemInstance item, Level level, PlayerBase player) {
		ItemInstance result = super.use(item, level, player);
		StatusEffects.addEffect(player, "additional_health");
		return result;
	}
	
	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture(texture);
	}
}
