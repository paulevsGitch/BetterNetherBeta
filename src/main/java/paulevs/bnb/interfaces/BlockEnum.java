package paulevs.bnb.interfaces;

public interface BlockEnum {
	public String getName();
	
	public String getLocalizedName();
	
	public String getTexture(int side, int meta);
	
	public int getDropMeta();
	
	public int getMeta();
	
	public boolean isInCreative();
}
