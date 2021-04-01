package paulevs.bnb.block;

import net.minecraft.block.BlockBase;
import net.minecraft.block.material.Material;

public class NetherBlock extends BlockBase {
	private final String registryName;
	
	protected NetherBlock(String registryName, int id, Material material) {
		super(id, material);
		this.setName(registryName);
		this.registryName = registryName;
	}
	
	public String getRegistryName() {
		return registryName;
	}
}
