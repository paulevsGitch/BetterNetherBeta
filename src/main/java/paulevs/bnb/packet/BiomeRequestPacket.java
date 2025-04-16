package paulevs.bnb.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.level.Level;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.PacketHandler;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import paulevs.bnb.BNB;
import paulevs.bnb.mixin.server.ServerPlayerPacketHandlerAccessor;
import paulevs.bnb.world.biome.BNBBiomeSource;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BiomeRequestPacket extends AbstractPacket implements ManagedPacket<BiomeRequestPacket> {
	public static final PacketType<BiomeRequestPacket> TYPE = PacketType.builder(false, true, BiomeRequestPacket::new).build();
	public static final Identifier ID = BNB.id("biome_request");
	private long position;
	
	public BiomeRequestPacket() {}
	
	public BiomeRequestPacket(long position) {
		this.position = position;
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			position = stream.readLong();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeLong(position);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(PacketHandler handler) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) return;
		applyServer(handler);
	}
	
	@Override
	public int length() {
		return Long.BYTES;
	}
	
	@NotNull
	@Override
	public PacketType<BiomeRequestPacket> getType() {
		return TYPE;
	}
	
	@Environment(EnvType.SERVER)
	private void applyServer(PacketHandler handler) {
		@SuppressWarnings("deprecation")
		MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
		Level level = server.getLevel(-1);
		if (!(level.getBiomeSource() instanceof BNBBiomeSource source)) return;
		ServerPlayerPacketHandlerAccessor accessor = (ServerPlayerPacketHandlerAccessor) handler;
		ServerPlayer player = accessor.bnb_getServerPlayer();
		source.requestUpdate(player, position);
	}
}
