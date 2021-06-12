package paulevs.bnb.util;

import net.minecraft.block.BlockBase;

public class RangedBlockState extends BlockState {
	private int metaMin;
	private int metaMax;
	
	public RangedBlockState(BlockBase block, int metaMax) {
		this(block, 0, metaMax);
	}
	
	public RangedBlockState(int id, int metaMax) {
		this(id, 0, metaMax);
	}
	
	public RangedBlockState(BlockBase block, int metaMin, int metaMax) {
		super(block);
		this.metaMin = metaMin;
		this.metaMax = metaMax;
	}
	
	public RangedBlockState(int id, int metaMin, int metaMax) {
		super(id);
		this.metaMin = metaMin;
		this.metaMax = metaMax;
	}
	
	@Override
	public int getMeta() {
		return MHelper.randRange(metaMin, metaMax, MHelper.getRandom());
	}
}
