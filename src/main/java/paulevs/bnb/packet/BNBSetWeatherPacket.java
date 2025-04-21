package paulevs.bnb.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.living.player.ServerPlayer;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.PacketHandler;
import net.minecraft.server.MinecraftServer;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketHelper;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import paulevs.bnb.BNB;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BNBSetWeatherPacket extends AbstractPacket implements ManagedPacket<BNBSetWeatherPacket> {
	public static final PacketType<BNBSetWeatherPacket> TYPE = PacketType.builder(false, true, BNBSetWeatherPacket::new).build();
	public static final Identifier ID = BNB.id("weather_set");
	
	private byte weatherID;
	private int weatherLength;
	
	public BNBSetWeatherPacket() {}
	
	public BNBSetWeatherPacket(WeatherType weatherType, int weatherLength) {
		weatherID = (byte) weatherType.ordinal();
		this.weatherLength = weatherLength;
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			weatherID = stream.readByte();
			weatherLength = stream.readInt();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeByte(weatherID);
			stream.writeInt(weatherLength);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(PacketHandler handler) {
		if (FabricLoader.getInstance().getEnvironmentType() != EnvType.SERVER) return;
		applyServer();
	}
	
	@Override
	public int length() {
		return 5;
	}
	
	@NotNull
	@Override
	public PacketType<BNBSetWeatherPacket> getType() {
		return TYPE;
	}
	
	@Environment(EnvType.SERVER)
	private void applyServer() {
		BNBWeatherManager.setWeather(WeatherType.getByID(weatherID), weatherLength);
		@SuppressWarnings("deprecation")
		MinecraftServer server = (MinecraftServer) FabricLoader.getInstance().getGameInstance();
		WeatherType type = WeatherType.getByID(weatherID);
		for (Object playerObj : server.serverPlayerConnectionManager.players) {
			ServerPlayer player = (ServerPlayer) playerObj;
			if (player.dimensionId != -1) continue;
			PacketHelper.sendTo(player, new BNBWeatherPacket(type));
		}
	}
}
