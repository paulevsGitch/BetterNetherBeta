package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.food.FoodBase;
import net.minecraft.level.Level;
import paulevs.bnb.effects.AdditionalHealthEffect;
import paulevs.bnb.interfaces.StatusEffectable;
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
		StatusEffectable stated = (StatusEffectable) player;
		stated.addEffect(new AdditionalHealthEffect());
		return result;
	}
	
	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture(texture);
	}
}
