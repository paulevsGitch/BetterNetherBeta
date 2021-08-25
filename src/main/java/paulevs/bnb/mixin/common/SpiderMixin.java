package paulevs.bnb.mixin.common;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.monster.MonsterBase;
import net.minecraft.entity.monster.Spider;
import net.minecraft.level.Level;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Nether;
import net.minecraft.util.io.CompoundTag;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.interfaces.NetherMob;
import paulevs.bnb.world.biome.CrimsonForest;
import paulevs.bnb.world.biome.PoisonForest;
import paulevs.bnb.world.biome.WarpedForest;

@Mixin(value = Spider.class, priority = 100)
public abstract class SpiderMixin extends MonsterBase implements NetherMob {
	private static final String[] TEXTURES = new String[] {
		BetterNetherBeta.getTexturePath("entity", "crimson_spider"),
		BetterNetherBeta.getTexturePath("entity", "warped_spider"),
		BetterNetherBeta.getTexturePath("entity", "poison_spider")
	};
	
	public SpiderMixin(Level level) {
		super(level);
	}

	@Override
	protected void initDataTracker() {
		super.initDataTracker();
		if (level.dimension instanceof Nether) {
			Biome biome = level.getBiomeSource().getBiome((int) x, (int) z);
			if (biome instanceof CrimsonForest) {
				startTrack(1);
			}
			else if (biome instanceof WarpedForest) {
				startTrack(2);
			}
			else if (biome instanceof PoisonForest) {
				startTrack(3);
			}
			else {
				startTrack(1);
			}
		}
		else {
			startTrack(0);
		}
	}
	
	private void startTrack(int value) {
		this.dataTracker.startTracking(16, new Byte((byte) value));
	}
	
	@Override
	public int getMobType() {
		return dataTracker.getByte(16);
	}
	
	@Override
	public void setMobType(int value) {
		dataTracker.setInt(16, (byte) (value));
	}
	
	@Environment(EnvType.CLIENT)
	public String method_1314() {
		int type = getMobType();
		return type > 0 ? TEXTURES[type - 1] : this.texture;
	}
	
	@Override
	public void writeCustomDataToTag(CompoundTag tag) {
		super.writeCustomDataToTag(tag);
		int type = getMobType();
		if (type > 0) {
			tag.put("mobType", (byte) type);
		}
	}

	@Override
	public void readCustomDataFromTag(CompoundTag tag) {
		super.readCustomDataFromTag(tag);
		setMobType(tag.getByte("mobType"));
	}
}
