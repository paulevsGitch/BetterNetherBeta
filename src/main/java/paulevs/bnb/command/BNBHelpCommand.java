package paulevs.bnb.command;

import java.util.stream.Collectors;

public class BNBHelpCommand extends BNBCommand {
	protected BNBHelpCommand() {
		super("help");
	}
	
	@Override
	void execute(Object commandSource, String[] args) {
		String commands = "§6List of available commands:§r\n";
		commands += BNBCommandManager
			.getCommands()
			.stream()
			.map(command -> "  /bnb " + command.getUsage() + ": " + command.getDescription())
			.collect(Collectors.joining("\n"));
		sendMessage(commandSource, commands);
	}
	
	@Override
	String getUsage() {
		return "help";
	}
	
	@Override
	String getDescription() {
		return "shows list of available commands";
	}
}
