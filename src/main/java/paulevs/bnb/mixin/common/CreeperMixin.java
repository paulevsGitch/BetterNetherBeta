package paulevs.bnb.mixin.common;

import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.MonsterBase;
import net.minecraft.level.Level;
import net.minecraft.level.dimension.Nether;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.interfaces.NetherMob;

@Mixin(value = Creeper.class, priority = 100)
public abstract class CreeperMixin extends MonsterBase implements NetherMob {
	public CreeperMixin(Level level) {
		super(level);
	}
	
	@Inject(method = "initDataTracker", at = @At("TAIL"))
	public void bnb_initDataTracker(CallbackInfo info) {
		if (level.dimension instanceof Nether) {
			startTrack(1);
		}
		else {
			startTrack(0);
		}
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
	
	@Inject(method = "writeCustomDataToTag", at = @At("TAIL"))
	public void bnb_writeCustomDataToTag(CompoundTag tag, CallbackInfo info) {
		int type = getMobType();
		if (type > 0) {
			tag.put("mobType", (byte) type);
		}
	}
	
	@Inject(method = "readCustomDataFromTag", at = @At("TAIL"))
	public void bnb_readCustomDataFromTag(CompoundTag tag, CallbackInfo info) {
		setMobType(tag.getByte("mobType"));
	}
}
