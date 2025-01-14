package paulevs.bnb.weather;

import it.unimi.dsi.fastutil.longs.LongOpenHashSet;
import it.unimi.dsi.fastutil.longs.LongSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.mixin.common.EntityAccessor;
import paulevs.bnb.packet.BNBWeatherPacket;

import java.util.List;
import java.util.Random;

public class BNBWeatherManager {
	private static final int MAX_WEATHER_SEARCH = 255 - 32;
	private static final WeatherType[] WEATHER_SEQUENCE = new WeatherType[16];
	private static final LongSet CHUNKS = new LongOpenHashSet(4096);
	private static final Random RANDOM = new Random();
	
	private static WeatherType currentWeather = WeatherType.CLEAR;
	private static int weatherLength = 1200;
	private static int weatherIndex;
	
	public static void tick(Level level) {
		updateWeather();
		processBlocksAndEntities(level);
	}
	
	private static void updateWeather() {
		if (weatherLength-- > 0) return;
		currentWeather = WEATHER_SEQUENCE[weatherIndex];
		if (currentWeather == null) {
			currentWeather = WeatherType.CLEAR;
			fillSequence();
		}
		if (++weatherIndex == WEATHER_SEQUENCE.length) {
			fillSequence();
		}
		weatherLength = currentWeather.getTime(RANDOM);
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BNB.LOGGER.info("Weather '" + currentWeather.name + "' for " + weatherLength + " ticks");
		}
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.SERVER) {
			PacketHelper.send(new BNBWeatherPacket(currentWeather));
		}
	}
	
	private static WeatherType getWeather(WeatherType prev1, WeatherType prev2) {
		if (prev1 == null) return WeatherType.CLEAR;
		return switch (prev1) {
			case CLEAR -> RANDOM.nextInt(5) == 0 ? WeatherType.DRIZZLE : WeatherType.FOG;
			case FOG -> RANDOM.nextInt(3) == 0 ? WeatherType.DRIZZLE : WeatherType.CLEAR;
			case DRIZZLE -> prev2 == WeatherType.RAIN ? WeatherType.CLEAR : WeatherType.RAIN;
			case RAIN -> WeatherType.DRIZZLE;
		};
	}
	
	private static void fillSequence() {
		WeatherType prev2 = WEATHER_SEQUENCE[WEATHER_SEQUENCE.length - 2];
		WeatherType prev1 = currentWeather;
		for (int i = 0; i < WEATHER_SEQUENCE.length; i++) {
			WEATHER_SEQUENCE[i] = getWeather(prev1, prev2);
			prev2 = prev1;
			prev1 = WEATHER_SEQUENCE[i];
		}
		weatherIndex = 0;
	}
	
	private static void processBlocksAndEntities(Level level) {
		if (currentWeather != WeatherType.RAIN) return;
		
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			updateOnClient(level);
		}
		else {
			updateOnServer(level);
		}
		
		for (long pos : CHUNKS) {
			int x = (int) (pos >> 32);
			int z = (int) (pos & 0xFFFFFFFFL);
			Chunk chunk = level.getChunkFromCache(x, z);
			
			for (short i = 0; i < 256; i++) {
				if (RANDOM.nextInt(2000) > 0) continue;
				x = i & 15;
				z = i >> 4;
				int y = getWeatherBottom(chunk, x, z);
				if (y == Short.MAX_VALUE) continue;
				BlockState state = chunk.getBlockState(x, y, z);
				if (state.getMaterial().isBurnable() && chunk.getBlockState(x, y + 1, z).isAir()) {
					chunk.setBlockState(x, y + 1, z, Block.FIRE.getDefaultState());
				}
			}
			
			for (Object section : chunk.entities) {
				for (Object preEntity : (List<?>) section) {
					EntityAccessor accessor = (EntityAccessor) preEntity;
					if (accessor.bnb_immuneToFire()) continue;
					Entity entity = (Entity) preEntity;
					if ((entity.ticks & 7) > 0) continue;
					if (entity.fire > 8) continue;
					if (preEntity instanceof PlayerEntity player) {
						if (BNB.isCreative(player)) continue;
					}
					x = MCMath.floor(entity.x) & 15;
					z = MCMath.floor(entity.z) & 15;
					int y = getWeatherBottom(chunk, x, z) + 2;
					if (y > entity.y + entity.height) continue;
					accessor.bnb_setOnFire();
				}
			}
		}
		
		CHUNKS.clear();
	}
	
	@Environment(EnvType.CLIENT)
	private static void updateOnClient(Level level) {
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
	
	@Environment(EnvType.SERVER)
	private static void updateOnServer(Level level) {
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
	
	public static WeatherType getCurrentWeather() {
		return currentWeather;
	}
	
	public static int getWeatherLength() {
		return weatherLength;
	}
	
	@Environment(EnvType.CLIENT)
	public static void setWeather(WeatherType weather) {
		currentWeather = weather;
	}
	
	public static void setWeather(WeatherType weather, int length) {
		if (currentWeather != weather || WEATHER_SEQUENCE[0] == null) fillSequence();
		currentWeather = weather;
		weatherLength = length;
	}
	
	public static short getWeatherTop(Chunk chunk, int x, int z) {
		int y = 255;
		BlockState state = chunk.getBlockState(x, y, z);
		while (isNetherCeiling(state) && y > MAX_WEATHER_SEARCH) state = chunk.getBlockState(x, --y, z);
		return !canPropagateWeather(state) ? Short.MAX_VALUE : (short) y;
	}
	
	public static short getWeatherBottom(Chunk chunk, int x, int z) {
		int y = getWeatherTop(chunk, x, z);
		if (y == Short.MAX_VALUE) return Short.MAX_VALUE;
		return getWeatherBottom(chunk, x, y, z);
	}
	
	public static short getWeatherBottom(Chunk chunk, int x, int y, int z) {
		BlockState state = chunk.getBlockState(x, y, z);
		while (canPropagateWeather(state) && y > 0) {
			state = chunk.getBlockState(x, --y, z);
		}
		return (short) y;
	}
	
	public static int getCurrentWeatherLength() {
		return weatherLength;
	}
	
	private static boolean isNetherCeiling(BlockState state) {
		return state.isOf(Block.BEDROCK) ||
			state.isIn(BNBBlockTags.NETHERRACK_TERRAIN) ||
			state.isIn(BNBBlockTags.SOUL_TERRAIN);
	}
	
	private static boolean canPropagateWeather(BlockState state) {
		if (state.isAir()) return true;
		Material material = state.getMaterial();
		if (material.isLiquid()) return false;
		if (material == BNBBlockMaterials.NETHER_PLANT) return true;
		return !material.blocksMovement();
	}
}
