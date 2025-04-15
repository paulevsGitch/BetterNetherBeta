package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screen.container.PlayerScreen;
import net.minecraft.client.resource.language.I18n;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.client.item.CustomTooltipProvider;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.BNBClient;
import paulevs.bnb.rendering.CustomStackTexture;
import paulevs.bnb.util.CompatUtil;
import paulevs.bnb.weather.BNBWeatherManager;

public class NetherHygrometerItem extends TemplateItem implements CustomStackTexture, CustomTooltipProvider {
	private static final String[] TOOLTIP_LONG = new String[4];
	private static final String[] TOOLTIP_SHORT = new String[2];
	public static final int[] TEXTURES = new int[32];
	private static boolean canPredict;
	private static float weatherDelta;
	
	public NetherHygrometerItem(Identifier identifier) {
		super(identifier);
		setMaxStackSize(1);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int getTexture(ItemStack stack) {
		Minecraft minecraft = BNBClient.getMinecraft();
		canPredict = minecraft.level != null && minecraft.level.dimension.id == -1;
		if (canPredict) {
			if (minecraft.currentScreen instanceof PlayerScreen) {
				canPredict = !CompatUtil.isAMIItem(stack);
			}
			else canPredict = false;
		}
		double time = minecraft.level == null ? stack.hashCode() : (minecraft.level.getLevelTime() + stack.hashCode()) * 0.05;
		if (canPredict) {
			weatherDelta = MathHelper.lerp(0.002F, weatherDelta, switch (BNBWeatherManager.getCurrentWeather()) {
				case CLEAR -> 0.0F;
				case FOG -> 0.25F;
				case DRIZZLE -> 0.5F;
				case RAIN -> 0.75F;
			});
		}
		else weatherDelta = 0.0F;
		float preIndex = (float) Math.sin(time) * 0.5F + 0.5F;
		if (canPredict) preIndex = preIndex * 0.25F + weatherDelta;
		int index = Math.round(preIndex * 31.0F);
		return TEXTURES[index];
	}
	
	@Override
	public String[] getTooltip(ItemStack stack, String originalTooltip) {
		if (canPredict) {
			TOOLTIP_LONG[0] = getTranslatedName();
			TOOLTIP_LONG[1] = getCurrentWeather();
			TOOLTIP_LONG[2] = getWeatherRainChance();
			TOOLTIP_LONG[3] = getWeatherLength();
			return TOOLTIP_LONG;
		}
		else {
			TOOLTIP_SHORT[0] = getTranslatedName();
			TOOLTIP_SHORT[1] = I18n.translate("tooltip.bnb.nether_hygrometer.no_weather");
			return TOOLTIP_SHORT;
		}
	}
	
	private static String getCurrentWeather() {
		return I18n.translate(
			"tooltip.bnb.nether_hygrometer.current"
		) + " " + I18n.translate(
			"tooltip.bnb.nether_hygrometer.weather." + BNBWeatherManager.getCurrentWeather().name
		);
	}
	
	private static String getWeatherRainChance() {
		return I18n.translate(switch (BNBWeatherManager.getCurrentWeather()) {
			case CLEAR -> "tooltip.bnb.nether_hygrometer.no_rain";
			case FOG -> "tooltip.bnb.nether_hygrometer.small_chance";
			case DRIZZLE -> "tooltip.bnb.nether_hygrometer.hight_chance";
			case RAIN -> "tooltip.bnb.nether_hygrometer.rain";
		});
	}
	
	private static String getWeatherLength() {
		String result = I18n.translate("tooltip.bnb.nether_hygrometer.change") + " ";
		String message;
		int ticks = BNBWeatherManager.getWeatherLength();
		if (ticks > 72000) message = "tooltip.bnb.nether_hygrometer.time.more_hour";
		else if (ticks > 36000) message = "tooltip.bnb.nether_hygrometer.time.hour";
		else if (ticks > 18000) message = "tooltip.bnb.nether_hygrometer.time.30_min";
		else if (ticks > 12000) message = "tooltip.bnb.nether_hygrometer.time.15_min";
		else if (ticks > 6000) message = "tooltip.bnb.nether_hygrometer.time.10_min";
		else message = "tooltip.bnb.nether_hygrometer.time.soon";
		return result + I18n.translate(message);
	}
}
