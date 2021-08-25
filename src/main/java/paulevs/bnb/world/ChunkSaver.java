package paulevs.bnb.world;

import net.minecraft.level.biome.Biome;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;

public class ChunkSaver extends Thread {
	private final List<Biome> picker;
	private final BiomeChunk chunk;
	private final File file;
	
	public ChunkSaver(BiomeChunk chunk, File file, List<Biome> picker) {
		this.picker = picker;
		this.chunk = chunk;
		this.file = file;
	}
	
	@Override
	public void run() {
		byte[] biomes = chunk.getBiomes(picker);
		CompoundTag tag = new CompoundTag();
		tag.put("biomes", biomes);
		try {
			file.getParentFile().mkdirs();
			FileOutputStream stream = new FileOutputStream(file);
			NBTIO.writeGzipped(tag, stream);
			stream.close();
		}
		catch (IOException e) {}
	}
}
