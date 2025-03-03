package paulevs.bnb.command;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.BiomeSource;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.api.util.math.MathHelper;
import paulevs.bnb.world.biome.BNBBiomes;

import java.util.Arrays;

public class BNBBiomeCommand extends BNBCommand {
	private static final Direction[] DIRECTIONS = new Direction[] {
		Direction.NORTH, Direction.EAST, Direction.SOUTH, Direction.WEST
	};
	
	private String[] biomeNames;
	
	protected BNBBiomeCommand() {
		super("biome");
	}
	
	@Override
	void execute(Object commandSource, String[] args) {
		if (!checkPlayer(commandSource)) return;
		
		if (args.length != 1) {
			showUsage(commandSource);
			return;
		}
		
		PlayerEntity player = (PlayerEntity) commandSource;
		BiomeSource source = player.level.getBiomeSource();
		final String biomeName = args[0];
		final int radius = 128;
		int cx = player.chunkX;
		int cz = player.chunkZ;
		byte dirIndex = 0;
		short steps = 0;
		
		for (short i = 0; steps <= radius; i++) {
			steps = (short) ((i >> 1) + 1);
			Direction dir = DIRECTIONS[dirIndex];
			for (short j = 0; j < steps; j++) {
				cx += dir.getOffsetX();
				cz += dir.getOffsetZ();
				int wx = cx << 4 | 8;
				int wz = cz << 4 | 8;
				Biome biome = source.getBiome(wx, wz);
				if (biomeName.equals(biome.name)) {
					cx = wx - (int) player.x;
					cz = wz - (int) player.z;
					int distance = (int) MathHelper.sqrt(cx * cx + cz * cz);
					sendMessage(player, String.format(
						"Nearest biome location at: §a%d %d§r (§b%d§r blocks away)",
						wx, wz, distance
					));
					return;
				}
			}
			dirIndex = (byte) ((dirIndex + 1) & 3);
		}
		
		sendMessage(player, "No biome found nearby");
	}
	
	@Override
	String getUsage() {
		return "biome <biome_name>";
	}
	
	@Override
	String getDescription() {
		return "find the closest biome location";
	}
	
	@Override
	public String[] getArgumentSuggestions(int index, String input) {
		if (biomeNames == null) {
			biomeNames = new String[BNBBiomes.BIOMES.size()];
			for (int i = 0; i < BNBBiomes.BIOMES.size(); i++) {
				biomeNames[i] = BNBBiomes.BIOMES.get(i).name;
			}
			Arrays.sort(biomeNames);
		}
		return getPossibleVariants(input, biomeNames);
	}
}
