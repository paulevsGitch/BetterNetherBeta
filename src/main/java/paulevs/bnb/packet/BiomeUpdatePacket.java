package paulevs.bnb.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.biome.Biome;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.PacketHandler;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.io.NBTIO;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.world.biome.BNBBiomeSource;
import paulevs.bnb.world.map.MapChunk;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.function.Function;

public class BiomeUpdatePacket extends AbstractPacket implements ManagedPacket<BiomeUpdatePacket> {
	public static final PacketType<BiomeUpdatePacket> TYPE = PacketType.builder(true, false, BiomeUpdatePacket::new).build();
	public static final Identifier ID = BNB.id("biome_update");
	private static final ByteArrayOutputStream STREAM_OUT = new ByteArrayOutputStream(8192);
	
	private long position;
	private byte[] data;
	private int length;
	
	public BiomeUpdatePacket() {}
	
	public BiomeUpdatePacket(long position, MapChunk<Biome> chunk, Function<Biome, String> serialiser) {
		this.position = position;
		
		CompoundTag tag = new CompoundTag();
		chunk.save(tag, serialiser);
		
		STREAM_OUT.reset();
		NBTIO.writeGzipped(tag, STREAM_OUT);
		
		data = STREAM_OUT.toByteArray();
		length = Long.BYTES + Short.BYTES + data.length;
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			position = stream.readLong();
			int length = stream.readShort();
			data = stream.readNBytes(length);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeLong(position);
			stream.writeShort(data.length);
			stream.write(data);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(PacketHandler handler) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.CLIENT) return;
		applyClient();
	}
	
	@Override
	public int length() {
		return length;
	}
	
	@NotNull
	@Override
	public PacketType<BiomeUpdatePacket> getType() {
		return TYPE;
	}
	
	@Environment(EnvType.CLIENT)
	private void applyClient() {
		Minecraft minecraft = BNBClient.getMinecraft();
		if (!minecraft.level.isRemote) return;
		if (!(minecraft.level.getBiomeSource() instanceof BNBBiomeSource source)) return;
		
		ByteArrayInputStream stream = new ByteArrayInputStream(data);
		CompoundTag tag = NBTIO.readGzipped(stream);
		try {
			stream.close();
		}
		catch (IOException ignore) {}
		
		source.updateData(position, tag);
	}
}
