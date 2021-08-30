package paulevs.bnb.mixin.client;

import net.minecraft.block.BlockBase;
import net.minecraft.level.Level;
import net.minecraft.level.TileView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.interfaces.BlockWithLight;
import paulevs.bnb.listeners.BlockListener;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.noise.OpenSimplexNoise;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.util.RadialSearch;

@Mixin(BlockBase.class)
public abstract class BlockBaseMixin {
	private static final OpenSimplexNoise BNB_TEXTURE_NOISE = new OpenSimplexNoise(123);
	
	@Inject(method = "getTextureForSide", at = @At("HEAD"), cancellable = true)
	private void bnb_getTextureForSide(int side, int meta, CallbackInfoReturnable<Integer> info) {
		BlockBase block = BlockBase.class.cast(this);
		if (block == BlockBase.NETHERRACK) {
			info.setReturnValue(TextureListener.getBlockTexture("netherrack"));
		}
		else if (block == BlockBase.GLOWSTONE) {
			info.setReturnValue(TextureListener.getBlockTexture("glowstone"));
		}
		else if (block == BlockBase.SOUL_SAND) {
			info.setReturnValue(TextureListener.getBlockTexture("soul_sand"));
		}
	}
	
	@Inject(method = "method_1626", at = @At("HEAD"), cancellable = true)
	private void bnb_getTextureInWorld(TileView world, int x, int y, int z, int side, CallbackInfoReturnable<Integer> info) {
		BlockBase block = BlockBase.class.cast(this);
		if (block == BlockBase.NETHERRACK) {
			float textureDist = RadialSearch.search(x, y, z, pos -> {
				int tileID = world.getTileId(pos.x, pos.y, pos.z);
				return tileID == BlockBase.SOUL_SAND.id || tileID == BlockListener.getBlockID("soul_soil");
			}, 5) - (MHelper.getRandomHash(y, x, z) & 7) / 8F;
			for (int i = 0; i < 3; i++) {
				if (textureDist <= i + 1) {
					info.setReturnValue((TextureListener.getBlockTexture("netherrack_soil_" + i)));
					return;
				}
			}
			if (BlockUtil.isHorizontalSide(side)) {
				if (BNB_TEXTURE_NOISE.eval(x * 0.1, y * 0.1, z * 0.1) > 0.3) {
					info.setReturnValue((TextureListener.getBlockTexture("layered_netherrack")));
				}
			}
		}
	}
	
	@Inject(method = "method_1604", at = @At("HEAD"), cancellable = true)
	private void bnb_getLight(TileView world, int x, int y, int z, CallbackInfoReturnable<Float> info) {
		if (this instanceof BlockWithLight && BlockUtil.isLightPass()) {
			info.setReturnValue(((BlockWithLight) (Object) this).getEmissionIntensity());
			info.cancel();
		}
	}
	
	@Inject(method = "onBlockPlaced", at = @At("TAIL"))
	public void bnb_onBlockPlaced(Level level, int x, int y, int z, CallbackInfo info) {
		BlockBase block = BlockBase.class.cast(this);
		if (block == BlockBase.SOUL_SAND || block == BlockListener.getBlock("soul_soil")) {
			BlockUtil.updateArea(level, x - 3, y - 3, z - 3, x + 3, y + 3, z + 3);
		}
	}
	
	@Inject(method = "onBlockRemoved", at = @At("TAIL"))
	public void bnb_onBlockRemoved(Level level, int x, int y, int z, CallbackInfo info) {
		BlockBase block = BlockBase.class.cast(this);
		if (block == BlockBase.SOUL_SAND || block == BlockListener.getBlock("soul_soil")) {
			BlockUtil.updateArea(level, x - 3, y - 3, z - 3, x + 3, y + 3, z + 3);
		}
	}
}
