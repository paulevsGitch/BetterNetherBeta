package paulevs.bnb.world.generator;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.level.Level;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.maths.Vec2I;
import paulevs.bnb.mixin.client.LevelRendererAccessor;

import java.util.ArrayList;
import java.util.List;

public class BNBWorldDecoratorThread extends Thread {
	private volatile List<Vec2I> offsets;
	private volatile BNBDecoratorLevel decorator;
	private volatile boolean canRun;
	private volatile Level lastLevel;
	private volatile int centerX;
	private volatile int centerZ;
	
	public BNBWorldDecoratorThread() {
		setName("BNB Chunk Decorator");
		updateRadius(8);
		canRun = true;
	}
	
	@Override
	public void run() {
		while (canRun) {
			if (decorator == null) continue;
			List<Vec2I> offsets = this.offsets;
			if (offsets == null) continue;
			for (Vec2I offset : offsets) {
				int x = centerX + offset.x;
				int z = centerZ + offset.z;
				if (decorator.decorate(x, z)) break;
			}
		}
	}
	
	@Environment(EnvType.CLIENT)
	public void updateMain(Minecraft minecraft) {
		if (lastLevel != minecraft.level) {
			lastLevel = minecraft.level;
			if (lastLevel == null || lastLevel.isRemote || lastLevel.dimension.id != -1) {
				decorator = null;
			}
			else decorator = new BNBDecoratorLevel(lastLevel);
		}
		
		if (decorator == null) return;
		
		int sectionsX = ((LevelRendererAccessor) minecraft.levelRenderer).bnb_getSectionCounX();
		updateRadius(sectionsX >> 1);
		
		centerX = minecraft.player.chunkX;
		centerZ = minecraft.player.chunkZ;
		
		decorator.copyBack();
	}
	
	@Environment(EnvType.SERVER)
	public void updateMain(MinecraftServer server) {
		if (lastLevel == null) {
			lastLevel = server.getLevel(-1);
			decorator = new BNBDecoratorLevel(lastLevel);
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
}
