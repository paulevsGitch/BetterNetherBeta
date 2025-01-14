package paulevs.bnb.block.entity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MCMath;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.registry.BlockRegistry;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.block.BNBBlockTags;
import paulevs.bnb.entity.NetherSpiderEntity;

import java.util.Random;

public class CocoonSpawnerBlockEntity extends BlockEntity {
	private int delay = 200 + (hashCode() % 600);
	
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
			if (!canSpawn(spider)) continue;
			level.spawnEntity(spider);
		}
	}
	
	public boolean isPlayerInRange() {
		return this.level.getClosestPlayer((double)this.x + 0.5D, (double)this.y + 0.5D, (double)this.z + 0.5D, 16.0D) != null;
	}
	
	private static boolean canSpawn(LivingEntity entity) {
		if (entity.level.canSuffocate(MCMath.floor(entity.x), MCMath.floor(entity.y), MCMath.floor(entity.z))) return false;
		if (!entity.level.getEntities(entity.getClass(), entity.boundingBox).isEmpty()) return false;
		
		int x1 = MCMath.floor(entity.boundingBox.minX);
		int y1 = MCMath.floor(entity.boundingBox.minY);
		int z1 = MCMath.floor(entity.boundingBox.minZ);
		int x2 = MCMath.floor(entity.boundingBox.maxX);
		int y2 = MCMath.floor(entity.boundingBox.maxY);
		int z2 = MCMath.floor(entity.boundingBox.maxZ);
		
		for (int x = x1; x <= x2; x++) {
			for (int y = y1; y <= y2; y++) {
				for (int z = z1; z <= z2; z++) {
					BlockState state = entity.level.getBlockState(x, y, z);
					if (state.isIn(BNBBlockTags.OBSIDIAN)) return false;
					if (state.getBlock().material == Material.LAVA) return false;
					boolean canSpawn = state.isAir() || state.getBlock().material.hasNoSuffocation() || !state.getBlock().material.blocksMovement();
					if (!canSpawn) return false;
				}
			}
		}
		
		return true;
	}
}
