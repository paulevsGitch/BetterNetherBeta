package paulevs.bnb.block.slab;

import net.minecraft.block.Block;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.Function;

public class BNBSlab extends TemplateBlock {
	private final Function<Integer, Integer> textureGetter;
	
	public BNBSlab(Identifier id, Block source) {
		super(id, source.material);
		this.textureGetter = source::getTexture;
		setTranslationKey(id);
		setSounds(source.sounds);
	}
	
	@Override
	public int getTexture(int side) {
		return textureGetter.apply(side);
	}
}
