package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;

public class NetherLeavesBlock extends TemplateBlockBase {
	public NetherLeavesBlock(Identifier id) {
		super(id, Material.LEAVES);
		setHardness(LEAVES.getHardness());
		disableNotifyOnMetaDataChange();
		setSounds(GRASS_SOUNDS);
		disableStat();
	}
}
