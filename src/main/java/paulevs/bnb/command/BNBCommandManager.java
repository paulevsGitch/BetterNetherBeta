package paulevs.bnb.command;

import com.matthewperiut.retrocommands.api.CommandRegistry;
import com.matthewperiut.retrocommands.util.SharedCommandSource;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.server.command.Command;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BNBCommandManager {
	private static final Map<String, BNBCommand> COMMANDS = new Object2ObjectOpenHashMap<>();
	private static final List<BNBCommand> COMMAND_LIST = new ArrayList<>();
	private static boolean hasRetroCommands;
	
	private static void add(BNBCommand command) {
		COMMANDS.put(command.name, command);
		COMMAND_LIST.add(command);
	}
	
	private static void init() {
		hasRetroCommands = FabricLoader.getInstance().isModLoaded("retrocommands");
		add(new BNBHelpCommand());
		add(new BNBBiomeCommand());
		add(new BNBWeatherCommand());
		COMMAND_LIST.subList(1, COMMAND_LIST.size()).sort(Comparator.comparing(c -> c.name));
	}
	
	private static void registerRetroCommands() {
		if (!hasRetroCommands) return;
		CommandRegistry.add(new com.matthewperiut.retrocommands.api.Command() {
			private static final String[] COMMAND_NAMES;
			
			static {
				COMMAND_NAMES = new String[COMMAND_LIST.size()];
				for (int i = 0; i < COMMAND_LIST.size(); i++) {
					COMMAND_NAMES[i] = COMMAND_LIST.get(i).name;
				}
			}
			
			@Override
			public void command(SharedCommandSource commandSource, String[] parameters) {
				BNBCommand bnbCommand = parameters.length < 2 ? COMMANDS.get("help") : COMMANDS.get(parameters[1]);
				if (bnbCommand == null) {
					commandSource.sendFeedback("Unknown command, use /bnb help");
					commandSource.sendFeedback("to get list of available commands");
					return;
				}
				bnbCommand.execute(commandSource.getPlayer(), cutArgs(parameters));
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
				BNBCommand command = COMMANDS.get(totalInput.split(" ")[1]);
				if (command == null) return BNBCommand.EMPTY;
				return command.getArgumentSuggestions(parameterNum - 2, currentInput);
			}
		});
	}
	
	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		init();
		registerRetroCommands();
	}
	
	@Environment(EnvType.SERVER)
	public static void registerServer() {
		init();
		registerRetroCommands();
	}
	
	@Environment(EnvType.SERVER)
	public static boolean processServer(Command command) {
		if (hasRetroCommands) return false;
		String[] parts = command.commandString.split(" ");
		if (!parts[0].equals("bnb")) return false;
		BNBCommand bnbCommand = COMMANDS.get(parts[1]);
		if (bnbCommand == null) return false;
		bnbCommand.execute(command.source, cutArgs(parts));
		return true;
	}
	
	protected static List<BNBCommand> getCommands() {
		return COMMAND_LIST;
	}
	
	private static String[] cutArgs(String[] args) {
		return args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 2, args.length);
	}
}
