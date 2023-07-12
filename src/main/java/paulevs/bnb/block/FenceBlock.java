package paulevs.bnb.block;

import net.minecraft.block.BaseBlock;
import net.minecraft.level.BlockView;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateFence;

public class FenceBlock extends TemplateFence {
	private final BaseBlock source;
	
	public FenceBlock(Identifier id, BaseBlock source) {
		super(id, 0);
		setHardness(source.getHardness());
		setSounds(source.sounds);
		this.source = source;
	}
	
	@Override
	public int getTextureForSide(BlockView arg, int i, int j, int k, int l) {
		return source.getTextureForSide(arg, i, j, k, l);
	}
	
	@Override
	public int getTextureForSide(int i, int j) {
		return source.getTextureForSide(i, j);
	}
	
	@Override
	public int getTextureForSide(int i) {
		return source.getTextureForSide(i);
	}
}
