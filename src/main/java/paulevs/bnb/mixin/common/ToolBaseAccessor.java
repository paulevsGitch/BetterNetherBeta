package paulevs.bnb.mixin.common;

import net.minecraft.block.BlockBase;
import net.minecraft.item.tool.ToolBase;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ToolBase.class)
public interface ToolBaseAccessor {
	@Accessor("effectiveBlocksBase")
	BlockBase[] bnb_getEffective();
}
