package paulevs.bnb.util;

import it.unimi.dsi.fastutil.objects.ReferenceOpenHashSet;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.loader.api.FabricLoader;
import net.glasslauncher.mods.alwaysmoreitems.gui.screen.OverlayScreen;
import net.minecraft.block.Block;
import net.minecraft.item.ItemStack;
import net.modificationstation.stationapi.api.block.BlockState;
import paulevs.materialexcavator.MaterialExcavator;

import java.util.Set;

public class CompatUtil {
	private static final boolean HAS_ME = FabricLoader.getInstance().isModLoaded("materialexcavator");
	private static final boolean HAS_AMI = FabricLoader.getInstance().isModLoaded("alwaysmoreitems");
	
	public static float scaleMiningSpeed(float speed) {
		if (!HAS_ME) return speed;
		return MaterialExcavator.scaleSpeed(speed);
	}
	
	@Environment(EnvType.CLIENT)
	public static boolean isAMIItem(ItemStack stack) {
		if (!HAS_AMI) return false;
		if (OverlayScreen.INSTANCE.hoveredItem == null) return false;
		return stack.itemId == OverlayScreen.INSTANCE.hoveredItem.item().itemId;
	}
	
	public static void registerExcavatorPattern(Block... blocks) {
		if (!HAS_ME) return;
		Set<BlockState> pattern = new ReferenceOpenHashSet<>();
		for (Block block : blocks) {
			pattern.addAll(block.getStateManager().getStates());
		}
		MaterialExcavator.registerPattern(pattern);
	}
}
