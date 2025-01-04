package paulevs.bnb.mixin.common;

import net.minecraft.item.ItemStack;
import net.minecraft.recipe.ShapedRecipe;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ShapedRecipe.class)
public interface ShapedRecipeAccessor {
	@Accessor("width")
	int bnb_getWidth();
	
	@Accessor("height")
	int bnb_getHeight();
	
	@Accessor("ingredients")
	ItemStack[] bnb_getIngredients();
	
	@Accessor("output")
	ItemStack bnb_getOutput();
}
