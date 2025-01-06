package paulevs.bnb.command;

import com.matthewperiut.retrocommands.api.Command;
import com.matthewperiut.retrocommands.api.CommandRegistry;
import com.matthewperiut.retrocommands.util.SharedCommandSource;

public class RetroCommandsIntegration {
	protected static void registerRetroCommands() {
		CommandRegistry.add(new Command() {
			private static final String[] COMMAND_NAMES;
			
			static {
				COMMAND_NAMES = new String[BNBCommandManager.COMMAND_LIST.size()];
				for (int i = 0; i < BNBCommandManager.COMMAND_LIST.size(); i++) {
					COMMAND_NAMES[i] = BNBCommandManager.COMMAND_LIST.get(i).name;
				}
			}
			
			@Override
			public void command(SharedCommandSource commandSource, String[] parameters) {
				BNBCommand bnbCommand = parameters.length < 2 ? BNBCommandManager.COMMANDS.get("help") : BNBCommandManager.COMMANDS.get(parameters[1]);
				if (bnbCommand == null) {
					commandSource.sendFeedback("Unknown command, use /bnb help");
					commandSource.sendFeedback("to get list of available commands");
					return;
				}
				bnbCommand.execute(commandSource.getPlayer(), BNBCommandManager.cutArgs(parameters));
			}
			
			@Override
			public String name() {
				return "bnb";
			}
			
			@Override
			public void manual(SharedCommandSource commandSource) {}
			
			@Override
			public String[] suggestion(SharedCommandSource source, int parameterNum, String currentInput, String totalInput) {
				if (parameterNum == 1) {
					return BNBCommand.getPossibleVariants(currentInput, COMMAND_NAMES);
				}
				BNBCommand command = BNBCommandManager.COMMANDS.get(totalInput.split(" ")[1]);
				if (command == null) return BNBCommand.EMPTY;
				return command.getArgumentSuggestions(parameterNum - 2, currentInput);
			}
		});
	}
}
