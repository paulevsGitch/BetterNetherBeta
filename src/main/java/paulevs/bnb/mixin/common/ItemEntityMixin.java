package paulevs.bnb.mixin.common;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.entity.technical.ItemEntity;
import net.minecraft.item.BlockItem;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.At.Shift;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.achievement.BNBAchievements;
import paulevs.bnb.block.BNBBlocks;
import paulevs.bnb.block.property.BNBBlockMaterials;
import paulevs.bnb.item.BNBItemTags;
import paulevs.bnb.item.BNBItems;

@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin extends Entity {
	@Shadow public ItemStack stack;
	@Shadow private int health;
	
	@Shadow public abstract void onPlayerCollision(PlayerEntity player);
	
	@Shadow public int age;
	
	public ItemEntityMixin(Level arg) {
		super(arg);
	}
	
	@Inject(method = "<init>(Lnet/minecraft/level/Level;DDDLnet/minecraft/item/ItemStack;)V", at = @At("TAIL"))
	private void bnb_makeFireproofInit(Level level, double x, double y, double z, ItemStack stack, CallbackInfo info) {
		if (stack != null && stack.isIn(BNBItemTags.NON_FLAMMABLE)) {
			immuneToFire = true;
			health = Integer.MAX_VALUE;
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void bnb_makeFireproofReadData(CompoundTag tag, CallbackInfo info) {
		if (stack != null && stack.isIn(BNBItemTags.NON_FLAMMABLE)) {
			immuneToFire = true;
			health = Integer.MAX_VALUE;
		}
	}
	
	@Inject(method = "onPlayerCollision", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;playSound(Lnet/minecraft/entity/Entity;Ljava/lang/String;FF)V",
		shift = Shift.BEFORE
	))
	private void bnb_onCollision(PlayerEntity player, CallbackInfo info) {
		if (!(stack.getType() instanceof BlockItem item)) return;
		Block block = item.getBlock();
		
		if (block == BNBBlocks.FALURIAN_LOG) player.incrementStat(BNBAchievements.COLLECT_FALURIAN_LOG);
		if (block == BNBBlocks.PIROZEN_LOG) player.incrementStat(BNBAchievements.COLLECT_PIROZEN_LOG);
		if (block == BNBBlocks.CHLOROPHATE_LOG) player.incrementStat(BNBAchievements.COLLECT_CHLOROPHATE_LOG);
		
		int summ = BNBAchievements.readStat(BNBAchievements.COLLECT_FALURIAN_LOG) > 0 ? 1 : 0;
		summ += BNBAchievements.readStat(BNBAchievements.COLLECT_PIROZEN_LOG) > 0 ? 1 : 0;
		summ += BNBAchievements.readStat(BNBAchievements.COLLECT_CHLOROPHATE_LOG) > 0 ? 1 : 0;
		
		if (summ == 3) {
			player.incrementStat(BNBAchievements.RGB);
		}
	}
	
	@WrapOperation(method = "tick", at = @At(
		value = "INVOKE",
		target = "Lnet/minecraft/level/Level;getMaterial(III)Lnet/minecraft/block/material/Material;"
	))
	private Material bnb_disableLavaVelocity(Level level, int x, int y, int z, Operation<Material> original) {
		Material material = original.call(level, x, y, z);
		
		if (material == BNBBlockMaterials.SULPHURIC_ACID) {
			if ((age & 3) == 0) {
				if (stack.getType() == BNBItems.AMETRINE_SHARD) {
					dropItem(new ItemStack(BNBItems.PURE_QUARTZ), 0.0F);
					level.playSound(x, y, z, "random.fizz", 1.0F, 1.0F);
					level.addParticle(
						"smoke",
						x + level.random.nextFloat() * 0.2F - 0.1F,
						y + level.random.nextFloat() * 0.2F - 0.1F,
						z + level.random.nextFloat() * 0.2F - 0.1F,
						0.0F, 0.0F, 0.0F
					);
					if (--stack.count < 1) remove();
				}
				else if (stack.getType() != BNBItems.PURE_QUARTZ) {
					level.playSound(x, y, z, "random.fizz", 1.0F, 1.0F);
					level.addParticle(
						"smoke",
						x + level.random.nextFloat() * 0.2F - 0.1F,
						y + level.random.nextFloat() * 0.2F - 0.1F,
						z + level.random.nextFloat() * 0.2F - 0.1F,
						0.0F, 0.0F, 0.0F
					);
					if (--stack.count < 1) remove();
				}
			}
			return Material.WATER;
		}
		
		if (!immuneToFire || material != Material.LAVA) return material;
		
		float h = stack.getType() instanceof BlockItem ? 0.9F : 0.7F;
		float dy = (float) ((y + h) - this.y);
		if (level.getBlockState(x, y + 1, z).getMaterial() == Material.LAVA) {
			dy = 1.0F;
		}
		if (dy > 0) velocityY = dy * 0.5F;
		velocityX *= 0.9;
		velocityZ *= 0.9;
		
		PlayerEntity player = level.getClosestPlayerTo(this, 2.5F);
		if (player != null && player.y - y > -0.25) {
			onPlayerCollision(player);
		}
		
		return Material.WATER;
	}
}
