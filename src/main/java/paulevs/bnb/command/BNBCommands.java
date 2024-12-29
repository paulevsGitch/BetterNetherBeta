package paulevs.bnb.command;

import com.matthewperiut.retrocommands.api.CommandRegistry;
import com.matthewperiut.retrocommands.command.Help;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.BiomeSource;
import net.minecraft.server.command.Command;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.BNB;

import java.util.Arrays;
import java.util.Map;

public class BNBCommands {
	private static final Map<String, BNBCommand> COMMANDS = new Object2ObjectOpenHashMap<>();
	private static boolean hasRetroCommands;
	
	private static final String AVAILABLE_COMMANDS = String.join("\n",
		"  /bnb help: shows command list with help",
		"  /bnb biome <biome_name>: find a closest biome location"
	);
	
	private static void init() {
		hasRetroCommands = FabricLoader.getInstance().isModLoaded("retrocommands");
		COMMANDS.put("bnb", (source, args) -> {
			if (!(source instanceof PlayerEntity player)) {
				BNB.LOGGER.warn("This command should be executed only by player");
				return;
			}
			
			if (args.length == 0) args = new String[] { "help" };
			
			switch (args[0]) {
				case "help":
					sendMessage(player, "List of available commands:\n" + AVAILABLE_COMMANDS);
					return;
				case "biome":
					if (args.length != 2) sendMessage(player, "Usage: /bnb biome <biome_name>");
					else locateBiome(player, args[1]);
					return;
			}
			
			sendMessage(player, "Unknown command, list of available commands:\n" + AVAILABLE_COMMANDS);
		});
	}
	
	private static void registerRetroCommands() {
		if (!hasRetroCommands) return;
		COMMANDS.forEach((name, command) ->
			CommandRegistry.add(new RetroCommandWrapper(name, command))
		);
		Help.addHelpTip("/bnb help", false);
		Help.addHelpTip("/bnb biome", false);
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
		BNBCommand bnbCommand = COMMANDS.get(parts[0]);
		if (bnbCommand == null) return false;
		bnbCommand.execute(command.source, Arrays.copyOfRange(parts, 1, parts.length));
		return true;
	}
	
	private static void locateBiome(PlayerEntity player, String name) {
		BiomeSource source = player.level.getBiomeSource();
		Direction[] directions = new Direction[] { Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST };
		final int radius = 64;
		int cx = player.chunkX;
		int cz = player.chunkZ;
		byte dirIndex = 0;
		short steps = 0;
		
		for (short i = 0; steps <= radius; i++) {
			steps = (short) ((i >> 1) + 1);
			Direction dir = directions[dirIndex];
			for (short j = 0; j < steps; j++) {
				cx += dir.getOffsetX();
				cz += dir.getOffsetZ();
				int wx = cx << 4 | 8;
				int wz = cz << 4 | 8;
				Biome biome = source.getBiome(wx, wz);
				if (name.equals(biome.name)) {
					cx = wx - player.chunkX;
					cz = wz - player.chunkZ;
					int distance = (int) MathHelper.sqrt(cx * cx + cz * cz);
					sendMessage(player, String.format(
						"Nearest biome location at: %d %d (%d blocks away)",
						wx, wz, distance
					));
					return;
				}
			}
			dirIndex = (byte) ((dirIndex + 1) & 3);
		}
		
		sendMessage(player, "No biome found nearby");
	}
	
	private static void sendMessage(PlayerEntity player, String message) {
		String[] parts = message.split("\n");
		for (String part : parts) {
			player.sendMessage(part);
		}
	}
}
