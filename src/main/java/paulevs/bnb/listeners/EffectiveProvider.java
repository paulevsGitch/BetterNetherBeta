package paulevs.bnb.listeners;

import java.util.List;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;
import net.minecraft.item.tool.Hatchet;
import net.minecraft.item.tool.Pickaxe;
import net.minecraft.item.tool.ToolBase;
import net.minecraft.item.tool.ToolMaterial;
import net.modificationstation.stationloader.api.common.event.item.tool.EffectiveBlocksProvider;
import paulevs.bnb.block.material.NetherMaterials;

public class EffectiveProvider implements EffectiveBlocksProvider {
	@Override
	public void getEffectiveBlocks(ToolBase toolBase, ToolMaterial toolMaterial, List<BlockBase> list) {
		BlockListener.getModBlocks().forEach((block) -> {
			if (toolBase instanceof Hatchet && (block.material.equals(Material.WOOD) || block.material.equals(NetherMaterials.NETHER_WOOD))) {
				list.add(block);
			}
			else if (toolBase instanceof Pickaxe && block.material.equals(Material.STONE)) {
				list.add(block);
			}
		});
	}
}
