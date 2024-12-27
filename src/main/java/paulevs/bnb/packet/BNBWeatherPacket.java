package paulevs.bnb.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.PacketHandler;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import paulevs.bnb.BNB;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BNBWeatherPacket extends AbstractPacket implements ManagedPacket<BNBWeatherPacket> {
	public static final PacketType<BNBWeatherPacket> TYPE = PacketType.builder(true, true, BNBWeatherPacket::new).build();
	public static final Identifier ID = BNB.id("weather");
	private byte weatherID;
	
	public  BNBWeatherPacket() {}
	
	public BNBWeatherPacket(WeatherType weatherType) {
		weatherID = (byte) weatherType.ordinal();
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			weatherID = stream.readByte();
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeByte(weatherID);
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void apply(PacketHandler handler) {
		if (FabricLoader.getInstance().getEnvironmentType() == EnvType.CLIENT) {
			BNBWeatherManager.setWeather(WeatherType.getByID(weatherID));
		}
	}
	
	@Override
	public int length() {
		return 1;
	}
	
	@Override
	public @NotNull PacketType<BNBWeatherPacket> getType() {
		return TYPE;
	}
}
