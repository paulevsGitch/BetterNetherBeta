package paulevs.bnb.block;

import net.minecraft.block.Block;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.template.block.TemplateFenceBlock;
import net.modificationstation.stationapi.api.util.Identifier;

public class FenceBlock extends TemplateFenceBlock {
	private final Block source;
	
	public FenceBlock(Identifier id, Block source) {
		super(id, 0);
		setHardness(source.getHardness());
		setSounds(source.sounds);
		this.source = source;
	}
	
	@Override
	public int getTexture(BlockView arg, int i, int j, int k, int l) {
		return source.getTexture(arg, i, j, k, l);
	}
	
	@Override
	public int getTexture(int i, int j) {
		return source.getTexture(i, j);
	}
	
	@Override
	public int getTexture(int i) {
		return source.getTexture(i);
	}
}
