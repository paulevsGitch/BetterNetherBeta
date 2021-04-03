package paulevs.bnb.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.client.Minecraft;
import net.minecraft.level.biome.Biome;
import net.minecraft.level.dimension.Dimension;
import net.minecraft.level.dimension.Nether;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3f;
import paulevs.bnb.util.MHelper;
import paulevs.bnb.world.NetherBiomeSource;
import paulevs.bnb.world.biome.NetherBiome;

@Mixin(Nether.class)
public abstract class NetherMixin extends Dimension {
	private static final Vec3f DEFAULT_COLOR = Vec3f.method_1293(0.2F, 0.03F, 0.03F);
	private static final Vec3f COLOR_A = Vec3f.method_1293(0, 0, 0);
	private static final Vec3f COLOR_B = Vec3f.method_1293(0, 0, 0);
	
	@Inject(method = "initBiomeSource", at = @At("TAIL"))
	private void bnb_initBiomeSource(CallbackInfo info) {
		this.biomeSource = new NetherBiomeSource(level.getSeed());
	}
	
	@Inject(method = "method_1762", at = @At("HEAD"), cancellable = true)
	private void method_1762(float f, float f1, CallbackInfoReturnable<Vec3f> info) {
		@SuppressWarnings("deprecation")
		Minecraft mc = (Minecraft) FabricLoader.getInstance().getGameInstance();
		
		double dx = mc.player.x / 8.0;
		double dz = mc.player.z / 8.0;
		int x1 = MathHelper.floor(dx) << 3;
		int z1 = MathHelper.floor(dz) << 3;
		int x2 = x1 + 8;
		int z2 = z1 + 8;
		dx -= x1 >> 3;
		dz -= z1 >> 3;
		
		Vec3f a = getColor(x1, z1);
		Vec3f b = getColor(x2, z1);
		Vec3f c = getColor(x1, z2);
		Vec3f d = getColor(x2, z2);
		
		MHelper.lerp(COLOR_A, a, b, (float) dx);
		MHelper.lerp(COLOR_B, c, d, (float) dx);
		MHelper.lerp(COLOR_A, COLOR_A, COLOR_B, (float) dz);
		
		info.setReturnValue(COLOR_A);
		info.cancel();
	}
	
	private Vec3f getColor(int x, int z) {
		Biome biome = this.biomeSource.getBiome(x, z);
		if (biome instanceof NetherBiome) {
			return ((NetherBiome) biome).getFogColor();
		}
		return DEFAULT_COLOR;
	}
}
