package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.sound.BNBSounds;

public class NetherMossBlock extends TemplateBlock {
	public NetherMossBlock(Identifier identifier) {
		super(identifier, Material.LEAVES);
		setSounds(BNBSounds.MOSS_BLOCK);
		setHardness(1F);
	}
}
