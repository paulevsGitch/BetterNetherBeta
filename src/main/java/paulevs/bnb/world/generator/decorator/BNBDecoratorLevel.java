package paulevs.bnb.world.generator.decorator;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.longs.LongArrayList;
import it.unimi.dsi.fastutil.longs.LongList;
import net.minecraft.block.Block;
import net.minecraft.block.SandBlock;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.LightUpdateArea;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.biome.BiomeSource;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.LevelSource;
import net.minecraft.level.structure.Structure;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.util.math.Direction;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.plant.MossCoverBlock;
import paulevs.bnb.mixin.common.LevelAccessor;
import paulevs.bnb.mixin.common.LevelPropertiesAccessor;
import paulevs.bnb.util.ConcurrentLongQueue;
import paulevs.bnb.world.structure.BNBStructureStage;

import java.util.ArrayList;
import java.util.List;

public class BNBDecoratorLevel extends Level {
	private final Long2ReferenceMap<FlattenedChunk> chunks = new Long2ReferenceOpenHashMap<>();
	private final LongList toRemove = new LongArrayList();
	private final ConcurrentLongQueue areasToUpdate = new ConcurrentLongQueue();
	private final List<LightUpdateArea> lightUpdates = new ArrayList<>();
	private final Biome[] biomes = new Biome[256];
	private final FlattenedChunk empty;
	private final LevelSource source;
	private final Level level;
	
	public BNBDecoratorLevel(Level source) {
		super(
			((LevelAccessor) source).bnb_getDimData(),
			((LevelPropertiesAccessor) source.getProperties()).bnb_getLevelName(),
			source.getSeed(),
			source.dimension
		);
		this.source = source.getCache();
		this.level = source;
		empty = new FlattenedChunk(this, 0, 0);
	}
	
	@Override
	protected void updateSpawnPosition() {}
	
	@Override
	public Chunk getChunkFromCache(int x, int z) {
		long index = pack(x, z);
		FlattenedChunk chunk = chunks.get(index);
		if (chunk == null) {
			synchronized (source) {
				if (source.isChunkLoaded(x, z)) chunk = (FlattenedChunk) source.getChunk(x, z);
			}
			if (chunk == null) chunk = empty;
			else chunks.put(index, copyFromSource(chunk));
		}
		return chunk;
	}
	
	@Override
	public boolean isBlockLoaded(int x, int y, int z) {
		return chunks.containsKey(pack(x >> 4, z >> 4));
	}
	
	@Override
	public int getHeight() {
		return level.getHeight();
	}
	
	@Override
	public int getBottomY() {
		return level.getBottomY();
	}
	
	@Override
	public int getTopY() {
		return level.getTopY();
	}
	
	@Override
	public boolean updateLight() {
		int maxCount = 1024;
		while (!lightUpdates.isEmpty()) {
			if (--maxCount <= 0) return true;
			int index = lightUpdates.size() - 1;
			lightUpdates.get(index).process(this);
			lightUpdates.remove(index);
		}
		return false;
	}
	
	@Override
	public void updateLight(LightType type, int x1, int y1, int z1, int x2, int y2, int z2, boolean cascadeUpdate) {
		if (type == LightType.SKY) return;
		
		int centerX = (x2 + x1) >> 1;
		int centerZ = (z2 + z1) >> 1;
		
		if (!isBlockLoaded(centerX, 64, centerZ)) return;
		if (getChunk(centerX, centerZ).isClient()) return;
		
		if (cascadeUpdate) {
			int count = Math.min(5, lightUpdates.size());
			for (int i = 0; i < count; i++) {
				LightUpdateArea area = lightUpdates.get(lightUpdates.size() - i - 1);
				if (area.lightType == type && area.checkAndUpdate(x1, y1, z1, x2, y2, z2)) {
					return;
				}
			}
		}
		
		lightUpdates.add(new LightUpdateArea(type, x1, y1, z1, x2, y2, z2));
	}
	
	private void copySection(ChunkSection source, ChunkSection target) {
		for (short n = 0; n < 4096; n++) {
			byte x = (byte) (n & 15);
			byte y = (byte) ((n >> 4) & 15);
			byte z = (byte) (n >> 8);
			BlockState state = source.getBlockState(x, y, z);
			if (state.isAir()) continue;
			target.setBlockState(x, y, z, state);
			target.setLight(LightType.BLOCK, x, y, z, source.getLight(LightType.BLOCK, x, y, z));
		}
	}
	
	private void copySections(FlattenedChunk source, FlattenedChunk target) {
		for (int i = 0; i < source.sections.length; i++) {
			if (source.sections[i] == null || source.sections[i] == target.sections[i]) continue;
			if (target.sections[i] == null) {
				target.sections[i] = new ChunkSection(i);
			}
			copySection(source.sections[i], target.sections[i]);
		}
	}
	
