package paulevs.bnb.interfaces;

import net.minecraft.level.Level;

public interface Bonemealable {
	boolean onBonemealUse(Level level, int x, int y, int z, int meta);
}
