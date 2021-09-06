package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.Living;
import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.MonsterBase;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Nether;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.effects.SoulWithering;
import paulevs.bnb.entity.CloudEntity;
import paulevs.bnb.interfaces.NetherMob;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

@Mixin(value = Creeper.class, priority = 100)
public abstract class CreeperMixin extends MonsterBase implements NetherMob {
	private static final String BNB_SOUL_TEXTURE = BetterNetherBeta.getTexturePath("entity", "soul_creeper");
	
	@Shadow
	int currentFuseTime;
	
	public CreeperMixin(Level level) {
		super(level);
	}
	
	private void startTrack(int value) {
		this.dataTracker.startTracking(18, (byte) value);
	}
	
	@Override
	public int getMobType() {
		return dataTracker.getByte(18);
	}
	
	@Override
	public void setMobType(int value) {
		dataTracker.setInt(18, (byte) value);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public String method_1314() {
		return getMobType() > 0 ? BNB_SOUL_TEXTURE : super.method_1314();
	}
	
	@Override
	public boolean canSpawn() {
		if (this.level.dimension instanceof Nether) {
			int x = MathHelper.floor(this.x);
			int y = MathHelper.floor(this.boundingBox.minY);
			int z = MathHelper.floor(this.z);
			if (BlockUtil.isSoulTerrain(this.level.getTileId(x, y - 1, z))) {
				return true;
			}
		}
		return super.canSpawn();
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	private void bnb_initDataTracker(CallbackInfo info) {
		if (level.dimension instanceof Nether) {
			startTrack(1);
		}
		else {
			startTrack(0);
		}
	}
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	private void bnb_writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		int type = getMobType();
		if (type > 0) {
			tag.put("mobType", (byte) type);
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	private void bnb_readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		setMobType(tag.getByte("mobType"));
	}
	
	@Shadow
	private int getFuseSpeed() {
		return 0;
	}
	
	@Shadow
	private void setFuseSpeed(int newSpeed) {}
	
	@Inject(method = "tryAttack", at = @At("HEAD"), cancellable = true)
	private void bnb_tryAttack(EntityBase target, float f, CallbackInfo info) {
		if (!this.level.isClient && this.currentFuseTime >= 29) {
			level.getEntities(
				Living.class,
				Box.create(this.x - 5, this.y - 2, this.z - 5, this.x + 5, this.y + 2, this.z + 5)
			).forEach(entity -> Living.class.cast(entity).damage(this, 3));
			CloudEntity cloud = new CloudEntity(level);
			cloud.setPositionAndAngles(this.x, this.y, this.z, 0, 0);
			cloud.setStatusEffect(new SoulWithering());
			cloud.setColor(MHelper.hexToInt("49f1f6"));
			level.spawnEntity(cloud);
			bnb_explosionEffects();
			this.field_663 = true;
			this.remove();
			info.cancel();
		}
	}
	
	private void bnb_explosionEffects() {
		this.level.playSound(this.x, this.y, this.z, "random.explode", 4.0F, (1.0F + MHelper.randRange(-0.2F, 0.2F, this.level.rand)) * 0.7F);
		for (int i = 0; i < 50; i++) {
			float dx = MHelper.randRange(-1, 1, this.level.rand);
			float dy = MHelper.randRange(-1, 1, this.level.rand);
			float dz = MHelper.randRange(-1, 1, this.level.rand);
			float l = dx * dx + dy * dy + dz * dz;
			if (l > 0) {
				l = MathHelper.sqrt(l) * 2F;
				dx /= l;
				dy /= l;
				dz /= l;
			}
			this.level.addParticle("explode", x, y, z, dx, dy, dz);
			this.level.addParticle("smoke", x, y, z, dx, dy, dz);
		}
	}
}
