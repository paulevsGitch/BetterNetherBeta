package paulevs.bnb.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.Dye;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.maths.Vec3i;
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
				Vec3i[] offsets = MHelper.getOffsets(level.rand);
				for (Vec3i offset: offsets) {
					int px = x + offset.x;
					int py = y + offset.y;
					int pz = z + offset.z;
					block = BlockUtil.getBlock(level, px, py, pz);
					if (block instanceof NetherTerrainBlock) {
						grass = block;
						meta = level.getTileMeta(px, py, pz);
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
}
