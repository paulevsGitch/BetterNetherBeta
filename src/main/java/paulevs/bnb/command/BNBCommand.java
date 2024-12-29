package paulevs.bnb.command;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.server.command.CommandSource;
import paulevs.bnb.BNB;

public abstract class BNBCommand {
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
	
	protected static void sendMessage(Object commandSource, String message) {
		if (commandSource instanceof PlayerEntity player) {
			sendMessageClient(player, message);
		}
		else if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
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
}
