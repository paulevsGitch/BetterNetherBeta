package paulevs.bnb.block.properties;

import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.block.material.ReplaceableMaterial;

public class BNBBlockMaterials {
	public static final Material NETHER_WOOD = new Material(MaterialColor.WOOD);
	public static final Material NETHER_LEAVES = new Material(MaterialColor.FOLIAGE);
	public static final Material NETHER_PLANT = new Material(MaterialColor.FOLIAGE);
	public static final Material NETHER_PLANT_REPLACEABLE = new ReplaceableMaterial(MaterialColor.FOLIAGE).replaceable();
}
