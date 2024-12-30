package paulevs.bnb.command;

import net.minecraft.entity.living.player.PlayerEntity;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

public class BNBWeatherCommand extends BNBCommand {
	protected BNBWeatherCommand() {
		super("weather");
	}
	
	@Override
	void execute(Object commandSource, String[] args) {
		if (args.length < 1) {
			showUsage(commandSource);
			return;
		}
		
		WeatherType type = WeatherType.getByName(args[0]);
		
		if (type == null) {
			sendMessage(commandSource, "Invalid weather type");
			return;
		}
		
		int weatherLength = 10000;
		if (args.length == 2) {
			try {
				weatherLength = Integer.parseInt(args[1], 10);
			}
			catch (NumberFormatException e) {
				sendMessage(commandSource, "Weather length should be an integer number");
				return;
			}
		}
		
		if (commandSource instanceof PlayerEntity player) {
			if (player.level.isRemote) {
				// Send packet to server
			}
			else BNBWeatherManager.setWeather(type, weatherLength);
		}
		else {
			// Send packet from server
		}
		
		sendMessage(commandSource, "Weather set to §a" + type.name + "§r");
	}
	
	@Override
	String getUsage() {
		return "weather <weather_type> §8[length]§r";
	}
	
	@Override
	String getDescription() {
		return "shows list of available commands";
	}
	
	/*@Environment(EnvType.SERVER)
	private void executeOnServer() {
		BNBWeatherManager.setWeather(type, length);
	}*/
}
