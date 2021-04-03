package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.minecraft.item.PlaceableTileEntity;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.interfaces.BlockEnum;
import paulevs.bnb.listeners.TextureListener;

public class MultiBlock extends NetherBlock implements BlockItemProvider {
	protected final BlockEnum[] variants;
	
	public <T extends BlockEnum> MultiBlock(String name, int id, Material material, Class<T> type) {
		super(name, id, material);
		variants = type.getEnumConstants();
		if (variants == null) {
			new RuntimeException("Block " + id + " failed initialization: " + type.getName() + " is not enum!");
		}
	}

	@Override
	public PlaceableTileEntity getBlockItem(int id) {
		return new PlaceableTileEntityWithMeta(id);
	}
	
	@Override
	protected int droppedMeta(int meta) {
		return variants[clampMeta(meta)].getDropMeta();
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		String name = variants[clampMeta(meta)].getTexture(side);
		return TextureListener.getBlockTexture(name);
	}
	
	protected int clampMeta(int meta) {
		return meta % variants.length;
	}
	
	public BlockEnum[] getVariants() {
		return variants;
	}
	
	public int getMeta(int variant) {
		return variants[variant].getMeta();
	}
	
	public BlockEnum getVariant(int meta) {
		return variants[clampMeta(meta)];
	}
}
