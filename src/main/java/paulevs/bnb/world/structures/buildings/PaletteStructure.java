package paulevs.bnb.world.structures.buildings;

import paulevs.bnb.util.BlockState;

public abstract class PaletteStructure extends BuildingStructure {
	public PaletteStructure(int side, int height, int offsetY) {
		super(side, height);
		setOffset(-(side >> 1), offsetY, -(side >> 1));
	}
	
	@Override
	protected void init() {
		BlockState[] palette = makePalette();
		byte[] data = makeData();
		for (int i = 0; i < data.length; i++) {
			int id = data[i] - 1;
			if (id >= 0) {
				setBlockState(i, palette[id]);
			}
		}
	}
	
	protected abstract BlockState[] makePalette();
	
	protected abstract byte[] makeData();
}
