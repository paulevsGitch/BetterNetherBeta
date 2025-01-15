package paulevs.bnb.world.generator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.ServerPlayerView;
import net.minecraft.util.maths.Vec2I;
import paulevs.bnb.mixin.client.LevelRendererAccessor;
import paulevs.bnb.mixin.server.ServerPlayerConnectionManagerAccessor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BNBWorldDecoratorThread extends Thread {
	private final List<PlayerPos> centers = Collections.synchronizedList(new ArrayList<>());
	private final List<PlayerPos> centersCopy = new ArrayList<>();
	private volatile BNBDecoratorLevel decorator;
	private volatile List<Vec2I> offsets;
	private volatile boolean canRun;
	private volatile Level lastLevel;
	
	public BNBWorldDecoratorThread() {
		setName("BNB Chunk Decorator");
		updateRadius(8);
		canRun = true;
	}
	
	@Override
	public void run() {
		while (canRun) {
			if (decorator == null || centers == null) continue;
			centersCopy.clear();
			centersCopy.addAll(centers);
			List<Vec2I> offsets = this.offsets;
			if (offsets == null) continue;
			boolean needSearch = true;
			for (int i = 0; needSearch && i < offsets.size(); i++) {
				Vec2I offset = offsets.get(i);
				for (PlayerPos pos : centersCopy) {
					int x = pos.x + offset.x;
					int z = pos.z + offset.z;
					if (decorator.decorate(x, z)) needSearch = false;
				}
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public void updateMain(Minecraft minecraft) {
		if (lastLevel != minecraft.level) {
			lastLevel = minecraft.level;
			if (lastLevel == null || lastLevel.isRemote || lastLevel.dimension.id != -1) {
				lastLevel = null;
				decorator = null;
				return;
			}
			else decorator = new BNBDecoratorLevel(lastLevel);
		}
		
		if (decorator == null) return;
		
		int sectionsX = ((LevelRendererAccessor) minecraft.levelRenderer).bnb_getSectionCounX();
		updateRadius(sectionsX >> 1);
		
		if (centers.isEmpty()) {
			PlayerPos pos = new PlayerPos();
			pos.x = minecraft.player.chunkX;
			pos.z = minecraft.player.chunkZ;
			centers.add(pos);
		}
		else {
			PlayerPos pos = centers.get(0);
			pos.x = minecraft.player.chunkX;
			pos.z = minecraft.player.chunkZ;
		}
		
		decorator.copyBack();
	}
	
	// TODO make server side
	@Environment(EnvType.SERVER)
	public void updateMain(MinecraftServer server) {
		if (lastLevel == null) {
			lastLevel = server.getLevel(-1);
			decorator = new BNBDecoratorLevel(lastLevel);
			int radius = server.serverProperties.getInteger("view-distance", 10);
			updateRadius(radius);
		}
		
		ServerPlayerConnectionManagerAccessor accessor = (ServerPlayerConnectionManagerAccessor) server.serverPlayerConnectionManager;
		ServerPlayerView view = accessor.bnb_getPlayerView(-1);
		
		int index = 0;
		for (Object prePlayer : view.trackers) {
			PlayerEntity player = (PlayerEntity) prePlayer;
			PlayerPos pos;
			if (centers.size() <= index) {
				pos = new PlayerPos();
				centers.add(pos);
			}
			else pos = centers.get(index);
			pos.x = player.chunkX;
			pos.z = player.chunkZ;
			index++;
		}
		
		if (centers.size() > index) {
			centers.subList(index, centers.size()).clear();
		}
	}
	
	public void updateRadius(int radius) {
		int count = radius * 2 + 1;
		count *= count;
		
		if (this.offsets != null && this.offsets.size() == count) return;
		
		List<Vec2I> offsets = new ArrayList<>();
		for (int x = -radius; x <= radius; x++) {
			for (int z = -radius; z <= radius; z++) {
				offsets.add(new Vec2I(x, z));
			}
		}
		offsets.sort((v1, v2) -> {
			int l1 = v1.x * v1.x + v1.z * v1.z;
			int l2 = v2.x * v2.x + v2.z * v2.z;
			return Integer.compare(l1, l2);
		});
		
		this.offsets = offsets;
	}
	
	public void stopThread() {
		canRun = false;
	}
	
	private static class PlayerPos {
		volatile int x;
		volatile int z;
	}
}
