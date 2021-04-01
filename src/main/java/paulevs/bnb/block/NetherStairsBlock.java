package paulevs.bnb.block;

import java.util.ArrayList;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.Living;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.Box;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.material.NetherMaterials;
import paulevs.bnb.listeners.TextureListener;

public class NetherStairsBlock extends NetherBlock {
	private final String texture;
	
	public NetherStairsBlock(String name, int id) {
		super(name, id, NetherMaterials.NETHER_WOOD);
		this.setHardness(WOOD.getHardness());
		this.sounds(WOOD_SOUNDS);
		this.setName(name);
		texture = name.replace("stairs_", "");
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return TextureListener.getBlockTexture(texture);
	}
	
	@Override
	public int getTextureForSide(int side) {
		return TextureListener.getBlockTexture(texture);
	}
	
	/*@Override
	public int getDropId(int meta, Random rand) {
		return this.id;
	}
	
	@Override
	protected int droppedMeta(int meta) {
		return 0;
	}
	
	@Override
	public void onBlockRemoved(Level level, int x, int y, int z) {}*/
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1621() {
		return 10;
	}
	
	@Override
	public boolean isFullOpaque() {
		return false;
	}

	@Override
	public boolean isFullCube() {
		return false;
	}
	
	@Override
	public void method_1616(TileView arg, int i, int j, int k) {
		this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	@SuppressWarnings("rawtypes")
	public void method_1562(Level arg, int i, int j, int k, Box arg1, ArrayList arrayList) {
		int var7 = arg.getTileMeta(i, j, k);
		if (var7 == 0) {
			this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 0.5F, 1.0F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
			this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
		} else if (var7 == 1) {
			this.setBoundingBox(0.0F, 0.0F, 0.0F, 0.5F, 1.0F, 1.0F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
			this.setBoundingBox(0.5F, 0.0F, 0.0F, 1.0F, 0.5F, 1.0F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
		} else if (var7 == 2) {
			this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 0.5F, 0.5F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
			this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 1.0F, 1.0F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
		} else if (var7 == 3) {
			this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 0.5F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
			this.setBoundingBox(0.0F, 0.0F, 0.5F, 1.0F, 0.5F, 1.0F);
			super.method_1562(arg, i, j, k, arg1, arrayList);
		}

		this.setBoundingBox(0.0F, 0.0F, 0.0F, 1.0F, 1.0F, 1.0F);
	}
	
	@Override
	public void afterPlaced(Level arg, int i, int j, int k, Living arg1) {
		int var6 = MathHelper.floor((double)(arg1.yaw * 4.0F / 360.0F) + 0.5D) & 3;
		if (var6 == 0) {
			arg.setTileMeta(i, j, k, 2);
		}

		if (var6 == 1) {
			arg.setTileMeta(i, j, k, 1);
		}

		if (var6 == 2) {
			arg.setTileMeta(i, j, k, 3);
		}

		if (var6 == 3) {
			arg.setTileMeta(i, j, k, 0);
		}
	}
}
