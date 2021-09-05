package paulevs.bnb.item;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;
import paulevs.bnb.interfaces.ItemWithMeta;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;

import java.util.List;
import java.util.function.Function;

public abstract class SpawnEggItem extends NetherItem implements ItemWithMeta {
	private final List<Function<Level, ? extends EntityBase>> entities = Lists.newArrayList();
	private final List<Integer> colorBack = Lists.newArrayList();
	private final List<Integer> colorDots = Lists.newArrayList();
	private final List<String> names = Lists.newArrayList();
	
	public SpawnEggItem(String name, int id) {
		super(name, id);
		addMobs();
	}
	
	protected abstract void addMobs();
	
	protected void addMob(String name, int colorBack, int colorDots, Function<Level, ? extends EntityBase> provider) {
		this.colorBack.add(colorBack);
		this.colorDots.add(colorDots);
		this.entities.add(provider);
		this.names.add(name);
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public String getTranslationKey(ItemInstance item) {
		return names.get(item.getDamage()) + " Egg";
	}
	
	@Override
	@Environment(EnvType.CLIENT)
	public int getTexturePosition(int damage) {
		return TextureListener.getItemTexture("spawn_egg");
	}
	
	@Environment(EnvType.CLIENT)
	public int getOverlayTexturePosition(int damage) {
		return TextureListener.getItemTexture("spawn_egg_overlay");
	}
	
	@Environment(EnvType.CLIENT)
	public int getBackColor(int damage) {
		return colorBack.get(damage);
	}
	
	@Environment(EnvType.CLIENT)
	public int getDotsColor(int damage) {
		return colorDots.get(damage);
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
			item.count--;
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
