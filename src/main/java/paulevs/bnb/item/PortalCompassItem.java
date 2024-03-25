package paulevs.bnb.item;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.living.LivingEntity;
import net.minecraft.entity.living.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.level.Level;
import net.minecraft.level.chunk.Chunk;
import net.minecraft.util.io.CompoundTag;
import net.minecraft.util.maths.MathHelper;
import net.modificationstation.stationapi.api.block.BlockState;
import net.modificationstation.stationapi.api.template.item.TemplateItem;
import net.modificationstation.stationapi.api.util.Identifier;
import paulevs.bnb.BNBClient;
import paulevs.bnb.rendering.CustomStackTexture;

public class PortalCompassItem extends TemplateItem implements CustomStackTexture {
	public static final int[] TEXTURES = new int[64];
	
	public PortalCompassItem(Identifier identifier) {
		super(identifier);
	}
	
	@Override
	public void inventoryTick(ItemStack stack, Level level, Entity entity, int i, boolean bl) {
		CompoundTag nbt = stack.getStationNbt();
		if (!nbt.containsKey("bnb_center")) return;
		CompoundTag center = nbt.getCompoundTag("bnb_center");
		if (!center.containsKey("dim") || level.dimension.id != center.getInt("dim")) return;
		int x = center.getInt("x");
		int z = center.getInt("z");
		Chunk chunk = level.getChunkFromCache(x >> 4, z >> 4);
		if (chunk == null) return;
		int y = center.getInt("y");
		BlockState state = chunk.getBlockState(x & 15, y, z & 15);
		if (!state.isOf(Block.PORTAL)) {
			nbt.put("bnb_center", new CompoundTag());
		}
	}
	
	@Override
	public boolean useOnBlock(ItemStack stack, PlayerEntity player, Level level, int x, int y, int z, int side) {
		if (!level.getBlockState(x, y, z).isOf(Block.PORTAL)) return false;
		CompoundTag nbt = stack.getStationNbt();
		CompoundTag center = new CompoundTag();
		nbt.put("bnb_center", center);
		center.put("dim", level.dimension.id);
		center.put("x", x);
		center.put("y", y);
		center.put("z", z);
		
		if (!level.isRemote) {
			float angle = (float) -Math.toRadians(player.yaw);
			float offsetX = MathHelper.sin(angle);
			float offsetZ = MathHelper.cos(angle);
			float a = -offsetZ;
			float b = offsetX;
			offsetX = offsetX * 0.25F + a * 0.25F;
			offsetZ = offsetZ * 0.25F + b * 0.25F;
			
			for (int i = 0; i < 20; i++) {
				float dx = level.random.nextFloat() - 0.5F;
				float dy = level.random.nextFloat() - 0.5F;
				float dz = level.random.nextFloat() - 0.5F;
				level.addParticle(
					"reddust",
					player.x + dx * 0.25F + offsetX,
					player.y - 0.6 - dy,
					player.z + dz * 0.25F + offsetZ,
					0.59F, 0.24F, 0.88F
				);
			}
			level.playSound(player, "bnb:effect.compass_link", 1.0F, 1.0F);
		}
		return false;
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int getTexture(ItemStack stack) {
		int index;
		CompoundTag nbt = stack.getStationNbt();
		Minecraft minecraft = BNBClient.getMinecraft();
		if (!nbt.containsKey("bnb_center") || minecraft.level == null || minecraft.viewEntity == null) {
			index = ((int) System.currentTimeMillis() / 20 + stack.hashCode()) & 63;
		}
		else {
			CompoundTag center = nbt.getCompoundTag("bnb_center");
			if (!center.containsKey("dim") || center.getInt("dim") != minecraft.level.dimension.id) {
				index = ((int) System.currentTimeMillis() / 20 + stack.hashCode()) & 63;
			}
			else {
				LivingEntity entity = minecraft.viewEntity;
				double dx = center.getInt("x") + 0.5 - entity.x;
				double dz = center.getInt("z") + 0.5 - entity.z;
				index = (int) Math.floor((-Math.toRadians(entity.yaw) - Math.atan2(dx, dz)) * 32 / Math.PI) & 63;
			}
		}
		return TEXTURES[index];
	}
}