	private FlattenedChunk copyFromSource(FlattenedChunk source) {
		FlattenedChunk target = new FlattenedChunk(this, source.x, source.z);
		System.arraycopy(source.sections, 0, target.sections, 0, source.sections.length);
		BNBWorldChunk.cast(target).bnb_setStatus(BNBWorldChunk.cast(source).bnb_getStatus());
		target.decorated = true;
		return target;
	}
	
	private void copyBack(FlattenedChunk source) {
		FlattenedChunk target;
		synchronized (this.source) {
			if (this.source.isChunkLoaded(source.x, source.z)) {
				target = (FlattenedChunk) this.source.getChunk(source.x, source.z);
			}
			else {
				target = (FlattenedChunk) this.source.loadChunk(source.x, source.z);
			}
		}
		copySections(source, target);
		BNBWorldChunk bnbWorldChunk = BNBWorldChunk.cast(target);
		BNBChunkStatus sourceStatus = BNBWorldChunk.cast(source).bnb_getStatus();
		BNBChunkStatus targetStatus = bnbWorldChunk.bnb_getStatus();
		bnbWorldChunk.bnb_setStatus(BNBChunkStatus.max(sourceStatus, targetStatus));
		target.needUpdate = true;
	}
	
	public BNBChunkStatus getChunkStatus(int x, int z) {
		BNBChunkStatus status = BNBChunkStatus.EMPTY;
		synchronized (source) {
			if (source.isChunkLoaded(x, z)) {
				status = BNBWorldChunk.cast(source.getChunk(x, z)).bnb_getStatus();
			}
		}
		return status;
	}
	
	public boolean decorate(int x, int z) {
		BNBChunkStatus status = getChunkStatus(x, z);
		if (status == BNBChunkStatus.EMPTY || status == BNBChunkStatus.FINISHED) return false;
		
		FlattenedChunk worldChunk = (FlattenedChunk) getChunkFromCache(x, z);
		if (worldChunk == empty) return false;
		BNBWorldChunk bnbChunk = BNBWorldChunk.cast(worldChunk);
		
		boolean decorate = true;
		for (byte i = 1; i < 4; i++) {
			byte dx = (byte) (i & 1);
			byte dz = (byte) ((i >> 1) & 1);
			BNBChunkStatus sideStatus = getChunkStatus(x + dx, z + dz);
			if (sideStatus.isLessThan(status)) {
				decorate = false;
				break;
			}
		}
		if (!decorate) return false;
		
		decorateWithStatus(x, z, status);
		bnbChunk.bnb_setStatus(status.increment());
		
		for (int i = 0; i < lightUpdates.size(); i++) {
			lightUpdates.get(i).process(this);
			lightUpdates.remove(i--);
		}
		
		for (FlattenedChunk chunk : chunks.values()) {
			copyBack(chunk);
			long index = pack(chunk.x, chunk.z);
			if (BNBWorldChunk.cast(chunk).bnb_getStatus() == BNBChunkStatus.FINISHED) {
				boolean needRemoval = true;
				for (byte i = 1; i < 4; i++) {
					byte dx = (byte) (i & 1);
					byte dz = (byte) ((i >> 1) & 1);
					status = getChunkStatus(chunk.x + dx, chunk.z + dz);
					if (status != BNBChunkStatus.FINISHED) {
						needRemoval = false;
						break;
					}
				}
				if (needRemoval) toRemove.add(index);
			}
		}
		
		for (long index : toRemove) {
			chunks.remove(index);
			areasToUpdate.add(index);
		}
		toRemove.clear();
		
		return true;
	}
	
	public void copyBack() {
		for (int i = 0; i < 8 && !areasToUpdate.isEmpty(); i++) {
			long index = areasToUpdate.get();
			int wx = (int) (index >> 32) << 4 | 8;
			int wz = (int) (index) << 4 | 8;
			level.updateArea(wx, getBottomY(), wz, wx + 16, getTopY(), wz + 16);
		}
	}
	
	private static long pack(int x, int z) {
		return (long) x << 32L | (long) z & 0xFFFFFFFFL;
	}
	
