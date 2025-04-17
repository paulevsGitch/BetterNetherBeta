package paulevs.bnb.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import paulevs.bnb.packet.BNBWeatherPacket;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

import java.util.Arrays;

public class BNBWeatherCommand extends BNBCommand {
	private static final String[] WEATHER_NAMES = Arrays
		.stream(WeatherType.values())
		.map(type -> type.name)
		.toArray(String[]::new);
	
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
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) updateOnServer(type);
		BNBWeatherManager.setWeather(type, weatherLength);
		
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
	
	@Override
	protected String[] getArgumentSuggestions(int index, String input) {
		return getPossibleVariants(input, WEATHER_NAMES);
	}
	
	@Environment(EnvType.SERVER)
	private void updateOnServer(WeatherType type) {
		@SuppressWarnings("deprecation")
		MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
		for (Object playerObj : server.serverPlayerConnectionManager.players) {
			ServerPlayer player = (ServerPlayer) playerObj;
			if (player.dimensionId != -1) continue;
			PacketHelper.sendTo(player, new BNBWeatherPacket(type));
		}
	}
	
	static {
		Arrays.sort(WEATHER_NAMES);
	}
}
