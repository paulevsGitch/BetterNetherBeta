package paulevs.bnb.effects;

import com.google.common.collect.Maps;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.util.io.CompoundTag;
import paulevs.bnb.interfaces.StatusEffectable;

import java.util.Map;
import java.util.function.Supplier;

public class StatusEffects {
	private static final Map<String, Supplier<StatusEffect>> REGISTRY = Maps.newHashMap();
	
	public static StatusEffect getEffect(CompoundTag tag) {
		String name = tag.getString("name");
		Supplier<StatusEffect> builder = REGISTRY.get(name);
		if (builder == null) {
			return null;
		}
		StatusEffect effect = builder.get();
		effect.fromTag(tag);
		return effect;
	}
	
	public static void registerEffect(String name, Supplier<StatusEffect> effect) {
		REGISTRY.put(name, effect);
	}
	
	public static void register() {
		registerEffect(AdditionalHealthEffect.NAME, AdditionalHealthEffect::new);
		registerEffect(SoulProtectionEffect.NAME, SoulProtectionEffect::new);
	}
	
	public static void addEffect(PlayerBase player, String name) {
		Supplier<StatusEffect> builder = REGISTRY.get(name);
		if (builder != null) {
			((StatusEffectable) player).addEffect(builder.get());
		}
	}
}
