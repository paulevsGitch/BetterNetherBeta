package paulevs.bnb.item;

import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.template.item.TemplateBucketItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.BNB;
import paulevs.bnb.block.BNBBlocks;

public class BNBBucket extends TemplateBucketItem {
	public BNBBucket(Identifier identifier) {
		super(identifier, 0);
	}
	
	@Override
	public ItemStack use(ItemStack item, Level level, PlayerEntity player) {
		float pitch = player.prevPitch + (player.pitch - player.prevPitch);
		float yaw = player.prevYaw + (player.yaw - player.prevYaw);
		
		double posX = player.prevX + (player.x - player.prevX);
		double posY = player.prevY + (player.y - player.prevY) + 1.62 - player.standingEyeHeight;
		double posZ = player.prevZ + (player.z - player.prevZ);
		Vec3D rayStart = Vec3D.getFromCacheAndSet(posX, posY, posZ);
		
		float cosYaw = MCMath.cos(-yaw * 0.017453292F - 3.1415927F);
		float sinYaw = MCMath.sin(-yaw * 0.017453292F - 3.1415927F);
		float cosPitch = -MCMath.cos(-pitch * 0.017453292F);
		
		float dy = MCMath.sin(-pitch * 0.017453292F);
		float dx = sinYaw * cosPitch;
		float dz = cosYaw * cosPitch;
		
		Vec3D rayEnd = rayStart.add(dx * 5.0, dy * 5.0, dz * 5.0);
		HitResult hit = level.getHitResult(rayStart, rayEnd, false);
		
		if (hit == null || hit.type != HitType.BLOCK) return item;
		if (!level.canChangeBlocks(player, hit.x, hit.y, hit.z)) return item;
		
		Direction side = Direction.byId(hit.facing);
		int hitX = hit.x + side.getOffsetX();
		int hitY = hit.y + side.getOffsetY();
		int hitZ = hit.z + side.getOffsetZ();
		
		if (level.isAir(hitX, hitY, hitZ) || !level.getMaterial(hitX, hitY, hitZ).isSolid()) {
			level.setBlockState(hitX, hitY, hitZ, BNBBlocks.SULPHURIC_ACID_FLOWING.getDefaultState());
			return BNB.isCreative(player) ? item : new ItemStack(Item.bucket);
		}
		
		return item;
	}
}
