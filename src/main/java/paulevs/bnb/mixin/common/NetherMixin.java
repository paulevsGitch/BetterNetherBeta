package paulevs.bnb.mixin.common;

import net.minecraft.level.dimension.Nether;
import net.modificationstation.stationapi.impl.level.StationDimension;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(Nether.class)
public class NetherMixin implements StationDimension {
	@Override
	public short getDefaultLevelHeight() {
		return 256;
	}
	
	@Override
	public short getSectionCount() {
		return (short) (this.getActualLevelHeight() >> 4);
	}
}
