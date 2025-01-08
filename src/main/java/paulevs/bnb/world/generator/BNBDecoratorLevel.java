package paulevs.bnb.world.generator;

import it.unimi.dsi.fastutil.longs.Long2ReferenceMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.block.SandBlock;
import net.minecraft.level.Level;
import net.minecraft.level.LightType;
import net.minecraft.level.LightUpdateArea;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.level.source.LevelSource;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.impl.world.chunk.ChunkSection;
import net.modificationstation.stationapi.impl.world.chunk.FlattenedChunk;
import net.modificationstation.stationapi.impl.worldgen.WorldDecoratorImpl;
import paulevs.bnb.BNB;
import paulevs.bnb.math.ConcurrentLongQueue;
import paulevs.bnb.mixin.common.LevelAccessor;

import java.util.ArrayList;
import java.util.List;

public class BNBDecoratorLevel extends Level {
	private final Long2ReferenceMap<FlattenedChunk> chunks = new Long2ReferenceOpenHashMap<>();
	private final ConcurrentLongQueue areasToUpdate = new ConcurrentLongQueue();
	private final List<LightUpdateArea> lightUpdates = new ArrayList<>();
	private final LevelSource source;
	private final Level level;
	
	
	public BNBDecoratorLevel(Level source) {
		super(
			((LevelAccessor) source).bnb_getDimData(),
			source.getProperties().getName(),
			source.getSeed(),
			source.dimension
		);
		this.source = source.getCache();
		this.level = source;
	}
	
	@Override
	public Chunk getChunkFromCache(int x, int z) {
		long index = pack(x, z);
		FlattenedChunk chunk = chunks.get(index);
		if (chunk == null) {
			synchronized (source) {
				if (source.isChunkLoaded(x, z)) {
					chunk = copyFromSource((FlattenedChunk) source.getChunk(x, z));
					chunks.put(index, chunk);
				}
			}
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
	
	private void CopySection(ChunkSection source, ChunkSection target) {
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
	
	private void CopySections(FlattenedChunk source, FlattenedChunk target) {
		for (int i = 0; i < source.sections.length; i++) {
			if (source.sections[i] == null || source.sections[i] == target.sections[i]) continue;
			if (target.sections[i] == null) {
				target.sections[i] = new ChunkSection(i);
			}
			CopySection(source.sections[i], target.sections[i]);
		}
	}
	
	private FlattenedChunk copyFromSource(FlattenedChunk source) {
		FlattenedChunk target = new FlattenedChunk(this, source.x, source.z);
		System.arraycopy(source.sections, 0, target.sections, 0, source.sections.length);
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
				System.out.println("Loading chunk");
			}
		}
		CopySections(source, target);
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
		if (status != BNBChunkStatus.TERRAIN) return false;
		
		boolean decorate = true;
		for (byte i = 1; i < 4; i++) {
			byte dx = (byte) (i & 1);
			byte dz = (byte) ((i >> 1) & 1);
			status = getChunkStatus(x + dx, z + dz);
			if (status == BNBChunkStatus.EMPTY) {
				decorate = false;
				break;
			}
		}
		if (!decorate) return false;
		
		boolean sand = SandBlock.fallInstantly;
		WorldDecoratorImpl.decorate(this, x, z);
		SandBlock.fallInstantly = sand;
		
		for (int i = 0; i < lightUpdates.size(); i++) {
			lightUpdates.get(i).process(this);
			lightUpdates.remove(i--);
		}
		
		BNBWorldChunk.cast(getChunkFromCache(x, z)).bnb_setStatus(BNBChunkStatus.FINISHED);
		
		for (FlattenedChunk chunk : chunks.values()) {
			copyBack(chunk);
			areasToUpdate.add(pack(chunk.x, chunk.z));
		}
		
		chunks.clear();
		
		if (FabricLoader.getInstance().isDevelopmentEnvironment()) {
			BNB.LOGGER.info("Decorated " + x + " " + z);
		}
		return true;
	}
	
	public void copyBack() {
		for (int i = 0; i < 8 && !areasToUpdate.isEmpty(); i++) {
			long index = areasToUpdate.get();
			int wx = (int) (index >> 32) << 4;
			int wz = (int) (index) << 4;
			level.updateArea(wx | 8, getBottomY(), wz | 8, wx | 8, getTopY(), wz | 8);
		}
	}
	
	private static long pack(int x, int z) {
		return (long) x << 32L | (long) z & 0xFFFFFFFFL;
	}
}
