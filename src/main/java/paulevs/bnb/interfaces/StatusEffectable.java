package paulevs.bnb.interfaces;

import paulevs.bnb.effects.StatusEffect;

import java.util.Collection;

public interface StatusEffectable {
	void addEffect(StatusEffect effect);
	
	StatusEffect getEffect(String name);
	
	void removeEffect(String name);
	
	Collection<StatusEffect> getEffects();
}
