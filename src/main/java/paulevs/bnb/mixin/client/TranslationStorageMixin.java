package paulevs.bnb.mixin.client;

import java.util.Map;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import com.google.common.collect.Maps;

import net.minecraft.client.resource.language.TranslationStorage;
import paulevs.bnb.BetterNetherBeta;

@Mixin(TranslationStorage.class)
public class TranslationStorageMixin {
	private static final Map<String, String> TRANSLATIONS = Maps.newHashMap();
	private static final String BLOCK_PATTERN = "tile." + BetterNetherBeta.MOD_ID + ":";
	private static final String ITEM_PATTERN = "item." + BetterNetherBeta.MOD_ID + ":";
	
	@Inject(method = "method_995", at = @At("HEAD"), cancellable = true)
	private void bnb_getTranslatedName(String key, CallbackInfoReturnable<String> info) {
		String result = TRANSLATIONS.get(key);
		if (result != null) {
			info.setReturnValue(result);
			info.cancel();
		}
		else if (key.startsWith(BLOCK_PATTERN)) {
			result = bnb_transformName(key, BLOCK_PATTERN);
			info.setReturnValue(result);
			info.cancel();
		}
		else if (key.startsWith(ITEM_PATTERN)) {
			result = bnb_transformName(key, ITEM_PATTERN);
			info.setReturnValue(result);
			info.cancel();
		}
	}
	
	private String bnb_transformName(String key, String pattern) {
		String result = bnb_capitalize(key.substring(pattern.length()).replace('_', ' ')).replace("Stairs", "Stairs From");
		TRANSLATIONS.put(key, result);
		return result;
	}
	
	private String bnb_capitalize(String value) {
		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < value.length(); i++) {
			char ch = value.charAt(i);
			if (i == 0 || (Character.isAlphabetic(ch) && Character.isWhitespace(value.charAt(i - 1)))) {
				builder.append(Character.toUpperCase(ch));
			}
			else {
				builder.append(ch);
			}
		}
		return builder.toString();
	}
}
