package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import paulevs.bnb.sound.BNBSounds;

public class NetherMossBlock extends TemplateBlockBase {
	public NetherMossBlock(Identifier identifier) {
		super(identifier, Material.LEAVES);
		setSounds(BNBSounds.MOSS_BLOCK);
		setHardness(1F);
	}
}
