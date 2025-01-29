package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.ModifyReturnValue;
import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.minecraft.util.maths.Box;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.entity.BNBPortalEntity;
import paulevs.bnb.entity.ObsidianBoatEntity;
import paulevs.bnb.item.BNBItemTags;
import paulevs.bnb.world.generator.decorator.BNBChunkStatus;
import paulevs.bnb.world.generator.decorator.BNBWorldChunk;

@Mixin(Entity.class)
public abstract class EntityMixin implements BNBPortalEntity {
	@Unique private final MutableBlockPos bnb_originPortalPos = new MutableBlockPos();
	@Unique private Level bnb_originPortalLevel;
	@Unique private static boolean bnb_isAcid;
	
	@Shadow public Level level;
	@Shadow public int chunkX;
	@Shadow public int chunkZ;
	@Shadow @Final public Box boundingBox;
	@Shadow protected float fallDistance;
	@Shadow private boolean skipFallCheck;
	
	@Shadow public abstract boolean damage(Entity target, int amount);
	
	@Inject(method = "setOnFire", at = @At("HEAD"), cancellable = true)
	private void bnb_disableFireDamage(CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if ((entity.vehicle instanceof ObsidianBoatEntity)) {
			info.cancel();
			return;
		}
		if (entity instanceof PlayerEntity player && bnb_damageArmor(player)) {
			info.cancel();
		}
	}
	
	@Inject(method = "isInLava", at = @At("HEAD"), cancellable = true)
	private void bnb_isInLava(CallbackInfoReturnable<Boolean> info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if (!(entity.vehicle instanceof ObsidianBoatEntity)) return;
		info.setReturnValue(false);
	}
	
	@Inject(method = "move", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/entity/Entity;damageByFire(I)V",
		shift = Shift.BEFORE
	), cancellable = true)
	private void bnb_disableMovementFireDamage(double x, double y, double z, CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof LivingEntity entity)) return;
		if ((entity.vehicle instanceof ObsidianBoatEntity)) {
			info.cancel();
			return;
		}
		if (entity instanceof PlayerEntity player && player.fire > 0 && bnb_damageArmor(player)) {
			info.cancel();
		}
	}
	
	@Inject(method = "damageByFire", at = @At("HEAD"), cancellable = true)
	private void bnb_disableFireDamage(int damage, CallbackInfo info) {
		if (!(Entity.class.cast(this) instanceof PlayerEntity player)) return;
		for (int i = 3; i >= 0; i--) {
			ItemStack stack = player.inventory.armor[i];
			if (stack != null && stack.isIn(BNBItemTags.FIREPROOF_ARMOR)) {
				info.cancel();
				return;
			}
		}
	}
	
	@Unique
	private boolean bnb_damageArmor(PlayerEntity player) {
		for (int i = 3; i >= 0; i--) {
			ItemStack stack = player.inventory.armor[i];
			if (stack != null && stack.isIn(BNBItemTags.FIREPROOF_ARMOR)) {
				stack.applyDamage(1, player);
				if (stack.count < 1) {
					player.inventory.armor[i] = null;
				}
				player.fire = 0;
				return true;
			}
		}
		return false;
	}
	
	@Inject(method = "move", at = @At("HEAD"), cancellable = true)
	private void bnb_checkChunk(double x, double y, double z, CallbackInfo info) {
		boolean isInLava = level.collidesWithMaterial(
			boundingBox.expandNegative(0.0, -0.4, 0.0).createAndCache(0.001, 0.001, 0.001),
			Material.LAVA,
			Entity.class.cast(this)
		);
		if (isInLava) {
			skipFallCheck = true;
			fallDistance = 0.0F;
		}
		
		if (level == null || level.isRemote || level.dimension.id != -1) return;
		BNBWorldChunk chunk = BNBWorldChunk.cast(level.getChunkFromCache(chunkX, chunkZ));
		if (chunk == null) return;
		if (chunk.bnb_getStatus() == BNBChunkStatus.EMPTY) {
			info.cancel();
		}
	}
	
	@ModifyReturnValue(method = "checkGroundCollision", at = @At("RETURN"))
	private boolean bnb_checkAcid(boolean original) {
		bnb_isAcid = original || level.collidesWithMaterial(
			boundingBox.expandNegative(0.0, -0.4F, 0.0).createAndCache(0.001, 0.001, 0.001),
			BNBBlockMaterials.SULPHURIC_ACID,
			Entity.class.cast(this)
		);
		if (bnb_isAcid) damage(null, 2);
		return bnb_isAcid;
	}
	
	@WrapOperation(method = "baseTick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;addParticle(Ljava/lang/String;DDDDDD)V"
	))
	private void bnb_removeParticles(Level level, String name, double x, double y, double z, double dx, double dy, double dz, Operation<Void> original) {
		if (!bnb_isAcid) original.call(level, name, x, y, z, dx, dy, dz);
	}
	
	@Override
	public Level bnb_getOriginLevel() {
		return bnb_originPortalLevel;
	}
	
	@Override
	public BlockPos bnb_getOriginPos() {
		return bnb_originPortalPos;
	}
	
	@Override
	public void bnb_setPortalOrigin(Level level, int x, int y, int z) {
		bnb_originPortalLevel = level;
		bnb_originPortalPos.set(x, y, z);
	}
}
