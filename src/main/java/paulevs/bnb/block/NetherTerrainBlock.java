package paulevs.bnb.block;

import net.minecraft.block.material.Material;
import net.modificationstation.stationapi.api.registry.Identifier;
import net.modificationstation.stationapi.api.template.block.TemplateBlockBase;
import paulevs.bnb.BNB;
import paulevs.bnb.interfaces.MultiTexturedBlock;
import paulevs.bnb.sound.NetherSounds;

public class NetherTerrainBlock extends TemplateBlockBase implements MultiTexturedBlock {
	private final int[] textures = new int[3];
	
	public NetherTerrainBlock(Identifier id, Material material) {
		super(id, Material.STONE);
	}
	
	public NetherTerrainBlock(Identifier id) {
		super(id, Material.STONE);
		setHardness(NETHERRACK.getHardness());
		setSounds(NetherSounds.NYLIUM_BLOCK);
	}
	
	@Override
	public int[] getTextureStorage() {
		return textures;
	}
	
	@Override
	public Identifier[] getTextureNames(Identifier id) {
		return new Identifier[] {
			BNB.id("block/netherrack"),
			BNB.id("block/" + id.id + "_top"),
			BNB.id("block/" + id.id + "_side")
		};
	}
	
	@Override
	public int getTextureForSide(int side) {
		return side < 2 ? textures[side] : textures[2];
	}
	
	/*@Override
	@Environment(EnvType.CLIENT)
	public int method_1626(TileView world, int x, int y, int z, int side) {
		int meta = world.getTileMeta(x, y, z);
		if (meta != NetherTerrainType.CORRUPTED_NYLIUM.getMeta() || !BlockUtil.isTopSide(side)) {
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
		if (meta == NetherTerrainType.CORRUPTED_NYLIUM.getMeta() && BlockUtil.isTopSide(side)) {
			String name = variants[clampMeta(meta)].getTexture(side) + "_0";
			return TextureListener.getBlockTexture(name);
		}
		return super.getTextureForSide(side, meta);
	}*/
}
