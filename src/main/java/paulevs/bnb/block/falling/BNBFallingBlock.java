package paulevs.bnb.block.falling;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

// TODO wait for StAPI fix: https://github.com/ModificationStation/StationAPI/issues/182
public class BNBFallingBlock extends /*TemplateSandBlock*/ TemplateBlock {
	public BNBFallingBlock(Identifier id) {
		//super(id, 0);
		super(id, Material.DIRT);
		setHardness(1.0F);
		setSounds(GRAVEL_SOUNDS);
	}
}
