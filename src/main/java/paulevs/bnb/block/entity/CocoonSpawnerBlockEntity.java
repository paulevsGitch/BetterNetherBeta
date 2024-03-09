package paulevs.bnb.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.entity.NetherSpiderEntity;

import java.util.Random;

public class CocoonSpawnerBlockEntity extends BlockEntity {
	private int delay = 200;
	
	@Override
	public void tick() {
		if (!this.isPlayerInRange()) return;
		if (level.isRemote) return;
		if (this.delay-- > 0) return;
		
		this.delay = 200 + this.level.random.nextInt(600);
		
		int entityCount = this.level.getEntities(
			NetherSpiderEntity.class,
			Box.createAndCache(x - 16, y - 16, z - 16, x + 16, y + 16, z + 16)
		).size();
		if (entityCount >= 6) return;
		
		Random random = level.random;
		byte count = (byte) (1 + random.nextInt(4));
		
		Identifier id = BlockRegistry.INSTANCE.getId(level.getBlockState(x, y, z).getBlock());
		if (id == null) return;
		
		String entity = "bnb_" + id.path.replace("_cocoon", "");
		
		for(byte i = 0; i < count; ++i) {
			LivingEntity spider = (LivingEntity) EntityRegistry.create(entity, level);
			spider.setPosition(x + random.nextGaussian(), y, z + random.nextGaussian());
			if (level.canSuffocate(
				MathHelper.floor(spider.x),
				MathHelper.floor(spider.y),
				MathHelper.floor(spider.z)
			)) continue;
			if (level.containsLiquids(spider.boundingBox)) continue;
			level.spawnEntity(spider);
		}
	}
	
	public boolean isPlayerInRange() {
		return this.level.getClosestPlayer((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, 16.0D) != null;
	}
}