	private void decorateWithStatus(int cx, int cz, BNBChunkStatus status) {
		boolean fallInstantly = SandBlock.fallInstantly;
		SandBlock.fallInstantly = false;
		
		int x1 = cx << 4 | 8;
		int z1 = cz << 4 | 8;
		
		Biome biome;
		
		if (status == BNBChunkStatus.TERRAIN) {
			getBiomeSource().getBiomes(biomes, x1, z1, 16, 16);
			
			int x2 = x1 + 16;
			int z2 = z1 + 16;
			
			surfaceRules(x1, z1, x2, z2);
			additionalDecoration(x1, z1, x2, z2);
			
			biome = biomes[136];
		}
		else {
			biome = getBiomeSource().getBiomes(biomes, x1 + 8, z1 + 8, 1, 1)[0];
		}
		
		placeStructures(biome, cx, cz, status, x1, z1);
		
		SandBlock.fallInstantly = fallInstantly;
	}
	
	private void surfaceRules(int x1, int z1, int x2, int z2) {
		int index = 0;
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				Biome biome = biomes[index++];
				int minY = getBottomY();
				int maxY = dimension.noSkyLight ? getTopY() : getHeight(x, z);
				for (int y = minY; y < maxY; y++) {
					BlockState state = getBlockState(x, y, z);
					biome.applySurfaceRules(this, x, y, z, state);
				}
			}
		}
	}
	
	private void additionalDecoration(int x1, int z1, int x2, int z2) {
		final BlockState netherrack = Block.NETHERRACK.getDefaultState();
		final BlockState mossyNetherrack = BNBBlocks.MOSSY_NETHERRACK.getDefaultState();
		
		for (int x = x1; x < x2; x++) {
			for (int z = z1; z < z2; z++) {
				Chunk chunk = getChunkFromCache(x >> 4, z >> 4);
				int cx = x & 15;
				int cz = z & 15;
				for (int y = 94; y < 256; y++) {
					BlockState state = chunk.getBlockState(cx, y, cz);
					if (state.isOf(BNBBlocks.NETHERRACK_MYCORRUM)) {
						fillCube(x, y, z, netherrack, mossyNetherrack, BNBBlocks.NETHER_MOSS_COVER);
					}
				}
			}
		}
	}
	
	private void fillCube(int x, int y, int z, BlockState filter, BlockState fill, MossCoverBlock moss) {
		boolean skipMoss = random.nextInt(32) > 0;
		
		if (!skipMoss) {
			BiomeSource source = getBiomeSource();
			Biome center = source.getBiome(x, z);
			boolean isSame = true;
			for (byte i = 0; i < 4; i++) {
				Direction dir = Direction.fromHorizontal(i);
				Biome side = source.getBiome(x + (dir.getOffsetX() << 2), z + (dir.getOffsetZ() << 2));
				if (side != center) {
					isSame = false;
					break;
				}
			}
			skipMoss = isSame;
		}
		
		for (byte dx = -2; dx <= 2; dx++) {
			int wx = x + dx;
			byte cx = (byte) (wx & 15);
			for (byte dz = -2; dz <= 2; dz++) {
				int wz = z + dz;
				byte cz = (byte) (wz & 15);
				Chunk chunk2 = getChunkFromCache(wx >> 4, wz >> 4);
				for (int dy = -1; dy <= 1; dy++) {
					int cy = y + dy;
					BlockState above = chunk2.getBlockState(cx, cy + 1, cz);
					if (!above.isAir() && above.isOpaque()) continue;
					
					if (chunk2.getBlockState(cx, cy, cz) == filter) {
						chunk2.setBlockState(cx, cy, cz, fill);
					}
					
					if (skipMoss) continue;
					
					for (byte i = 0; i < 6; i++) {
						if (random.nextInt(3) > 0) continue;
						Direction dir = Direction.byId(i);
						int px = wx + dir.getOffsetX();
						int py = cy + dir.getOffsetY();
						int pz = wz + dir.getOffsetZ();
						if (!getBlockState(px, py, pz).isAir()) continue;
						BlockState state = moss.getStructureState(this, px, py, pz);
						if (state != null) {
							setBlockState(px, py, pz, state);
						}
					}
				}
			}
		}
	}
	
	private void placeStructures(Biome biome, int cx, int cz, BNBChunkStatus status, int x1, int z1) {
		List<Structure> structures = biome.getFeatures();
		if (structures.isEmpty()) return;
		
		random.setSeed(getSeed());
		long dx = (random.nextLong() >> 1) << 1 | 1;
		long dy = (random.nextLong() >> 1) << 1 | 1;
		random.setSeed((long) cx * dx + (long) cz * dy ^ getSeed());
		
		for (Structure structure : structures) {
			BNBChunkStatus targetStatus = BNBChunkStatus.TERRAIN;
			if (structure instanceof BNBStructureStage bnbStructureStage) {
				targetStatus = bnbStructureStage.bnb_getTargetStatus();
			}
			if (targetStatus != status) continue;
			structure.generate(this, random, x1, 0, z1);
		}
	}
}
