package paulevs.bnb.weather;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.packet.BNBWeatherPacket;

import java.util.Random;

public class BNBWeatherManager {
	private static final WeatherType[] WEATHER_ORDER = WeatherType.values();
	private static final LongSet CHUNKS = new LongOpenHashSet(4096);
	private static final Random RANDOM = new Random();
	
	private static WeatherType currentWeather;
	private static int weatherLength;
	private static int weatherIndex;
	
	public static void tick(Level level) {
		updateWeather();
		processBlocks(level);
	}
	
	private static void updateWeather() {
		if (weatherLength-- > 0) return;
		currentWeather = WEATHER_ORDER[weatherIndex];
		if (++weatherIndex == WEATHER_ORDER.length) {
			for (int i = 0; i < WEATHER_ORDER.length; i++) {
				int j = RANDOM.nextInt(WEATHER_ORDER.length);
				WeatherType t = WEATHER_ORDER[j];
				WEATHER_ORDER[j] = WEATHER_ORDER[i];
				WEATHER_ORDER[i] = t;
			}
			if (WEATHER_ORDER[0] == currentWeather) {
				int j = WEATHER_ORDER.length - 1;
				WeatherType t = WEATHER_ORDER[j];
				WEATHER_ORDER[j] = WEATHER_ORDER[0];
				WEATHER_ORDER[0] = t;
			}
			weatherIndex = 0;
		}
		weatherLength = currentWeather.getTime(RANDOM);
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BNB.LOGGER.info("Weather " + currentWeather + " for " + weatherLength + " ticks");
		}
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PacketHelper.send(new BNBWeatherPacket(currentWeather));
		}
	}
	
	private static void processBlocks(Level level) {
		if (currentWeather != WeatherType.LAVA_RAIN) return;
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			PlayerEntity player = BNBClient.getMinecraft().player;
			for (int x = -7; x <= 7; x++) {
				int px = player.chunkX + x;
				for (int z = -7; z <= 7; z++) {
					int pz = player.chunkZ + z;
					if (level.getChunkFromCache(px, pz) != null) {
						CHUNKS.add(((long) px & 0xFFFFFFFFL) << 32L | (long) pz & 0xFFFFFFFFL);
					}
				}
			}
		}
		else {
			for (Object obj : level.players) {
				PlayerEntity player = (PlayerEntity) obj;
				for (int x = -7; x <= 7; x++) {
					int px = player.chunkX + x;
					for (int z = -7; z <= 7; z++) {
						int pz = player.chunkZ + z;
						if (level.getChunkFromCache(px, pz) != null) {
							CHUNKS.add(((long) px & 0xFFFFFFFFL) << 32L | (long) pz & 0xFFFFFFFFL);
						}
					}
				}
			}
		}
		
		for (long pos : CHUNKS) {
			int x = (int) (pos >> 32);
			int z = (int) (pos & 0xFFFFFFFFL);
			Chunk chunk = level.getChunkFromCache(x, z);
			for (int i = 0; i < 256; i++) {
				if (RANDOM.nextInt(2000) > 0) continue;
				int px = (x << 4) | (i & 15);
				int pz = (z << 4) | (i >> 4);
				int py = getWeatherBottom(level, chunk, px & 15, pz & 15);
				BlockState state = chunk.getBlockState(px & 15, py, pz & 15);
				if (state.getMaterial().isBurnable() && chunk.getBlockState(px & 15, py + 1, pz & 15).isAir()) {
					chunk.setBlockState(px & 15, py + 1, pz & 15, Block.FIRE.getDefaultState());
				}
			}
		}
		
		CHUNKS.clear();
	}
	
	public static WeatherType getCurrentWeather() {
		return currentWeather;
	}
	
	@Environment(EnvType.CLIENT)
	public static void setWeather(WeatherType weather) {
		currentWeather = weather;
	}
	
	public static int getWeatherTop(Level level, int x, int z) {
		int y = level.getTopY() - 1;
		int minY = level.getBottomY();
		BlockState state = level.getBlockState(x, y, z);
		while (!state.isAir() && y > minY) state = level.getBlockState(x, --y, z);
		return y;
	}
	
	public static int getWeatherBottom(Level level, int x, int y, int z) {
		int minY = level.getBottomY();
		BlockState state = level.getBlockState(x, y, z);
		while (state.isAir() && y > minY) state = level.getBlockState(x, --y, z);
		return y;
	}
	
	private static int getWeatherBottom(Level level, Chunk chunk, int x, int z) {
		int y = level.getTopY() - 1;
		int minY = level.getBottomY();
		BlockState state = chunk.getBlockState(x, y, z);
		while (!state.isAir() && y > minY) state = chunk.getBlockState(x, --y, z);
		while (state.isAir() && y > minY) state = chunk.getBlockState(x, --y, z);
		return y;
	}
}
