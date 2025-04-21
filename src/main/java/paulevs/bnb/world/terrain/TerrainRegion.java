package paulevs.bnb.world.terrain;

public enum TerrainRegion {
	OCEAN_NORMAL,
	OCEAN_MOUNTAINS,
	SHORE_NORMAL,
	SHORE_MOUNTAINS,
	PLAINS,
	HILLS,
	MOUNTAINS,
	BRIDGES,
	RIVERS;
	
	public boolean isOcean() {
		return this == OCEAN_NORMAL || this == OCEAN_MOUNTAINS;
	}
	
	public boolean isShore() {
		return this == SHORE_NORMAL || this == SHORE_MOUNTAINS;
	}
	
	public boolean isLand() {
		return this != RIVERS && !isOcean() && !isShore();
	}
}
