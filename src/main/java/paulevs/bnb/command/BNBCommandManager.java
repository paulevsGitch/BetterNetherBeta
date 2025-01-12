package paulevs.bnb.command;

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
	protected static final Map<String, BNBCommand> COMMANDS = new Object2ObjectOpenHashMap<>();
	protected static final List<BNBCommand> COMMAND_LIST = new ArrayList<>();
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
	
	@Environment(EnvType.CLIENT)
	public static void registerClient() {
		init();
		if (hasRetroCommands) {
			RetroCommandsIntegration.registerRetroCommands();
		}
	}
	
	@Environment(EnvType.SERVER)
	public static void registerServer() {
		init();
		if (hasRetroCommands) {
			RetroCommandsIntegration.registerRetroCommands();
		}
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
	
	protected static String[] cutArgs(String[] args) {
		return args.length < 2 ? new String[0] : Arrays.copyOfRange(args, 2, args.length);
	}
}
