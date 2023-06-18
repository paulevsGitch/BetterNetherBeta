package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import paulevs.bnb.interfaces.SimpleTexturedBlock;

public class NetherLeavesBlock extends TemplateBlockBase implements SimpleTexturedBlock {
	public NetherLeavesBlock(Identifier id) {
		super(id, Material.LEAVES);
		setHardness(LEAVES.getHardness());
		disableNotifyOnMetaDataChange();
		setSounds(GRASS_SOUNDS);
		disableStat();
	}
}
