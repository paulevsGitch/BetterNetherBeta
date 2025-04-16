package paulevs.bnb.packet;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.packet.AbstractPacket;
import net.minecraft.packet.PacketHandler;
import net.modificationstation.stationapi.api.network.packet.ManagedPacket;
import net.modificationstation.stationapi.api.network.packet.PacketType;
import net.modificationstation.stationapi.api.util.Identifier;
import org.jetbrains.annotations.NotNull;
import paulevs.bnb.BNB;
import paulevs.bnb.BNBClient;
import paulevs.bnb.gui.container.SpinningWheelContainer;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;

public class SpinningWheelPacket extends AbstractPacket implements ManagedPacket<SpinningWheelPacket> {
	public static final PacketType<SpinningWheelPacket> TYPE = PacketType.builder(true, false, SpinningWheelPacket::new).build();
	public static final Identifier ID = BNB.id("spinning_wheel");
	private int process;
	
	public SpinningWheelPacket() {}
	
	public SpinningWheelPacket(int process) {
		this.process = process;
	}
	
	@Override
	public void read(DataInputStream stream) {
		try {
			process = stream.readByte() & 255;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}
	
	@Override
	public void write(DataOutputStream stream) {
		try {
			stream.writeByte((byte) process);
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
		return 13;
	}
	
	@NotNull
	@Override
	public PacketType<SpinningWheelPacket> getType() {
		return TYPE;
	}
	
	@Environment(EnvType.CLIENT)
	private void applyClient() {
		Minecraft minecraft = BNBClient.getMinecraft();
		if (minecraft.player.container instanceof SpinningWheelContainer wheelContainer) {
			wheelContainer.entity.setProcess(process);
		}
	}
}
