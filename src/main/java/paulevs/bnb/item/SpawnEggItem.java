package paulevs.bnb.item;

import com.google.common.collect.Lists;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.entity.EntityBase;
import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.Spider;
import net.minecraft.entity.player.PlayerBase;
import net.minecraft.item.ItemInstance;
import net.minecraft.level.Level;
import net.minecraft.util.Vec3i;
import paulevs.bnb.effects.StatusEffect;
import paulevs.bnb.effects.StatusEffects;
import paulevs.bnb.entity.CloudEntity;
import paulevs.bnb.interfaces.ItemWithMeta;
import paulevs.bnb.interfaces.NetherMob;
import paulevs.bnb.listeners.TextureListener;
import paulevs.bnb.util.BlockDirection;
import paulevs.bnb.util.DyeColors;
import paulevs.bnb.util.MHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;

public class SpawnEggItem extends NetherItem implements ItemWithMeta {
	private final List<Function<Level, ? extends EntityBase>> entities = Lists.newArrayList();
	private final List<Integer> colorBack = Lists.newArrayList();
	private final List<Integer> colorDots = Lists.newArrayList();
	private final List<String> names = Lists.newArrayList();
	
	public SpawnEggItem(String name, int id) {
		super(name, id);
		
		addMob("Crimson Spider", hexToInt("442131"), hexToInt("e23f36"), level -> {
			Spider entity = new Spider(level);
			((NetherMob) entity).setMobType(1);
			return entity;
		});
		
		addMob("Warped Spider", hexToInt("442131"), hexToInt("14b485"), level -> {
			Spider entity = new Spider(level);
			((NetherMob) entity).setMobType(2);
			return entity;
		});
		
		addMob("Poison Spider", hexToInt("442131"), hexToInt("6dda3c"), level -> {
			Spider entity = new Spider(level);
			((NetherMob) entity).setMobType(3);
			return entity;
		});
		
		addMob("Soul Creeper", hexToInt("32251e"), hexToInt("66b8fe"), level -> {
			Creeper entity = new Creeper(level);
			((NetherMob) entity).setMobType(1);
			return entity;
		});
		
		addMob("Random Cloud", hexToInt("7e7e7e"), hexToInt("e7e7e7"), level -> {
			CloudEntity entity = new CloudEntity(level);
			entity.setColor(DyeColors.values()[MHelper.getRandom().nextInt(16)].getColor());
			Collection<Supplier<StatusEffect>> effects = StatusEffects.getAllEffects();
			int index = MHelper.getRandom().nextInt(effects.size());
			Iterator<Supplier<StatusEffect>> iterator = effects.iterator();
			for (int i = 0; i < index; i++) {
				iterator.next();
			}
			entity.setStatusEffect(iterator.next().get());
			return entity;
		});
	}
	
	protected void addMob(String name, int colorBack, int colorDots, Function<Level, ? extends EntityBase> provider) {
		this.colorBack.add(colorBack);
		this.colorDots.add(colorDots);
		this.entities.add(provider);
		this.names.add(name);
	}
	
	private int hexToInt(String hex) {
		int r = Integer.parseInt(hex.substring(0, 2), 16);
		int g = Integer.parseInt(hex.substring(2, 4), 16);
		int b = Integer.parseInt(hex.substring(4, 6), 16);
		return r << 16 | g << 8 | b;
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
