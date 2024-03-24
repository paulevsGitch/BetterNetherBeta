package paulevs.bnb.gui.screen;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.gui.screen.container.ContainerScreen;
import org.lwjgl.opengl.GL11;
import paulevs.bnb.block.SpinningWheelBlock;
import paulevs.bnb.gui.container.SpinningWheelContainer;

@Environment(EnvType.CLIENT)
public class SpinningWheelScreen extends ContainerScreen {
	private int backgroundTexture = -1;
	
	public SpinningWheelScreen(SpinningWheelContainer container) {
		super(container);
	}
	
	@Override
	protected void renderContainerBackground(float delta) {
		if (backgroundTexture == -1) {
			backgroundTexture = minecraft.textureManager.getTextureId("/assets/bnb/stationapi/textures/gui/spinning_wheel.png");
		}
		
		GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
		minecraft.textureManager.bindTexture(backgroundTexture);
		int posX = (width - containerWidth) / 2;
		int posY = (height - containerHeight) / 2;
		blit(posX, posY, 0, 0, containerWidth, containerHeight);
		
		int side = (int) Math.ceil(19 * SpinningWheelBlock.currentEntity.getProcess());
		blit(posX + 79, posY + 44, 176, 0, side, 12);
		
		String name = SpinningWheelBlock.currentEntity.getInventoryName();
		int px = (176 - textManager.getTextWidth(name)) >> 1;
		textManager.drawText(name, posX + px, posY + 6, 0x404040);
		
		name = "Inventory";
		px = (176 - textManager.getTextWidth(name)) >> 1;
		textManager.drawText(name, posX + px, posY + this.containerHeight - 94, 0x404040);
	}
}
