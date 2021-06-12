package paulevs.bnb.mixin.common;

import java.util.List;
import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.Living;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.ListTag;
import paulevs.bnb.effects.AdditionalHealthEffect;
import paulevs.bnb.effects.StatusEffect;
import paulevs.bnb.effects.StatusEffects;
import paulevs.bnb.interfaces.StatusEffectable;

@Mixin(PlayerBase.class)
public class PlayerBaseMixin extends Living implements StatusEffectable {
	private Map<String, StatusEffect> bnb_effects = Maps.newHashMap();
	private List<String> bnb_toRemove = Lists.newArrayList();
	
	public PlayerBaseMixin(Level arg) {
		super(arg);
	}

	@Override
	public void addEffect(StatusEffect effect) {
		bnb_effects.put(effect.getName(), effect);
	}

	@Override
	public StatusEffect getEffect(String name) {
		return bnb_effects.get(name);
	}

	@Override
	public void removeEffect(String name) {
		bnb_effects.remove(name);
	}
	
	@Inject(method = "tick", at = @At("TAIL"))
	private void bnb_tickPlayer(CallbackInfo info) {
		bnb_effects.values().forEach(effect -> effect.tick((PlayerBase) (Object) this, bnb_toRemove));
		bnb_toRemove.forEach(name -> bnb_effects.remove(name));
		bnb_toRemove.clear();
	}
	
	@Inject(method = "addHealth", at = @At("HEAD"), cancellable = true)
	private void bnb_addHealth(int amount, CallbackInfo info) {
		if (bnb_effects.containsKey("additional_health")) {
			this.health += amount;
			if (this.health > 20) {
				((AdditionalHealthEffect) bnb_effects.get("additional_health")).addHealth(this.health - 20);
				this.health = 20;
			}
			this.field_1613 = this.field_1009 / 2;
			info.cancel();
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void bnb_writePlayerCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		ListTag tags = new ListTag();
		bnb_effects.values().forEach(effect -> {
			tags.add(effect.toTag());
		});
		tag.put("statusEffects", tags);
	}

	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void bnb_readPlayerCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		ListTag tags = tag.getListTag("statusEffects");
		for (int i = 0; i < tags.size(); i++) {
			StatusEffect effect = StatusEffects.getEffect((CompoundTag) tags.get(i));
			if (effect != null) {
				addEffect(effect);
			}
		}
	}
	
	@Inject(method = "applyDamage", at = @At("TAIL"))
	private void bnb_applyPlayerDamage(int damageAmount, CallbackInfo info) {
		if (this.health <= 0) {
			bnb_effects.clear();
		}
	}
}
