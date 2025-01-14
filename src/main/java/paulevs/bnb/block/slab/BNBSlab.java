package paulevs.bnb.block.slab;

import net.minecraft.block.Block;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.maths.MCMath;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.template.block.TemplateBlock;
import net.modificationstation.stationapi.api.util.Identifier;

import java.util.function.Function;

public class BNBSlab extends TemplateBlock {
	private final Function<Integer, Integer> textureGetter;
	
	public BNBSlab(Identifier id, Block source) {
		super(id, source.material);
		this.textureGetter = source::getTexture;
		setTranslationKey(id);
		setSounds(source.sounds);
	}
	
	@Override
	public int getTexture(int side) {
		return textureGetter.apply(side);
	}
	
	static HitResult raycast(Level level, PlayerEntity player) {
		double dist = 5.0;
		float toRadians = (float) Math.PI / 180;
		float pitch = player.prevPitch + (player.pitch - player.prevPitch);
		
		double x = player.prevX + (player.x - player.prevX);
		double y = player.prevY + (player.y - player.prevY) + 1.62 - (double) player.standingEyeHeight;
		double z = player.prevZ + (player.z - player.prevZ);
		Vec3D pos = Vec3D.getFromCacheAndSet(x, y, z);
		
		float yaw = player.prevYaw + (player.yaw - player.prevYaw);
		yaw = -yaw * toRadians - (float) Math.PI;
		float cosYaw = MCMath.cos(yaw);
		float sinYaw = MCMath.sin(yaw);
		float cosPitch = -MCMath.cos(-pitch * toRadians);
		
		Vec3D dir = pos.add(
			sinYaw * cosPitch * dist,
			(MCMath.sin(-pitch * ((float) Math.PI / 180))) * dist,
			cosYaw * cosPitch * dist
		);
		
		return level.getHitResult(pos, dir, false);
	}
}
