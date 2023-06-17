package paulevs.bnb.sound;

import net.minecraft.block.BlockSounds;
import paulevs.bnb.BNB;

public class NetherBlockSound extends BlockSounds {
	private final String breakSound;
	private final String walkSound;
	
	public NetherBlockSound(String sound, float volume, float pitch) {
		super(sound, volume, pitch);
		breakSound = BNB.id("break." + this.sound).toString();
		walkSound = BNB.id("step." + this.sound).toString();
	}
	
	@Override
	public String getBreakSound() {
		return breakSound;
	}

	@Override
	public String getWalkSound() {
		return walkSound;
	}
}
