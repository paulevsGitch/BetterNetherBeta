package paulevs.bnb.block;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Torch;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.item.PlaceableTileEntity;
import net.minecraft.level.Level;
import net.modificationstation.stationloader.api.common.block.BlockItemProvider;
import net.modificationstation.stationloader.impl.common.preset.item.PlaceableTileEntityWithMeta;
import paulevs.bnb.BetterNetherBeta;
import paulevs.bnb.effects.SoulProtectionEffect;
import paulevs.bnb.effects.StatusEffect;
import paulevs.bnb.interfaces.StatusEffectable;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.particles.SoulParticle;
import paulevs.bnb.util.ClientUtil;
import paulevs.bnb.util.MHelper;

import java.util.Random;

public class SoulTorchBlock extends Torch implements BlockItemProvider {
	private final String name;
	
	public SoulTorchBlock(String name, int id) {
		super(id, 0);
		this.sounds(WOOD_SOUNDS);
		this.setLightEmittance(0.9F);
		this.name = name;
	}
	
	@Override
	public PlaceableTileEntity getBlockItem(int i) {
		return new PlaceableTileEntityWithMeta(i) {
			@Override
			@Environment(EnvType.CLIENT)
			public int getTexturePosition(int damage) {
				return getTextureForSide(0);
			}
			
			@Override
			@Environment(EnvType.CLIENT)
			public String getTranslationKey(ItemInstance item) {
				return "tile." + BetterNetherBeta.MOD_ID + ":" + name;
			}
		};
	}
	
	@Override
	public int getTextureForSide(int side) {
		return TextureListener.getBlockTexture(name);
	}
	
	@Override
	public int getTextureForSide(int side, int meta) {
		return getTextureForSide(side);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(Level level, int x, int y, int z, Random rand) {
		if (rand.nextInt(4) > 0) {
			return;
		}
		int meta = level.getTileMeta(x, y, z);
		double posX = x + 0.5F;
		double posY = y + 0.7F;
		double posZ = z + 0.5F;
		posX += meta == 1 ? -0.27F : meta == 2 ? 0.27F : 0;
		posZ += meta == 3 ? -0.27F : meta == 4 ? 0.27F : 0;
		posY += meta < 5 ? 0.22F : 0;
		ClientUtil.addParticle(new SoulParticle(level, posX, posY, posZ));
	}
	
	public void onScheduledTick(Level level, int x, int y, int z, Random rand) {
		level.players.stream().filter(entity -> {
			PlayerBase player = (PlayerBase) entity;
			int dx = MHelper.abs((int) (player.x + 0.5F) - x);
			int dy = MHelper.abs((int) (player.y + 0.5F) - y);
			int dz = MHelper.abs((int) (player.z + 0.5F) - z);
			return dx < 16 && dy < 16 && dz < 16;
		}).forEach(player -> {
			StatusEffectable effectable = (StatusEffectable) player;
			StatusEffect effect = effectable.getEffect(SoulProtectionEffect.NAME);
			if (effect == null) {
				effect = new SoulProtectionEffect();
				effectable.addEffect(effect);
			}
			else {
				effect.resetTime();
			}
		});
	}
}
