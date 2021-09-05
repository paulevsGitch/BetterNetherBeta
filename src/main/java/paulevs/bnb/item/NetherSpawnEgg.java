package paulevs.bnb.item;

import net.minecraft.entity.monster.Creeper;
import net.minecraft.entity.monster.Spider;
import paulevs.bnb.effects.StatusEffect;
import paulevs.bnb.effects.StatusEffects;
import paulevs.bnb.entity.CloudEntity;
import paulevs.bnb.interfaces.NetherMob;
import paulevs.bnb.util.DyeColors;
import paulevs.bnb.util.MHelper;

import java.util.Collection;
import java.util.Iterator;
import java.util.function.Supplier;

public class NetherSpawnEgg extends SpawnEggItem {
	public NetherSpawnEgg(String name, int id) {
		super(name, id);
	}
	
	@Override
	protected void addMobs() {
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
}
