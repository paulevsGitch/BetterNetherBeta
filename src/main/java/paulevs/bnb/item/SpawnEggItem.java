package paulevs.bnb.item;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;
import paulevs.bnb.entity.CloudEntity;
import paulevs.bnb.interfaces.ItemWithMeta;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.DyeColors;
import paulevs.bnb.util.MHelper;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.function.Function;

public class SpawnEggItem extends NetherItem implements ItemWithMeta {
	private final List<Function<Level, ? extends EntityBase>> entities;
	private final int[] colors;
	
	public SpawnEggItem(String name, int id) {
		super(name, id);
		
		entities = Lists.newArrayList();
		entities.add(level -> new Creeper(level));
		entities.add(level -> {
			CloudEntity entity = new CloudEntity(level);
			entity.setColor(DyeColors.values()[MHelper.getRandom().nextInt(16)].getColor());
			return entity;
		});
		
		colors = new int[entities.size()];
		colors[0] = DyeColors.GREEN.getColor();
		colors[1] = DyeColors.BLUE.getColor();
	}
	
	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture("spawn_egg");
	}
	
	@Override
	public boolean useOnTile(ItemInstance item, PlayerBase player, Level level, int x, int y, int z, int facing) {
		Vec3i pos = BlockDirection.fromFacing(facing).offset(new Vec3i(x, y, z));
		if (level.getTileId(pos.x, pos.y, pos.z) != 0) {
			return false;
		}
		
		int index = item.getDamage();
		if (index >= entities.size()) {
			return false;
		}
		
		try {
			EntityBase entity = entities.get(index).apply(level);
			entity.setPositionAndAngles(pos.x + 0.5, pos.y, pos.z + 0.5, level.rand.nextFloat() * 360, 0);
			level.spawnEntity(entity);
		}
		catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		
		return true;
	}
	
	@Override
	public int getMaxMeta() {
		return entities.size();
	}
}
