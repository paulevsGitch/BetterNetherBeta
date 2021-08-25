package paulevs.bnb.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.Dye;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import org.lwjgl.util.Point;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.block.NetherTerrainBlock;
import paulevs.bnb.interfaces.Bonemealable;
import paulevs.bnb.util.BlockState;
import paulevs.bnb.util.BlockUtil;
import paulevs.bnb.util.BonemealUtil;
import paulevs.bnb.util.MHelper;

@Mixin(Dye.class)
public class DyeMixin {
	private static final Point[] OFFSETS;
	
	@Inject(method = "useOnTile", at = @At("HEAD"), cancellable = true)
	private void bnb_onBonemealUse(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing, CallbackInfoReturnable<Boolean> info) {
		if (item.getDamage() == 15) {
			BlockBase block = BlockUtil.getBlock(level, x, y, z);
			if (block instanceof Bonemealable) {
				if (((Bonemealable) block).onBonemealUse(level, x, y, z, level.getTileMeta(x, y, z))) {
					item.count--;
					info.setReturnValue(true);
					info.cancel();
				}
			}
			else if (block == BlockBase.NETHERRACK && level.getTileId(x, y + 1, z) == 0) {
				int meta = 0;
				BlockBase grass = null;
				MHelper.shuffle(OFFSETS, level.rand);
				for (Point offset: OFFSETS) {
					int px = x + offset.getX();
					int pz = z + offset.getY();
					block = BlockUtil.getBlock(level, px, y, pz);
					if (block instanceof NetherTerrainBlock) {
						grass = block;
						meta = level.getTileMeta(px, y, pz);
						break;
					}
				}
				if (grass != null) {
					level.placeBlockWithMetaData(x, y, z, grass.id, meta);
					item.count--;
					info.setReturnValue(true);
					info.cancel();
				}
			}
			else if (level.getTileId(x, y + 1, z) == 0) {
				if (BonemealUtil.growGrass(level, x, y, z, new BlockState(block, level.getTileMeta(x, y, z)))) {
					item.count--;
					info.setReturnValue(true);
					info.cancel();
				}
			}
		}
	}
	
	static {
		int index = 0;
		OFFSETS = new Point[8];
		for (int x = -1; x < 2; x++) {
			for (int y = -1; y < 2; y++) {
				if (x != 0 || y != 0) {
					OFFSETS[index++] = new Point(x, y);
				}
			}
		}
	}
}
