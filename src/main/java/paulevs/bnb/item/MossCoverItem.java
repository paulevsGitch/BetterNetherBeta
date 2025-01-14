package paulevs.bnb.item;

import net.minecraft.block.BlockSounds;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.maths.BlockPos;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import net.modificationstation.stationapi.api.util.math.Direction;
import paulevs.bnb.block.BNBBlocks;

public class MossCoverItem extends TemplateItem {
	public MossCoverItem(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public boolean useOnBlock(ItemStack item, PlayerEntity player, Level level, int x, int y, int z, int side) {
		Direction direction = Direction.byId(side);
		x += direction.getOffsetX();
		y += direction.getOffsetY();
		z += direction.getOffsetZ();
		if (!BNBBlocks.NETHER_MOSS_COVER.canPlaceAt(level, x, y, z)) return false;
		BlockState state = BNBBlocks.NETHER_MOSS_COVER.getStateForItem(
			level, new BlockPos(x, y, z), direction.getOpposite()
		);
		if (state == null) return false;
		level.setBlockStateWithNotify(x, y, z, state);
		BlockSounds sound = BNBBlocks.NETHER_MOSS_COVER.sounds;
		level.playSound(x + 0.5, y + 0.5, z + 0.5, sound.getWalkSound(), sound.getVolume(), sound.getPitch());
		item.count--;
		return true;
	}
}
