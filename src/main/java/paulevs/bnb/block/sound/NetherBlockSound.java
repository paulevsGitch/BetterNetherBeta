package paulevs.bnb.block.sound;

import net.minecraft.block.BlockSounds;
import paulevs.bnb.BetterNetherBeta;

public class NetherBlockSound extends BlockSounds {
	public NetherBlockSound(String sound, float volume, float pitch) {
		super(sound, volume, pitch);
	}
	
	@Override
	public String getBreakSound() {
		return BetterNetherBeta.MOD_ID + ".sound.break." + this.sound;
	}

	@Override
	public String getWalkSound() {
		return BetterNetherBeta.MOD_ID + ".sound.step." + this.sound;
	}
}
