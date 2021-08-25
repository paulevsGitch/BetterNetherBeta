package paulevs.bnb.mixin.client;

import net.minecraft.client.render.entity.LivingEntityRenderer;
import net.minecraft.client.render.entity.PlayerRenderer;
import net.minecraft.client.render.entity.model.Biped;
import net.minecraft.client.render.entity.model.EntityModelBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemBase;
import net.minecraft.item.ItemInstance;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import paulevs.bnb.item.NetherArmourItem;

@Mixin(PlayerRenderer.class)
public abstract class PlayerRendererMixin extends LivingEntityRenderer {
	public PlayerRendererMixin(EntityModelBase model, float shadow) {
		super(model, shadow);
	}

	@Shadow
	private Biped field_295;
	@Shadow
	private Biped field_296;
	
	@Inject(method = "render", at = @At("HEAD"), cancellable = true)
	private void bnb_armourRender(PlayerBase player, int slot, float f, CallbackInfoReturnable<Boolean> info) {
		ItemInstance instance = player.inventory.getArmourItem(3 - slot);
		if (instance != null) {
			ItemBase item = instance.getType();
			if (item instanceof NetherArmourItem) {
				NetherArmourItem armour = (NetherArmourItem) item;
				bindTexture(armour.getArmourTexture(slot == 2 ? 2 : 1));
				Biped model = slot == 2 ? this.field_296 : this.field_295;
				model.field_619.visible = slot == 0;
				model.field_620.visible = slot == 0;
				model.field_621.visible = slot == 1 || slot == 2;
				model.field_622.visible = slot == 1;
				model.field_623.visible = slot == 1;
				model.field_624.visible = slot == 2 || slot == 3;
				model.field_625.visible = slot == 2 || slot == 3;
				setModel(model);
				info.setReturnValue(true);
				info.cancel();
			}
		}
	}
}
