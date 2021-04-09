package paulevs.bnb.item.material;

public enum NetherToolMaterial {
	ORICHALCUM(2, 750, 6.0F, 2);

	private final int miningLevel;
	private final int durability;
	private final float miningSpeed;
	private final int attackDamage;

	private NetherToolMaterial(int miningLevel, int durability, float miningSpeed, int attackDamage) {
		this.miningLevel = miningLevel;
		this.durability = durability;
		this.miningSpeed = miningSpeed;
		this.attackDamage = attackDamage;
	}

	public int getDurability() {
		return this.durability;
	}

	public float getMiningSpeed() {
		return this.miningSpeed;
	}

	public int getAttackDamage() {
		return this.attackDamage;
	}

	public int getMiningLevel() {
		return this.miningLevel;
	}
}
