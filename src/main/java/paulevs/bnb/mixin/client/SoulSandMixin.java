package paulevs.bnb.mixin.client;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.BlockBase;
import net.minecraft.block.SoulSand;
import net.minecraft.block.material.Material;
import net.minecraft.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import paulevs.bnb.particles.SoulSandParticle;
import paulevs.bnb.util.ClientUtil;

import java.util.Random;

@Mixin(SoulSand.class)
public class SoulSandMixin extends BlockBase {
	public SoulSandMixin(int i, int j) {
		super(i, j, Material.SAND);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public void randomDisplayTick(Level level, int x, int y, int z, Random rand) {
		if (rand.nextInt(32) > 0 || level.getTileId(x, y + 1, z) != 0) {
			return;
		}
		double posY = y + 1.0F;
		double posX = x + rand.nextFloat();
		double posZ = z + rand.nextFloat();
		ClientUtil.addParticle(new SoulSandParticle(level, posX, posY, posZ));
	}
}
