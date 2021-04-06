package paulevs.bnb.tab;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.gui.screen.container.ContainerBase;
import net.minecraft.container.Chest;
import net.minecraft.inventory.InventoryBase;

public class CreativeScreen extends ContainerBase {
	private InventoryBase inventory;
	private int rows = 0;
	
	public CreativeScreen(InventoryBase playerInventory) {
		super(null);
		inventory = new BETabInventory();
		container = new Chest(playerInventory, inventory);
		this.rows = BETabInventory.ROWS;
		this.containerHeight = 114 + this.rows * 18;
	}

	@Override
	protected void renderContainerBackground(float f) {
		int var2 = this.minecraft.textureManager.getTextureId("/gui/container.png");
		GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
		this.minecraft.textureManager.bindTexture(var2);
		int var3 = (this.width - this.containerWidth) / 2;
		int var4 = (this.height - this.containerHeight) / 2;
		this.blit(var3, var4, 0, 0, this.containerWidth, this.rows * 18 + 17);
		this.blit(var3, var4 + this.rows * 18 + 17, 0, 126, this.containerWidth, 96);
	}
	
	@Override
	protected void renderForeground() {
		this.textManager.drawText(inventory.getContainerName(), 8, 6, 4210752);
	}
}
