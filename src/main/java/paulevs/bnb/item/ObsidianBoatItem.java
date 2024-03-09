package paulevs.bnb.item;

import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityRegistry;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.hit.HitType;
import net.minecraft.util.maths.MathHelper;
import net.minecraft.util.maths.Vec3D;
import net.modificationstation.stationapi.api.template.item.TemplateBoatItem;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.vbe.utils.CreativeUtil;

public class ObsidianBoatItem extends TemplateBoatItem {
	public ObsidianBoatItem(Identifier id) {
		super(id);
	}
	
	@Override
	public ItemStack use(ItemStack stack, Level level, PlayerEntity player) {
		float pi = (float) Math.PI;
		float toDeg = pi / 180.0F;
		Vec3D start = Vec3D.getFromCacheAndSet(player.x, player.y, player.z);
		float cosY = MathHelper.cos(-player.yaw * toDeg - pi);
		float sinY = MathHelper.sin(-player.yaw * toDeg - pi);
		float cosP = -MathHelper.cos(-player.pitch * toDeg);
		Vec3D end = start.add(sinY * cosP * 5.0F, MathHelper.sin(-player.pitch * toDeg) * 5.0F, cosY * cosP * 5.0F);
		
		HitResult hit = level.getHitResult(start, end, true);
		if (hit == null) return stack;
		
		if (hit.type == HitType.BLOCK) {
			int px = hit.x;
			int py = hit.y;
			int pz = hit.z;
			if (!level.isRemote) {
				if (level.getBlockId(px, py, pz) == Block.SNOW.id) py--;
				Entity entity = EntityRegistry.create("bnb_obsidian_boat", level);
				entity.setPosition(px + 0.5, py + 1.5, pz + 0.5);
				level.spawnEntity(entity);
			}
			if (!CreativeUtil.isCreative(player)) stack.count--;
		}
		
		return stack;
	}
}
