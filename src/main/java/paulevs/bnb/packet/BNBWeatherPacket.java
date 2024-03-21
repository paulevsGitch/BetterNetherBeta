package paulevs.bnb.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.network.PacketHandler;
import net.minecraft.packet.AbstractPacket;
import net.modificationstation.stationapi.api.network.packet.IdentifiablePacket;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNB;
import paulevs.bnb.weather.BNBWeatherManager;
import paulevs.bnb.weather.WeatherType;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class BNBWeatherPacket extends AbstractPacket implements IdentifiablePacket {
	private static final Identifier ID = BNB.id("weather");
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
	public Identifier getId() {
		return ID;
	}
	
	public static void register() {
		IdentifiablePacket.register(ID, true, true, BNBWeatherPacket::new);
	}
}
