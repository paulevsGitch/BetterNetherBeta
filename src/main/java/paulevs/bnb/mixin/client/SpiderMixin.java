package paulevs.bnb.mixin.client;

import net.minecraft.client.model.Cuboid;
import net.minecraft.client.render.entity.model.Spider;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Spider.class)
public class SpiderMixin {
	@Shadow
	public Cuboid field_2448;
	@Shadow
	public Cuboid field_2449;
	@Shadow
	public Cuboid field_2450;
	@Shadow
	public Cuboid field_2451;//
	@Shadow
	public Cuboid field_2452;
	@Shadow
	public Cuboid field_2453;//
	@Shadow
	public Cuboid field_2454;
	@Shadow
	public Cuboid field_2455;//
	@Shadow
	public Cuboid field_2456;
	@Shadow
	public Cuboid field_2457;//
	@Shadow
	public Cuboid field_2458;
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	private void bnb_onSpiderModelInit(CallbackInfo info) {
		field_2451 = new Cuboid(18, 0);
		field_2451.mirror = true;
		field_2451.method_1818(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0);
		field_2451.setRotationPoint(-4.0F, 15.0F, 2.0F);
		
		field_2453 = new Cuboid(18, 0);
		field_2453.mirror = true;
		field_2453.method_1818(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0);
		field_2453.setRotationPoint(-4.0F, 15.0F, 1.0F);
		
		field_2455 = new Cuboid(18, 0);
		field_2455.mirror = true;
		field_2455.method_1818(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0);
		field_2455.setRotationPoint(-4.0F, 15.0F, 0.0F);
		
		field_2457 = new Cuboid(18, 0);
		field_2457.mirror = true;
		field_2457.method_1818(-15.0F, -1.0F, -1.0F, 16, 2, 2, 0);
		field_2457.setRotationPoint(-4.0F, 15.0F, -1.0F);
	}
}
