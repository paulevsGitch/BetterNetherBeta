package paulevs.bnb.block.tileentity;

import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.monster.Spider;
import net.minecraft.tileentity.TileEntityBase;
import net.minecraft.util.maths.Box;
import paulevs.bnb.interfaces.NetherMob;

public class CocoonSpawner extends TileEntityBase {
	private int delay = 20;
	
	@Override
	public void tick() {
		if (this.isPlayerInRange()) {
			if (!this.level.isClient) {
				if (this.delay == -1) {
					this.resetDelay();
				}

				if (this.delay > 0) {
					--this.delay;
					return;
				}

				int count = 1 + level.rand.nextInt(4);
				for(int i = 0; i < count; ++i) {
					Spider spider = (Spider) EntityRegistry.create("Spider", this.level);
					
					if (spider == null) {
						return;
					}

					int entityCount = this.level.getEntities(spider.getClass(), Box.method_94(this.x, this.y, this.z, this.x + 1, this.y + 1, this.z + 1).expand(8.0D, 4.0D, 8.0D)).size();
					if (entityCount >= 6) {
						this.resetDelay();
						return;
					}

					double posX = this.x + this.level.rand.nextGaussian() * 0.3;
					double posY = this.y;
					double posZ = this.z + this.level.rand.nextGaussian() * 0.3;
					spider.setPositionAndAngles(posX, posY, posZ, this.level.rand.nextFloat() * 360.0F, 0.0F);
					if (spider.canSpawn()) {
						int type = (level.getTileMeta(x, y, z) % 3) + 1;
						((NetherMob) spider).setMobType(type);
						this.level.spawnEntity(spider);
						this.resetDelay();
					}
				}
			}

			super.tick();
		}
	}
	
	private void resetDelay() {
		this.delay = 200 + this.level.rand.nextInt(600);
	}
	
	public boolean isPlayerInRange() {
		return this.level.getClosestPlayer((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, 16.0D) != null;
	}
}
