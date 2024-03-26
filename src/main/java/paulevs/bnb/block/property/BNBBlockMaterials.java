package paulevs.bnb.block.property;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.ReplaceableMaterial;

public class BNBBlockMaterials {
	public static final Material NETHER_LOG = new Material(MaterialColor.WOOD);
	public static final Material NETHER_LEAVES = new Material(MaterialColor.FOLIAGE);
	public static final Material NETHER_PLANT = new Material(MaterialColor.FOLIAGE);
	public static final Material NETHER_CLOTH = new Material(MaterialColor.WHITE);
	public static final Material NETHER_PLANT_REPLACEABLE = new ReplaceableMaterial(MaterialColor.FOLIAGE).replaceable();
	public static final Material SPIDER_NET = new NetMaterial();
	
	private static class NetMaterial extends Material {
		public NetMaterial() {
			super(MaterialColor.WEB);
			breaksWhenPushed();
		}
		
		@Override
		public boolean blocksMovement() {
			return false;
		}
	}
}
