package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.level.Level;
import paulevs.bnb.block.types.GlowingFur;

public class GlowingFurBlock extends NetherCeilPlantBlock {
	public GlowingFurBlock(String name, int id) {
		super(name, id, GlowingFur.class);
		this.setLightEmittance(1F);
		this.disableNotifyOnMetaDataChange();
		this.disableStat();
	}
	
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 6;
	}
	
	@Override
	public boolean canPlaceAt(Level level, int x, int y, int z) {
		int id = level.getTileId(x, y + 1, z);
		return this.isCeil(id) || (hasVariants(level.getTileMeta(x, y + 1, z)) && id == this.id);
	}
	
	private boolean hasVariants(int meta) {
		return this.getVariant(meta).getDropMeta() != meta || this.getVariant(meta + 1).getDropMeta() != (meta + 1);
	}
	
	@Override
	protected void tick(Level level, int x, int y, int z) {
		super.tick(level, x, y, z);
		int meta = level.getTileMeta(x, y, z);
		if (hasVariants(meta)) {
			int id = level.getTileId(x, y, z);
			if (id == this.id) {
				boolean hasBottom = level.getTileId(x, y - 1, z) == this.id;
				boolean isFull = this.getVariant(meta).getDropMeta() != meta;
				if (isFull && !hasBottom) {
					level.setTileMeta(x, y, z, meta - 1);
				}
				else if (!isFull && hasBottom) {
					level.setTileMeta(x, y, z, meta + 1);
				}
			}
		}
	}
}
