package paulevs.bnb.entity;

import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.util.Util;

public interface BNBPortalEntity {
	default Level bnb_getOriginLevel() {
		return Util.assertImpl();
	}
	
	default BlockPos bnb_getOriginPos() {
		return Util.assertImpl();
	}
	
	default void bnb_setPortalOrigin(Level level, int x, int y, int z) {
		Util.assertImpl();
	}
}
