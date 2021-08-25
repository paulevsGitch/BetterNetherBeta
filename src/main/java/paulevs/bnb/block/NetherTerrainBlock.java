package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.material.Material;
import net.minecraft.level.TileView;
import net.minecraft.util.maths.MathHelper;
import paulevs.bnb.block.sound.NetherBlockSounds;
import paulevs.bnb.block.types.NetherTerrain;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class NetherTerrainBlock extends MultiBlock implements BlockWithLight {
	public NetherTerrainBlock(String name, int id) {
		super(name, id, Material.STONE, NetherTerrain.class);
		this.setHardness(NETHERRACK.getHardness());
		this.sounds(NetherBlockSounds.NYLIUM);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		int meta = world.getTileMeta(x, y, z);
		if (meta != NetherTerrain.CORRUPTED_NYLIUM.getMeta() || !BlockUtil.isTopSide(side)) {
			return super.method_1626(world, x, y, z, side);
		}
		
		float noise = 1 - Math.abs(MHelper.getNoiseValue(x * 0.1, z * 0.1));
		noise = noise * noise * noise;
		int seed = MHelper.getRandomHash(y, x, z);
		Random random = MHelper.getRandom();
		random.setSeed(seed);
		noise = MHelper.lerp(noise, random.nextFloat(), 0.5F);
		int index = MathHelper.floor(noise * 8 - 3);
		index = MHelper.clamp(index, 0, 3);
		
		String name = variants[clampMeta(meta)].getTexture(side) + "_" + index;
		return TextureListener.getBlockTexture(name);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		if (meta == NetherTerrain.CORRUPTED_NYLIUM.getMeta() && BlockUtil.isTopSide(side)) {
			String name = variants[clampMeta(meta)].getTexture(side) + "_0";
			return TextureListener.getBlockTexture(name);
		}
		return super.getTextureForSide(side, meta);
	}
	
	@Override
	public float getEmissionIntensity() {
		return 2;
	}
}
