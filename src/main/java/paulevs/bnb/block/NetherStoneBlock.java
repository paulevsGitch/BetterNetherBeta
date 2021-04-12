package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import paulevs.bnb.interfaces.RenderTypePerMeta;
import paulevs.bnb.interfaces.StoneBlockEnum;
import paulevs.bnb.util.BlockUtil;

public class NetherStoneBlock extends MultiBlock implements RenderTypePerMeta {
	public <T extends StoneBlockEnum> NetherStoneBlock(String name, int id, Class<T> type) {
		super(name, id, Material.STONE, type);
		this.setHardness(STONE.getHardness());
	}

	@Override
	public int getTypeByMeta(int meta) {
		StoneBlockEnum variant = (StoneBlockEnum) getVariant(meta);
		return meta == variant.getPillarXMeta() || meta == variant.getPillarZMeta() ? 16 : 0;
	}
	
	@Override
	public void onBlockPlaced(Level level, int x, int y, int z, int facing) {
		super.onBlockPlaced(level, x, y, z, facing);
		int meta = level.getTileMeta(x, y, z);
		StoneBlockEnum variant = (StoneBlockEnum) getVariant(meta);
		if (variant.isPillar()) {
			int metaPillar = meta;
			if (BlockUtil.isSideY(facing)) {
				metaPillar = variant.getPillarYMeta();
			}
			else if (BlockUtil.isSideX(facing)) {
				metaPillar = variant.getPillarXMeta();
			}
			else {
				metaPillar = variant.getPillarZMeta();
			}
			if (metaPillar != meta) {
				level.setTileMeta(x, y, z, metaPillar);
			}
		}
	}
}
