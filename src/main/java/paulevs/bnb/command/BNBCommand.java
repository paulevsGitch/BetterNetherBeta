package paulevs.bnb.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.command.CommandSource;
import paulevs.bnb.BNB;

import java.util.ArrayList;
import java.util.List;

public abstract class BNBCommand {
	private static final List<String> PRE_LIST = new ArrayList<>();
	protected static final String[] EMPTY = new String[0];
	protected final String name;
	
	protected BNBCommand(String name) {
		this.name = name;
	}
	
	abstract void execute(Object commandSource, String[] args);
	abstract String getUsage();
	abstract String getDescription();
	
	protected static boolean checkPlayer(Object commandSource) {
		if (commandSource instanceof PlayerEntity) return true;
		BNB.LOGGER.warn("This command should be executed only by player");
		return false;
	}
	
	protected void showUsage(Object commandSource) {
		sendMessage(commandSource, "Usage: /bnb " + getUsage());
	}
	
	protected String[] getArgumentSuggestions(int index, String input) {
		return EMPTY;
	}
	
	protected static void sendMessage(Object commandSource, String message) {
		if (commandSource instanceof PlayerEntity player) {
			if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
				sendMessageClient(player, message);
			}
			else sendMessageServer(player, message);
		}
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			sendMessageServer(commandSource, message);
		}
	}
	
	@Environment(EnvType.CLIENT)
	private static void sendMessageClient(PlayerEntity player, String message) {
		String[] parts = message.split("\n");
		for (String part : parts) {
			player.sendMessage(part);
		}
	}
	
	@Environment(EnvType.SERVER)
	private static void sendMessageServer(Object commandSource, String message) {
		if (commandSource instanceof CommandSource source) {
			source.sendFeedback(message);
		}
	}
	
	protected static String[] getPossibleVariants(String input, String[] names) {
		if (input.isEmpty()) return names;
		for (String name : names) {
			if (!name.startsWith(input)) continue;
			PRE_LIST.add(name.substring(input.length()));
		}
		String[] result = PRE_LIST.toArray(String[]::new);
		PRE_LIST.clear();
		return result;
	}
}
