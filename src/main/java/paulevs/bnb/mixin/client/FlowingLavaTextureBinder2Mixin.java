package paulevs.bnb.mixin.client;

import net.minecraft.client.render.FlowingLavaTextureBinder2;
import net.minecraft.client.render.TextureBinder;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import paulevs.bnb.util.ResourceUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.io.InputStream;

@Mixin(FlowingLavaTextureBinder2.class)
public abstract class FlowingLavaTextureBinder2Mixin extends TextureBinder {
	private static final int FRAMES = 32;
	private static final int SPEED = 2;
	private static final int MAX_TIME = FRAMES * SPEED;
	private int[] storage = new int[16 * 512];
	private int timer = 0;
	
	public FlowingLavaTextureBinder2Mixin(int i) {
		super(i);
	}
	
	@Inject(method = "<init>*", at = @At("TAIL"))
	public void bnb_onBinderInit(CallbackInfo info) {
		InputStream stream = ResourceUtil.getResourceAsStream("/assets/bnb/textures/replacements/lava_flow.png");
		if (stream != null) {
			try {
				BufferedImage img = ImageIO.read(stream);
				stream.close();
				img.getRGB(0, 0, 16, 512, storage, 0, 16);
			}
			catch (IOException e) {
				e.printStackTrace();
			}
		}
	}
	
	@Inject(method = "setup", at = @At("HEAD"), cancellable = true)
	public void bnb_setupLavaTexture(CallbackInfo info) {
		timer++;
		if (timer >= MAX_TIME) {
			timer = 0;
		}
		int frame = timer / SPEED;
		for(int i = 0; i < 256; ++i) {
			int x = i & 15;
			int y = i >> 4 | frame << 4;
			int index = i << 2;
			int argb = storage[y << 4 | x];
			this.grid[index] = (byte) ((argb >> 16) & 255);
			this.grid[index | 1] = (byte) ((argb >> 8) & 255);
			this.grid[index | 2] = (byte) (argb & 255);
			this.grid[index | 3] = (byte) 255;
		}
		info.cancel();
	}
}
