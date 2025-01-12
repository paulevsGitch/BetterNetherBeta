package paulevs.bnb.rendering;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.modificationstation.stationapi.api.util.math.MutableBlockPos;

@Environment(EnvType.CLIENT)
public class LavaRenderer {
	public static final MutableBlockPos POS = new MutableBlockPos();
	public static final int[] STILL_TEXTURES = new int[16];
	public static int flowTexture;
}
