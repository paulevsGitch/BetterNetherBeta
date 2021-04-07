package paulevs.bnb.interfaces;

public interface BlockEnum {
	public String getName();
	
	public String getTranslationKey();
	
	public String getTexture(int side);
	
	public int getDropMeta();
	
	public int getMeta();
	
	public boolean isInCreative();
}
